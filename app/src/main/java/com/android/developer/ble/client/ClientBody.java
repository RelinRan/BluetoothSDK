package com.android.developer.ble.client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import com.android.developer.ble.listener.OnBluetoothClientListener;

import java.io.IOException;
import java.io.Serializable;

/**
 * Author: Relin
 * Describe:客户端实体
 * Date:2020/5/28 23:14
 */
public class ClientBody implements Serializable {

    /**
     * 数据类型
     */
    private int dataType;
    /**
     * 数据
     */
    private byte[] data;
    /**
     * 错误代码
     */
    private int error;
    /**
     * 模式
     */
    private int mode;
    /**
     * 状态
     */
    private int status;
    /**
     * 异常
     */
    private IOException exception;
    /**
     * 设备
     */
    private BluetoothDevice bluetoothDevice;
    /**
     * 蓝牙操作对象
     */
    private BluetoothGatt bluetoothGatt;
    /**
     * 客户端监听
     */
    private OnBluetoothClientListener onBluetoothClientListener;

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

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public OnBluetoothClientListener getOnBluetoothClientListener() {
        return onBluetoothClientListener;
    }

    public void setOnBluetoothClientListener(OnBluetoothClientListener onBluetoothClientListener) {
        this.onBluetoothClientListener = onBluetoothClientListener;
    }

    public IOException getException() {
        return exception;
    }

    public void setException(IOException exception) {
        this.exception = exception;
    }
}