package com.android.developer.ble.socket;

import android.bluetooth.BluetoothSocket;

import com.android.developer.ble.listener.OnBluetoothServiceListener;
import com.android.developer.ble.server.BluetoothServer;

import java.io.IOException;

/**
 * Author: Relin
 * Describe:Socket 服务
 * Date:2020/5/29 13:44
 */
public class SocketService extends Thread {

    /**
     * Socket读取对象
     */
    private SocketReader reader;
    /**
     * Socket写入对象
     */
    private SocketWriter writer;
    /**
     * Socket连接对象
     */
    private BluetoothSocket socket;
    /**
     * 服务监听
     */
    private OnBluetoothServiceListener onBluetoothServiceListener;

    public SocketService(BluetoothSocket socket) {
        this.socket = socket;
    }

    public void setOnBluetoothServiceListener(OnBluetoothServiceListener onBluetoothServiceListener) {
        this.onBluetoothServiceListener = onBluetoothServiceListener;
    }

    public OnBluetoothServiceListener getOnBluetoothServiceListener() {
        return onBluetoothServiceListener;
    }

    /**
     * 写入数据
     *
     * @param data
     */
    public void write(byte[] data) {
        if (writer != null) {
            writer.write(data);
        }
    }

    /**
     * 关闭服务
     */
    public void close() {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public void run() {
        super.run();
        if (socket != null) {
            try {
                reader = new SocketReader(socket.getInputStream());
                reader.setOnBluetoothServiceListener(onBluetoothServiceListener);
                reader.start();
                writer = new SocketWriter(socket.getOutputStream());
                writer.setOnBluetoothServiceListener(onBluetoothServiceListener);
                writer.start();
            } catch (IOException e) {
                e.printStackTrace();
                if (onBluetoothServiceListener != null) {
                    onBluetoothServiceListener.onBluetoothServiceError(BluetoothServer.ERROR_WRITE, e);
                }
            }
        }
    }

}
