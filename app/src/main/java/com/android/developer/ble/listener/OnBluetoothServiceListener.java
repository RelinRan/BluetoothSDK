package com.android.developer.ble.listener;

import java.io.IOException;

/**
 * Author: Relin
 * Describe:Socket监听
 * Date:2020/5/29 14:40
 */
public interface OnBluetoothServiceListener {

    /**
     * 服务写入
     *
     * @param data 数据
     */
    void onBluetoothServiceWrite(byte[] data);

    /**
     * 服务读取
     *
     * @param data 数据
     */
    void onBluetoothServiceRead(byte[] data);

    /**
     * 服务错误
     *
     * @param error 错误代码
     * @param e     异常
     */
    void onBluetoothServiceError(int error, IOException e);

}
