package com.android.developer.ble.socket;

import android.util.Log;

import com.android.developer.ble.server.BluetoothServer;
import com.android.developer.ble.listener.OnBluetoothServiceListener;
import com.android.developer.ble.utils.ByteBus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

/**
 * Author: Relin
 * Describe:Socket写入
 * Date:2020/5/29 14:03
 */
public class SocketWriter extends Thread {

    private String TAG = "SocketWriter";
    /**
     * 写入通道
     */
    private OutputStream os;
    /**
     * 是否关闭
     */
    private boolean isClose;
    /**
     * 数据
     */
    private Stack<byte[]> stack;
    /**
     * 服务监听
     */
    private OnBluetoothServiceListener listener;


    public SocketWriter(OutputStream os) {
        stack = new Stack<>();
        this.os = os;
    }

    public void setOnBluetoothServiceListener(OnBluetoothServiceListener listener) {
        this.listener = listener;
    }

    public OnBluetoothServiceListener getOnBluetoothServiceListener() {
        return listener;
    }

    public boolean isClose() {
        return isClose;
    }

    public void close() {
        isClose = true;
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] data) {
        stack.push(data);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            if (isClose) {
                break;
            }
            if (stack.size() > 0) {
                try {
                    byte[] data = stack.pop();
                    os.write(data);
                    os.flush();
                    if (listener != null) {
                        listener.onBluetoothServiceWrite(data);
                    } else {
                        Log.i(TAG, "->write data=" + ByteBus.encodeHex(data));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onBluetoothServiceError(BluetoothServer.ERROR_WRITE, e);
                    }
                    isClose = true;
                }
            }
        }
    }

}
