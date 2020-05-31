package com.android.developer.ble.client;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.android.developer.ble.utils.ByteBus;
import com.android.developer.ble.utils.BUUID;

import java.io.IOException;
import java.util.List;


/**
 * Author: Relin
 * Describe:BLE回调类
 * Date:2020/5/29 00:14
 */
public class BleGattCallBack extends BluetoothGattCallback {

    private String TAG = "BLEGattCallBack";
    private BluetoothClient client;
    private BluetoothGattService service;
    private List<BluetoothGattService> services;
    private BluetoothGattCharacteristic writer;
    private BluetoothGattCharacteristic reader;
    private BluetoothGattCharacteristic notifier;
    private BluetoothGattCharacteristic indicate;

    public BleGattCallBack(BluetoothClient client) {
        this.client = client;
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        Log.i(TAG, "->onServicesDiscovered");
        findBluetoothGattService(gatt, client.getBUUID().value(BUUID.NAME_SERVICE));
        client.transmit(BluetoothClient.WHAT_SERVICES_DISCOVERED, BluetoothClient.DATA_NONE, null, status, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        Log.i(TAG, "->onConnectionStateChange status=" + status + ",newState=" + newState);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                client.transmit(BluetoothClient.WHAT_CONNECTED, BluetoothClient.DATA_NONE, null, status, BluetoothClient.ERROR_NONE, null);
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                client.transmit(BluetoothClient.WHAT_DISCONNECT, BluetoothClient.DATA_NONE, null, status, BluetoothClient.ERROR_NONE, null);
            }
        } else {
            client.transmit(BluetoothClient.WHAT_ERROR, BluetoothClient.DATA_NONE, null, status, BluetoothClient.ERROR_CONNECT, new IOException("onConnectionStateChange() failed status=" + status));
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        Log.i(TAG, "->onCharacteristicChanged data=" + ByteBus.encodeHex(characteristic.getValue()));
        client.transmit(BluetoothClient.WHAT_READ, BluetoothClient.DATA_CHARACTERISTIC_CHANGED, characteristic.getValue(), BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        Log.i(TAG, "->onCharacteristicRead data=" + ByteBus.encodeHex(characteristic.getValue()));
        client.transmit(BluetoothClient.WHAT_READ, BluetoothClient.DATA_CHARACTERISTIC_READ, characteristic.getValue(), BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        Log.i(TAG, "->onCharacteristicWrite data=" + ByteBus.encodeHex(characteristic.getValue()));
        client.transmit(BluetoothClient.WHAT_WRITE, BluetoothClient.DATA_CHARACTERISTIC_WRITE, characteristic.getValue(), BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
        Log.i(TAG, "->onDescriptorRead data=" + ByteBus.encodeHex(descriptor.getValue()));
        client.transmit(BluetoothClient.WHAT_READ, BluetoothClient.DATA_DESCRIPTOR_READ, descriptor.getValue(), BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        Log.i(TAG, "->onDescriptorWrite data=" + ByteBus.encodeHex(descriptor.getValue()));
        client.transmit(BluetoothClient.WHAT_WRITE, BluetoothClient.DATA_DESCRIPTOR_WRITE, descriptor.getValue(), BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        Log.i(TAG, "->onPhyUpdate txPhy=" + txPhy + ",rxPhy=" + rxPhy + ",status=" + status);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyRead(gatt, txPhy, rxPhy, status);
        Log.i(TAG, "->onPhyRead txPhy=" + txPhy + ",rxPhy=" + rxPhy + ",status=" + status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        Log.i(TAG, "->onReadRemoteRssi rssi=" + rssi + ",status=" + status);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        Log.i(TAG, "->onReliableWriteCompleted status=" + status);
    }

    /**
     * 找到需要的服务
     *
     * @param gatt
     * @param uuid
     */
    public void findBluetoothGattService(BluetoothGatt gatt, String uuid) {
        Log.i(TAG, "->findBluetoothGattService() - Target Service UUID=" + uuid);
        services = gatt.getServices();
        BUUID buuid = client.getBUUID();
        String notifyUUID = buuid.value(BUUID.NAME_CHARACTERISTIC_NOTIFY);
        for (int i = 0; i < services.size(); i++) {
            BluetoothGattService service = services.get(i);
            String serviceUUid = service.getUuid().toString();
            String serviceName = buuid.findServiceName(serviceUUid);
            serviceName = serviceName == null ? " (Unknown)" : " (" + serviceName + ")";
            Log.i(TAG, "->UUID - Service = " + service.getUuid().toString() + serviceName);
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (int j = 0; j < characteristics.size(); j++) {
                BluetoothGattCharacteristic characteristic = characteristics.get(j);
                String characteristicUUID = characteristic.getUuid().toString();
                String characteristicName = buuid.findCharacteristicName(characteristicUUID);
                int properties = characteristic.getProperties();
                int permissions = characteristic.getPermissions();
                StringBuffer propertiesName = new StringBuffer();
                if ((uuid != null && uuid.equals(serviceUUid)) || uuid == null) {
                    this.service = service;
                    //读取权限
                    if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) == BluetoothGattCharacteristic.PROPERTY_READ) {
                        reader = characteristic;
                        propertiesName.append("Read&");
                    }
                    //写入权限
                    if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) == BluetoothGattCharacteristic.PROPERTY_WRITE
                            || (properties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                        writer = characteristic;
                        propertiesName.append("Write&");
                    }
                    //通知权限
                    if ((notifyUUID != null && characteristicUUID.equals(notifyUUID))
                            || (notifyUUID == null && (properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) == BluetoothGattCharacteristic.PROPERTY_NOTIFY)) {
                        notifier = characteristic;
                        propertiesName.append("Notify&");
                    }
                    //通知权限
                    if ((properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) == BluetoothGattCharacteristic.PROPERTY_INDICATE) {
                        indicate = characteristic;
                        propertiesName.append("Indicate&");
                    }
                }
                if (propertiesName.toString().contains("&")) {
                    propertiesName.deleteCharAt(propertiesName.lastIndexOf("&"));
                }
                characteristicName = characteristicName == null ? " (Unknown)" : " (" + characteristicName + ")";
                Log.i(TAG, "->UUID - Characteristic = " + characteristic.getUuid().toString() + characteristicName + " , properties = " + properties + " (" + propertiesName.toString() + ") , permissions = " + permissions);
                List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                for (int k = 0; k < descriptors.size(); k++) {
                    BluetoothGattDescriptor descriptor = descriptors.get(k);
                    String descriptorName = buuid.findDescriptorName(descriptor.getUuid().toString());
                    descriptorName = descriptorName == null ? " (Unknown)" : " (" + descriptorName + ")";
                    int descriptorPermissions = descriptor.getPermissions();
                    Log.i(TAG, "->UUID - Descriptor = " + descriptor.getUuid().toString() + descriptorName + " , permissions = " + descriptorPermissions);
                }
            }
        }
        enableNotification(gatt, notifier == null ? indicate : notifier);
    }

    /**
     * 开启通知
     *
     * @param gatt           蓝牙操作对象
     * @param characteristic 特征
     */
    public void enableNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) {
            return;
        }
        String characteristicUUID = characteristic.getUuid().toString();
        String characteristicName = client.getBUUID().findCharacteristicName(characteristicUUID);
        int permissions = characteristic.getPermissions();
        int properties = characteristic.getProperties();
        Log.i(TAG, "->enableNotification() - characteristic UUID=" + characteristic.getUuid().toString() + " (" + characteristicName + ") , properties=" + properties + " , permissions=" + permissions);
        boolean enabled = gatt.setCharacteristicNotification(characteristic, true);
        if (enabled) {
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
            for (BluetoothGattDescriptor descriptor : descriptors) {
                if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                }
                if ((properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                }
                gatt.writeDescriptor(descriptor);
            }
        }
    }

    /**
     * 获取服务列表
     *
     * @return
     */
    public List<BluetoothGattService> getServices() {
        return services;
    }

    /**
     * 获取服务
     *
     * @return
     */
    public BluetoothGattService getService() {
        return service;
    }

    /**
     * 获取
     *
     * @return
     */
    public BluetoothGattCharacteristic getWriter() {
        return writer;
    }

    /**
     * 获取读取权限的特征
     *
     * @return
     */
    public BluetoothGattCharacteristic getReader() {
        return reader;
    }

    /**
     * 获取通知权限的特征
     *
     * @return
     */
    public BluetoothGattCharacteristic getNotifier() {
        return notifier;
    }

    /**
     * 获取象征特征
     *
     * @return
     */
    public BluetoothGattCharacteristic getIndicate() {
        return indicate;
    }
}