package com.android.developer.ble.listener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.AdvertiseSettings;

import java.io.IOException;

/**
 * Author: Relin
 * Describe:蓝牙客户端监听
 * Date:2020/5/28 17:08
 */
public interface OnBluetoothServerListener {

    /**
     * 开启广播成功
     *
     * @param mode             模式
     * @param settingsInEffect 设置
     */
    void onBluetoothServerStartAdvertiseSuccess(int mode, AdvertiseSettings settingsInEffect);

    /**
     * 开启广播失败
     *
     * @param mode  模式
     * @param error 错误代码
     */
    void onBluetoothServerStartAdvertiseFailure(int mode, int error);

    /**
     * 服务添加
     *
     * @param mode    模式
     * @param service 服务
     * @param status  状态
     */
    void onBluetoothServerServiceAdded(int mode, BluetoothGattService service, int status);

    /**
     * 连接成功
     *
     * @param mode   模式
     * @param device 设备
     */
    void onBluetoothServerConnected(int mode, BluetoothDevice device);

    /**
     * 断开连接
     *
     * @param mode   模式
     * @param device 设备
     */
    void onBluetoothServerDisconnect(int mode, BluetoothDevice device);

    /**
     * 读取数据
     *
     * @param mode     模式
     * @param device   设备
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothServerRead(int mode, BluetoothDevice device, int dataType, byte[] data);

    /**
     * 写入数据
     *
     * @param mode     模式
     * @param device   设备
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothServerWrite(int mode, BluetoothDevice device, int dataType, byte[] data);

    /**
     * 错误
     *
     * @param mode   模式
     * @param device 设备
     * @param code   错误代码
     * @param e      异常信息
     */
    void onBluetoothServerError(int mode, BluetoothDevice device, int code, IOException e);

}
