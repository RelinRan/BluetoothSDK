package com.android.developer.ble.client;

import android.os.Handler;
import android.os.Message;

import com.android.developer.ble.listener.OnBluetoothClientListener;


/**
 * Author: Relin
 * Describe:UI处理Handler
 * Date:2020/5/29 13:14
 */
public class ClientHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ClientBody body = (ClientBody) msg.obj;
        OnBluetoothClientListener listener = body.getOnBluetoothClientListener();
        if (msg.what == BluetoothClient.WHAT_CONNECTED) {
            if (listener != null) {
                listener.onBluetoothClientConnected(body.getMode(), body.getBluetoothDevice(), body.getBluetoothGatt());
            }
        }
        if (msg.what == BluetoothClient.WHAT_DISCONNECT) {
            if (listener != null) {
                listener.onBluetoothClientDisconnect(body.getMode(), body.getBluetoothDevice(), body.getBluetoothGatt());
            }
        }
        if (msg.what == BluetoothClient.WHAT_ERROR) {
            if (listener != null) {
                listener.onBluetoothClientError(body.getMode(), body.getBluetoothDevice(), body.getBluetoothGatt(), body.getError(), body.getException());
            }
        }
        if (msg.what == BluetoothClient.WHAT_READ) {
            if (listener != null) {
                listener.onBluetoothClientRead(body.getMode(), body.getBluetoothDevice(), body.getBluetoothGatt(), body.getDataType(), body.getData());
            }
        }
        if (msg.what == BluetoothClient.WHAT_WRITE) {
            if (listener != null) {
                listener.onBluetoothClientWrite(body.getMode(), body.getBluetoothDevice(), body.getBluetoothGatt(), body.getDataType(), body.getData());
            }
        }
        if (msg.what == BluetoothClient.WHAT_SERVICES_DISCOVERED) {
            if (listener != null) {
                listener.onBluetoothClientServicesDiscovered(body.getMode(), body.getBluetoothDevice(), body.getBluetoothGatt(), body.getStatus());
            }
        }
    }
}