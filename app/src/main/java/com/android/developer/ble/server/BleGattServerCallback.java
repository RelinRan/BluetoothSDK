package com.android.developer.ble.server;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.android.developer.ble.utils.ByteBus;

import java.io.IOException;
import java.util.HashMap;

/**
 * Author: Relin
 * Describe: 低功耗蓝牙服务端
 * Date:2020/5/29 22:08
 */
public class BleGattServerCallback extends BluetoothGattServerCallback {

    private String TAG = "BleGattServerCallback";
    /**
     * 设备
     */
    private BluetoothDevice bluetoothDevice;
    /**
     * 通用服务端
     */
    private BluetoothServer server;
    /**
     * BLE服务端
     */
    private BleServer bleServer;
    /**
     * 连接的设备
     */
    private HashMap<String, BluetoothDevice> devices;

    /**
     * 构造函数
     *
     * @param server    通用服务端
     * @param bleServer BLE服务端
     */
    public BleGattServerCallback(BluetoothServer server, BleServer bleServer) {
        this.server = server;
        this.bleServer = bleServer;
        devices = new HashMap<>();
    }

    @Override
    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);
        Log.i(TAG, "->onConnectionStateChange() - status = " + status + " , newState = " + newState);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            String key = deviceKey(device);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothDevice = device;
                devices.put(key, device);
                server.transmit(BluetoothServer.WHAT_CONNECTED, device, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, bleServer.getBluetoothGattService(), null);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                for (int i = 0; i < devices.size(); i++) {
                    if (key.equals(deviceKey(devices.get(i)))) {
                        devices.remove(key);
                    }
                }
                server.transmit(BluetoothServer.WHAT_DISCONNECT, device, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, bleServer.getBluetoothGattService(), null);
            }
        } else {
            server.transmit(BluetoothServer.ERROR_CONNECT, device, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, new IOException("connect failed status=" + status + " , newState=" + newState), bleServer.getBluetoothGattService(), null);
        }
    }

    @Override
    public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
        super.onExecuteWrite(device, requestId, execute);
        Log.i(TAG, "->onExecuteWrite() - name = " + device.getName() + " , address = " + device.getAddress());
    }

    @Override
    public void onNotificationSent(BluetoothDevice device, int status) {
        super.onNotificationSent(device, status);
        Log.i(TAG, "->onNotificationSent() - name = " + device.getName() + " , address = " + device.getAddress() + " , status = " + status);
    }

    @Override
    public void onServiceAdded(int status, BluetoothGattService service) {
        super.onServiceAdded(status, service);
        Log.i(TAG, "->onServiceAdded() - status = " + status);
        server.transmit(BluetoothServer.WHAT_SERVICE_ADDED, null, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, service, null);
    }

    @Override
    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
        Log.i(TAG, "->onCharacteristicReadRequest() - name = " + device.getName() + " , address = " + device.getAddress() + " , data = " + ByteBus.encodeHex(characteristic.getValue()));
        bleServer.getBluetoothGattServer().sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
        server.transmit(BluetoothServer.WHAT_READ, null, BluetoothServer.DATA_CHARACTERISTIC_READ, characteristic.getValue(), BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, bleServer.getBluetoothGattService(), null);
    }

    @Override
    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        bleServer.getBluetoothGattServer().sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
        Log.i(TAG, "->onCharacteristicWriteRequest() - name = " + device.getName() + " , address = " + device.getAddress() + " , data = " + ByteBus.encodeHex(value));
        server.transmit(BluetoothServer.WHAT_WRITE, null, BluetoothServer.DATA_CHARACTERISTIC_WRITE, value, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, bleServer.getBluetoothGattService(), null);
    }

    @Override
    public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
        super.onDescriptorReadRequest(device, requestId, offset, descriptor);
        Log.i(TAG, "->onDescriptorReadRequest() - name = " + device.getName() + " , address = " + device.getAddress() + " , data = " + ByteBus.encodeHex(descriptor.getValue()));
        bleServer.getBluetoothGattServer().sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, descriptor.getValue());
        server.transmit(BluetoothServer.WHAT_WRITE, null, BluetoothServer.DATA_DESCRIPTOR_READ, descriptor.getValue(), BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, bleServer.getBluetoothGattService(), null);
    }

    @Override
    public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
        Log.i(TAG, "->onDescriptorWriteRequest() - name = " + device.getName() + " , address = " + device.getAddress() + " , data = " + ByteBus.encodeHex(value));
        bleServer.getBluetoothGattServer().sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
        server.transmit(BluetoothServer.WHAT_READ, null, BluetoothServer.DATA_DESCRIPTOR_WRITE, descriptor.getValue(), BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, bleServer.getBluetoothGattService(), null);
    }

    @Override
    public void onMtuChanged(BluetoothDevice device, int mtu) {
        super.onMtuChanged(device, mtu);
        Log.i(TAG, "->onMtuChanged() - name = " + device.getName() + " , address = " + device.getAddress() + " , mtu = " + mtu);
    }

    @Override
    public void onPhyRead(BluetoothDevice device, int txPhy, int rxPhy, int status) {
        super.onPhyRead(device, txPhy, rxPhy, status);
        Log.i(TAG, "->onPhyRead() - name = " + device.getName() + " , address = " + device.getAddress() + " , txPhy = " + txPhy + " , rxPhy = " + rxPhy + " , status = " + status);
    }

    @Override
    public void onPhyUpdate(BluetoothDevice device, int txPhy, int rxPhy, int status) {
        super.onPhyUpdate(device, txPhy, rxPhy, status);
        Log.i(TAG, "->onPhyUpdate() - name = " + device.getName() + " , address = " + device.getAddress() + " , txPhy = " + txPhy + " , rxPhy = " + rxPhy + " , status = " + status);
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public HashMap<String, BluetoothDevice> getDevices() {
        return devices;
    }

    /**
     * 获取设备KEY
     *
     * @param device
     * @return
     */
    private String deviceKey(BluetoothDevice device) {
        if (device == null) {
            return "";
        }
        return device.getName() + "&" + device.getAddress();
    }

}