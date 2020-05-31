package com.android.developer.ble.server;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.android.developer.ble.Bluetooth;
import com.android.developer.ble.utils.BUUID;
import com.android.developer.ble.utils.ByteBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Relin
 * Describe: 低功耗蓝牙服务端
 * Date:2020/5/29 21:08
 */
public class BleServer {

    private String TAG = "BleServer";
    /**
     * 蓝牙操作对象
     */
    private Bluetooth bluetooth;
    /**
     * 蓝牙服务端
     */
    private BluetoothServer server;
    /**
     * 广播
     */
    private BluetoothLeAdvertiser advertiser;
    /**
     * Gatt服务端
     */
    private BluetoothGattServer bluetoothGattServer;
    /**
     * Gatt服务
     */
    private BluetoothGattService bluetoothGattService;
    /**
     * 广播设置
     */
    private AdvertiseSettings advertiseSettings;
    /**
     * 广播数据
     */
    private AdvertiseData advertiseData;
    /**
     * 服务回调
     */
    private BleGattServerCallback bleGattServerCallback;
    /**
     * 特征 - 读取
     */
    private BluetoothGattCharacteristic reader;
    /**
     * 特征写入
     */
    private BluetoothGattCharacteristic writer;
    /**
     * 特征写入
     */
    private BluetoothGattCharacteristic notifier;
    /**
     * 描述
     */
    private BluetoothGattDescriptor descriptor;
    /**
     * 广播回调
     */
    private BleAdvertiseCallback bleAdvertiseCallback;

    /**
     * 构造函数
     *
     * @param server 服务端
     */
    public BleServer(BluetoothServer server) {
        this.server = server;
        bluetooth = Bluetooth.with(server.getContext());
    }

    /**
     * 开启服务
     */
    public void start() {
        String serviceDataUUID = server.getBUUID().value(BUUID.NAME_SERVICE);
        byte[] serviceData = server.getBleServiceData();
        int manufacturerId = server.getBleManufacturerId();
        byte[] manufacturerData = server.getBleManufacturerData();
        startAdvertising(serviceDataUUID,serviceData,manufacturerId,manufacturerData);
    }

    /**
     * 关闭服务
     */
    public void close() {
        if (bluetoothGattServer != null) {
            bluetoothGattServer.close();
        }
        stopAdvertising();
    }

    /**
     * 开启广播
     */
    protected void startAdvertising(String serviceDataUUID,byte[] serviceData,int manufacturerId,byte[] manufactureData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            advertiseSettings = new AdvertiseSettings.Builder().
                    setConnectable(true)
                    .build();
            AdvertiseData.Builder builder = new AdvertiseData.Builder();
            builder.setIncludeDeviceName(true);
            builder.setIncludeTxPowerLevel(true);
            if (serviceDataUUID!=null&&serviceData != null) {
                Log.i(TAG,"->startAdvertising() addServiceData - serviceDataUUID = "+serviceDataUUID+" , serviceData = "+ByteBus.encodeHex(serviceData));
                builder.addServiceData(ParcelUuid.fromString(serviceDataUUID), serviceData);
            }
            if (manufactureData!=null){
                Log.i(TAG,"->startAdvertising() addManufacturerData - manufacturerId = "+manufacturerId+" , manufactureData = "+ByteBus.encodeHex(manufactureData));
                builder.addManufacturerData(manufacturerId,manufactureData);
            }
            advertiseData = builder.build();
            advertiser = bluetooth.bluetoothAdapter().getBluetoothLeAdvertiser();
            bleAdvertiseCallback = new BleAdvertiseCallback();
            advertiser.startAdvertising(advertiseSettings, advertiseData, bleAdvertiseCallback);
        }
    }

    /**
     * 停止广播
     */
    protected void stopAdvertising() {
        if (advertiser == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            advertiser.stopAdvertising(bleAdvertiseCallback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected class BleAdvertiseCallback extends AdvertiseCallback {

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i(TAG, "->onStartSuccess()");
            initServices();
            server.transmit(BluetoothServer.WHAT_ADVERTISE_SUCCEED, null, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, null, settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.i(TAG, "->onStartFailure() - errorCode = " + errorCode +" , value find in AdvertiseCallback#ADVERTISE_FAILED_DATA_TOO_LARGE");
            server.transmit(BluetoothServer.WHAT_ADVERTISE_FAILURE, null, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, errorCode, new IOException("start Advertise failure error = " + errorCode), null, null);
        }
    }

    /**
     * 初始化服务
     */
    protected void initServices() {
        BUUID buuid = server.getBUUID();
        String serviceUUID = buuid.value(BUUID.NAME_SERVICE);
        String readUUID = buuid.value(BUUID.NAME_CHARACTERISTIC_READ);
        String writeUUID = buuid.value(BUUID.NAME_CHARACTERISTIC_WRITE);
        String notifyUUID = buuid.value(BUUID.NAME_CHARACTERISTIC_NOTIFY);
        String descriptorUUID = buuid.value(BUUID.NAME_DESCRIPTOR);
        boolean isEnable;
        if (TextUtils.isEmpty(serviceUUID) || TextUtils.isEmpty(readUUID) || TextUtils.isEmpty(writeUUID) || TextUtils.isEmpty(notifyUUID) || TextUtils.isEmpty(descriptorUUID)) {
            isEnable = false;
        } else {
            isEnable = true;
        }
        Log.i(TAG, "->initServices() " + (isEnable ? "succeed" : "failed")
                + "\nUUID - Service    = " + serviceUUID
                + "\nUUID - Read       = " + readUUID
                + "\nUUID - Write      = " + writeUUID
                + "\nUUID - Notify     = " + notifyUUID
                + "\nUUID - Descriptor = " + descriptorUUID);
        if (!isEnable) {
            return;
        }
        bleGattServerCallback = new BleGattServerCallback(server, this);
        bluetoothGattServer = bluetooth.bluetoothManager().openGattServer(server.getContext(), bleGattServerCallback);
        bluetoothGattService = new BluetoothGattService(UUID.fromString(serviceUUID), BluetoothGattService.SERVICE_TYPE_PRIMARY);
        //读取特征
        reader = new BluetoothGattCharacteristic(UUID.fromString(readUUID), BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
        descriptor = new BluetoothGattDescriptor(UUID.fromString(descriptorUUID), BluetoothGattDescriptor.PERMISSION_WRITE | BluetoothGattDescriptor.PERMISSION_READ);
        reader.addDescriptor(descriptor);
        bluetoothGattService.addCharacteristic(reader);
        //写入特征
        writer = new BluetoothGattCharacteristic(UUID.fromString(writeUUID), BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, BluetoothGattCharacteristic.PERMISSION_WRITE);
        bluetoothGattService.addCharacteristic(writer);
        //通知特征
        notifier = new BluetoothGattCharacteristic(UUID.fromString(notifyUUID), BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_WRITE);
        bluetoothGattService.addCharacteristic(notifier);
        bluetoothGattServer.addService(bluetoothGattService);
    }

    /**
     * 写入数据
     *
     * @param data
     */
    public void write(byte[] data) {
        if (data == null) {
            return;
        }
        if (writer == null) {
            return;
        }
        writer.setValue(data);
        if (bleGattServerCallback == null) {
            return;
        }
        if (bleGattServerCallback.getBluetoothDevice() == null) {
            return;
        }
        if (bluetoothGattServer == null) {
            return;
        }
        bluetoothGattServer.notifyCharacteristicChanged(bleGattServerCallback.getBluetoothDevice(), writer, false);
        Log.i(TAG, "->write() - data=" + ByteBus.encodeHex(data));
    }

    public Bluetooth getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(Bluetooth bluetooth) {
        this.bluetooth = bluetooth;
    }

    public BluetoothServer getServer() {
        return server;
    }

    public void setServer(BluetoothServer server) {
        this.server = server;
    }

    public BluetoothLeAdvertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(BluetoothLeAdvertiser advertiser) {
        this.advertiser = advertiser;
    }

    public BluetoothGattServer getBluetoothGattServer() {
        return bluetoothGattServer;
    }

    public void setBluetoothGattServer(BluetoothGattServer bluetoothGattServer) {
        this.bluetoothGattServer = bluetoothGattServer;
    }

    public BluetoothGattService getBluetoothGattService() {
        return bluetoothGattService;
    }

    public void setBluetoothGattService(BluetoothGattService bluetoothGattService) {
        this.bluetoothGattService = bluetoothGattService;
    }

    public AdvertiseSettings getAdvertiseSettings() {
        return advertiseSettings;
    }

    public void setAdvertiseSettings(AdvertiseSettings advertiseSettings) {
        this.advertiseSettings = advertiseSettings;
    }

    public AdvertiseData getAdvertiseData() {
        return advertiseData;
    }

    public void setAdvertiseData(AdvertiseData advertiseData) {
        this.advertiseData = advertiseData;
    }

    public BleGattServerCallback getBleGattServerCallback() {
        return bleGattServerCallback;
    }

    public void setBleGattServerCallback(BleGattServerCallback bleGattServerCallback) {
        this.bleGattServerCallback = bleGattServerCallback;
    }

    public BluetoothGattCharacteristic getReader() {
        return reader;
    }

    public void setReader(BluetoothGattCharacteristic reader) {
        this.reader = reader;
    }

    public BluetoothGattCharacteristic getWriter() {
        return writer;
    }

    public void setWriter(BluetoothGattCharacteristic writer) {
        this.writer = writer;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(BluetoothGattDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public BleAdvertiseCallback getBleAdvertiseCallback() {
        return bleAdvertiseCallback;
    }

    public void setBleAdvertiseCallback(BleAdvertiseCallback bleAdvertiseCallback) {
        this.bleAdvertiseCallback = bleAdvertiseCallback;
    }

    public BluetoothDevice getBluetoothDevice() {
        if (bleGattServerCallback == null) {
            return null;
        }
        return bleGattServerCallback.getBluetoothDevice();
    }

    public HashMap<String, BluetoothDevice> getDevices() {
        if (bleGattServerCallback == null) {
            return null;
        }
        return bleGattServerCallback.getDevices();
    }

}
