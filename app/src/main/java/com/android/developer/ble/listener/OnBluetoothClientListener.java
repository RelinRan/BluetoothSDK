package com.android.developer.ble.listener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import java.io.IOException;

/**
 * Author: Relin
 * Describe:蓝牙客户端监听
 * Date:2020/5/28 17:08
 */
public interface OnBluetoothClientListener {

    /**
     * 连接成功
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     */
    void onBluetoothClientConnected(int mode, BluetoothDevice device, BluetoothGatt gatt);

    /**
     * 断开连接
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     */
    void onBluetoothClientDisconnect(int mode, BluetoothDevice device, BluetoothGatt gatt);

    /**
     * 错误
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     * @param code   错误代码
     * @param e      异常信息
     */
    void onBluetoothClientError(int mode, BluetoothDevice device, BluetoothGatt gatt, int code, IOException e);

    /**
     * 读取数据
     *
     * @param mode     模式
     * @param device   设备
     * @param gatt     Ble对象
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothClientRead(int mode, BluetoothDevice device, BluetoothGatt gatt, int dataType, byte[] data);

    /**
     * 写入数据
     *
     * @param mode     模式
     * @param device   设备
     * @param gatt     Ble对象
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothClientWrite(int mode, BluetoothDevice device, BluetoothGatt gatt, int dataType, byte[] data);

    /**
     * 新服务发现
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     * @param status 状态
     */
    void onBluetoothClientServicesDiscovered(int mode, BluetoothDevice device, BluetoothGatt gatt, int status);

}
