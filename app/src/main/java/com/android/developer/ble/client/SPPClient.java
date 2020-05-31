package com.android.developer.ble.client;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.android.developer.ble.listener.OnBluetoothServiceListener;
import com.android.developer.ble.socket.SocketService;
import com.android.developer.ble.utils.BUUID;
import com.android.developer.ble.utils.ByteBus;

import java.io.IOException;
import java.util.UUID;

/**
 * Author: Relin
 * Describe:串口客户端
 * Date:2020/5/29 14:24
 */
public class SPPClient extends Thread implements OnBluetoothServiceListener {

    private String TAG = "SPPClient";
    private BluetoothSocket socket;
    private SocketService service;
    private BluetoothClient client;

    public SPPClient(BluetoothClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        super.run();
        try {
            String uuid = client.getBUUID().value(BUUID.NAME_SERVICE);
            if (client.isSecure()) {
                socket = client.getDevice().createRfcommSocketToServiceRecord(UUID.fromString(uuid));
            } else {
                socket = client.getDevice().createInsecureRfcommSocketToServiceRecord(UUID.fromString(uuid));
            }
            socket.connect();
            Log.i(TAG, "->connect succeed");
            client.transmit(BluetoothClient.WHAT_CONNECTED, BluetoothClient.DATA_NONE, null, BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
            service = new SocketService(socket);
            service.setOnBluetoothServiceListener(this);
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "->connect failed " + e.toString());
            client.transmit(BluetoothClient.WHAT_ERROR, BluetoothClient.DATA_NONE, null, BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_CONNECT, null);
        }
    }

    public void write(byte[] data) {
        if (service != null) {
            service.write(data);
        }
    }

    public void close() {
        if (service != null) {
            service.close();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBluetoothServiceWrite(byte[] data) {
        Log.i(TAG, "->onBluetoothServiceWrite() - write data = " + ByteBus.encodeHex(data));
        client.transmit(BluetoothClient.WHAT_WRITE, BluetoothClient.DATA_SPP_WRITE, data, BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onBluetoothServiceRead(byte[] data) {
        Log.i(TAG, "->onBluetoothServiceRead() - read data = " + ByteBus.encodeHex(data));
        client.transmit(BluetoothClient.WHAT_READ, BluetoothClient.DATA_SPP_READ, data, BluetoothClient.STATUS_NONE, BluetoothClient.ERROR_NONE, null);
    }

    @Override
    public void onBluetoothServiceError(int error, IOException e) {
        Log.i(TAG, "->onBluetoothServiceError() - error = " + error + " , IOException = " + e.toString());
        client.transmit(BluetoothClient.WHAT_ERROR, BluetoothClient.DATA_NONE, null, BluetoothClient.STATUS_NONE, error, e);
    }

}