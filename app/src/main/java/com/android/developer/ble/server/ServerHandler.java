package com.android.developer.ble.server;

import android.os.Handler;
import android.os.Message;

import com.android.developer.ble.client.BluetoothClient;
import com.android.developer.ble.client.ClientBody;
import com.android.developer.ble.listener.OnBluetoothClientListener;
import com.android.developer.ble.listener.OnBluetoothServerListener;


/**
 * Author: Relin
 * Describe:UI处理Handler
 * Date:2020/5/29 13:14
 */
public class ServerHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ServerBody body = (ServerBody) msg.obj;
        OnBluetoothServerListener listener = body.getOnBluetoothServerListener();
        if (msg.what == BluetoothServer.WHAT_ADVERTISE_SUCCEED) {
            if (listener != null) {
                listener.onBluetoothServerStartAdvertiseSuccess(body.getMode(), body.getAdvertiseSettings());
            }
        }
        if (msg.what == BluetoothServer.WHAT_ADVERTISE_FAILURE) {
            if (listener != null) {
                listener.onBluetoothServerStartAdvertiseFailure(body.getMode(), body.getError());
            }
        }
        if (msg.what == BluetoothServer.WHAT_CONNECTED) {
            if (listener != null) {
                listener.onBluetoothServerConnected(body.getMode(), body.getBluetoothDevice());
            }
        }
        if (msg.what == BluetoothServer.WHAT_DISCONNECT) {
            if (listener != null) {
                listener.onBluetoothServerDisconnect(body.getMode(), body.getBluetoothDevice());
            }
        }
        if (msg.what == BluetoothServer.WHAT_ERROR) {
            if (listener != null) {
                listener.onBluetoothServerError(body.getMode(), body.getBluetoothDevice(), body.getError(), body.getException());
            }
        }
        if (msg.what == BluetoothServer.WHAT_READ) {
            if (listener != null) {
                listener.onBluetoothServerRead(body.getMode(), body.getBluetoothDevice(), body.getDataType(), body.getData());
            }
        }
        if (msg.what == BluetoothServer.WHAT_WRITE) {
            if (listener != null) {
                listener.onBluetoothServerWrite(body.getMode(), body.getBluetoothDevice(), body.getDataType(), body.getData());
            }
        }
        if (msg.what == BluetoothServer.WHAT_SERVICE_ADDED) {
            if (listener != null) {
                listener.onBluetoothServerServiceAdded(body.getMode(), body.getService(), body.getStatus());
            }
        }
    }
}