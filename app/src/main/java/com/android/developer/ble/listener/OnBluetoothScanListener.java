package com.android.developer.ble.listener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

import com.android.developer.ble.Bluetooth;

import java.util.List;

/**
 * Author: Relin
 * Describe:蓝牙扫描监听
 * Date:2020/5/29 01:14
 */
public interface OnBluetoothScanListener {

    /**
     * 发现新设备（SPP）
     *
     * @param device
     */
    void onBluetoothFound(BluetoothDevice device);

    /**
     * 设备扫描完毕（SPP）
     */
    void onBluetoothDiscoveryFinished();

    /**
     * 设备绑定状态（SPP）
     *
     * @param state {@link Bluetooth#BOND_BONDED}
     */
    void onBluetoothBondStateChanged(int state);

    /**
     * 设备连接成功（SPP）
     *
     * @param device
     */
    void onBluetoothConnected(BluetoothDevice device);

    /**
     * 设备断开连接
     *
     * @param device
     */
    void onBluetoothDisconnected(BluetoothDevice device);

    /**
     * 低功耗设备扫描结果
     *
     * @param callbackType {@link Bluetooth#CALLBACK_TYPE_ALL_MATCHES}
     * @param result
     */
    void onBluetoothLeScanResult(int callbackType, ScanResult result);

    /**
     * 低功耗批量处理扫描结果
     *
     * @param results
     */
    void onBluetoothLeBatchScanResults(List<ScanResult> results);

    /**
     * 低功耗扫描失败
     *
     * @param error
     */
    void onBluetoothScanFailed(int error);

}
