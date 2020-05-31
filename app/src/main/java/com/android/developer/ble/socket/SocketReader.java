package com.android.developer.ble.socket;

import android.util.Log;

import com.android.developer.ble.server.BluetoothServer;
import com.android.developer.ble.listener.OnBluetoothServiceListener;
import com.android.developer.ble.utils.ByteBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: Relin
 * Describe:Socket读取
 * Date:2020/5/29 14:04
 */
public class SocketReader extends Thread {

    private String TAG = "SocketReader";
    /**
     * 读取通道
     */
    private InputStream is;
    /**
     * 是否关闭
     */
    private boolean isClose;
    /**
     * 读取数据大小
     */
    private int bufferSize = 128;
    /**
     * 服务监听
     */
    private OnBluetoothServiceListener listener;

    public SocketReader(InputStream is) {
        this.is = is;
    }

    public void setOnBluetoothServiceListener(OnBluetoothServiceListener listener) {
        this.listener = listener;
    }

    public OnBluetoothServiceListener getOnBluetoothServiceListener() {
        return listener;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isClose() {
        return isClose;
    }

    public void close() {
        isClose = true;
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            if (isClose) {
                break;
            }
            byte[] buffer = new byte[bufferSize];
            try {
                int length = is.read(buffer);
                if (length > 0) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream(length);
                    output.write(buffer, 0, length);
                    output.flush();
                    byte[] data = output.toByteArray();
                    if (data != null && data.length != 0) {
                        if (listener != null) {
                            listener.onBluetoothServiceRead(data);
                        } else {
                            Log.i(TAG, "->read data=" + ByteBus.encodeHex(data));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onBluetoothServiceError(BluetoothServer.ERROR_READ, e);
                }
                isClose = true;
            }
        }
    }

}
