package com.android.developer.ble.server;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.AdvertiseSettings;

import com.android.developer.ble.listener.OnBluetoothServerListener;

import java.io.IOException;

/**
 * Author: Relin
 * Describe: 服务端实体类
 * Date:2020/5/29 22:18
 */
public class ServerBody {

    private int dataType;
    private byte[] data;
    private int error;
    private int mode;
    private int status;
    private IOException exception;
    private BluetoothGattService service;
    private BluetoothDevice bluetoothDevice;
    private AdvertiseSettings advertiseSettings;
    private OnBluetoothServerListener onBluetoothServerListener;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public IOException getException() {
        return exception;
    }

    public void setException(IOException exception) {
        this.exception = exception;
    }

    public OnBluetoothServerListener getOnBluetoothServerListener() {
        return onBluetoothServerListener;
    }

    public void setOnBluetoothServerListener(OnBluetoothServerListener onBluetoothServerListener) {
        this.onBluetoothServerListener = onBluetoothServerListener;
    }

    public BluetoothGattService getService() {
        return service;
    }

    public void setService(BluetoothGattService service) {
        this.service = service;
    }

    public AdvertiseSettings getAdvertiseSettings() {
        return advertiseSettings;
    }

    public void setAdvertiseSettings(AdvertiseSettings advertiseSettings) {
        this.advertiseSettings = advertiseSettings;
    }
}
