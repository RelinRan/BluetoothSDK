package com.android.developer.ble.client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.developer.ble.Bluetooth;
import com.android.developer.ble.listener.OnBluetoothClientListener;
import com.android.developer.ble.utils.BUUID;

import java.io.IOException;

/**
 * Author= Relin
 * Describe=蓝牙客户端
 * Date=2020/5/28 15=28
 */
public class BluetoothClient {

    public final String TAG = "BluetoothClient";

    public static final int MODE_SPP = Bluetooth.MODE_SPP;
    public static final int MODE_BLE = Bluetooth.MODE_BLE;

    public static final int WHAT_CONNECTED = 0;
    public static final int WHAT_DISCONNECT = 1;
    public static final int WHAT_ERROR = 2;
    public static final int WHAT_READ = 3;
    public static final int WHAT_WRITE = 4;
    public static final int WHAT_SERVICES_DISCOVERED = 5;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_CONNECT = -100;
    public static final int ERROR_READ = -101;
    public static final int ERROR_WRITE = -102;

    public static final int DATA_NONE = 0;
    public static final int DATA_CHARACTERISTIC_READ = 1;
    public static final int DATA_CHARACTERISTIC_WRITE = 2;
    public static final int DATA_DESCRIPTOR_READ = 3;
    public static final int DATA_DESCRIPTOR_WRITE = 4;
    public static final int DATA_SPP_READ = 5;
    public static final int DATA_SPP_WRITE = 6;
    public static final int DATA_CHARACTERISTIC_CHANGED = 7;

    public static final int STATUS_NONE = 0;

    private Context context;
    private int mode;
    private boolean secure;
    private boolean autoConnect;
    private BUUID buuid;
    private String address;
    private SPPClient spp;
    private BluetoothGatt gatt;
    private BluetoothDevice device;
    private ClientHandler handler;
    private BleGattCallBack bleGattCallBack;
    private OnBluetoothClientListener listener;

    /**
     * 蓝牙构造函数
     *
     * @param mode    模式
     * @param address 蓝牙地址
     * @param buuid   蓝牙UUID
     */
    public BluetoothClient(Context context, int mode, String address, BUUID buuid) {
        handler = new ClientHandler();
        this.context = context;
        this.mode = mode;
        this.address = address;
        this.buuid = buuid;
    }

    /**
     * 连接设备
     */
    public void connect() {
        device = Bluetooth.with(context).bluetoothAdapter().getRemoteDevice(address);
        if (device == null) {
            return;
        }
        if (mode == MODE_SPP) {
            if (spp != null) {
                spp.close();
                spp = null;
            }
            spp = new SPPClient(this);
            spp.start();
        }
        if (mode == MODE_BLE) {
            if (gatt != null) {
                gatt.disconnect();
                gatt = null;
            }
            bleGattCallBack = new BleGattCallBack(this);
            gatt = device.connectGatt(context, autoConnect, bleGattCallBack);
            gatt.connect();
        }
    }

    /**
     * 写入数据
     *
     * @param data
     */
    public void write(byte[] data) {
        if (mode == MODE_SPP) {
            if (spp != null) {
                spp.write(data);
            }
            transmit(WHAT_WRITE, DATA_SPP_WRITE, data, STATUS_NONE, ERROR_NONE, null);
        }
        if (mode == MODE_BLE) {
            if (bleGattCallBack == null) {
                return;
            }
            BluetoothGattCharacteristic writeCharacteristic = bleGattCallBack.getWriter();
            if (writeCharacteristic != null) {
                writeCharacteristic.setValue(data);
                boolean isWrite = gatt.writeCharacteristic(writeCharacteristic);
                if (isWrite) {
                    transmit(WHAT_WRITE, DATA_CHARACTERISTIC_WRITE, data, STATUS_NONE, ERROR_NONE, null);
                } else {
                    Log.i(TAG, "->write ble data failed.");
                }
            } else {
                Log.i(TAG, "->write ble data failed , because writeCharacteristic is null.");
            }
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (mode == MODE_SPP) {
            if (spp != null) {
                spp.close();
            }
        }
        if (mode == MODE_BLE) {
            if (gatt != null) {
                gatt.disconnect();
            }
        }
    }

    /**
     * 传递消息
     *
     * @param what     消息标志
     * @param dataType 数据类型
     * @param data     数据
     * @param status   状态
     * @param error    错误
     * @param e        异常
     */
    public void transmit(int what, int dataType, byte[] data, int status, int error, IOException e) {
        if (handler == null) {
            return;
        }
        ClientBody body = new ClientBody();
        body.setMode(mode);
        body.setBluetoothDevice(device);
        body.setBluetoothGatt(gatt);
        body.setDataType(dataType);
        body.setData(data);
        body.setStatus(status);
        body.setError(error);
        body.setException(e);
        body.setOnBluetoothClientListener(listener);
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = body;
        handler.sendMessage(message);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public BUUID getBUUID() {
        return buuid;
    }

    public void setBUUID(BUUID buuid) {
        this.buuid = buuid;
    }

    public BleGattCallBack getBleGattCallBack() {
        return bleGattCallBack;
    }

    public void setBleGattCallBack(BleGattCallBack bleGattCallBack) {
        this.bleGattCallBack = bleGattCallBack;
    }

    public OnBluetoothClientListener getListener() {
        return listener;
    }

    public void setListener(OnBluetoothClientListener listener) {
        this.listener = listener;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SPPClient getSpp() {
        return spp;
    }

    public void setSpp(SPPClient spp) {
        this.spp = spp;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public void setHandler(ClientHandler handler) {
        this.handler = handler;
    }

    public OnBluetoothClientListener getOnBluetoothClientListener() {
        return listener;
    }

    public void setOnBluetoothClientListener(OnBluetoothClientListener listener) {
        this.listener = listener;
    }

}
