package com.android.developer.ble.server;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.developer.ble.Bluetooth;
import com.android.developer.ble.listener.OnBluetoothServerListener;
import com.android.developer.ble.utils.BUUID;

import java.io.IOException;

/**
 * Author: Relin
 * Describe:蓝牙服务端
 * Date:2020/5/29 10:09
 */
public class BluetoothServer {

    public final static String TAG = "BluetoothServer";

    public static final int MODE_SPP = Bluetooth.MODE_SPP;
    public static final int MODE_BLE = Bluetooth.MODE_BLE;

    public static final int STATUS_NONE = 0;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_CONNECT = -100;
    public static final int ERROR_READ = -101;
    public static final int ERROR_WRITE = -102;

    public static final int WHAT_CONNECTED = 0;
    public static final int WHAT_DISCONNECT = 1;
    public static final int WHAT_ERROR = 2;
    public static final int WHAT_READ = 3;
    public static final int WHAT_WRITE = 4;
    public static final int WHAT_SERVICE_ADDED = 5;
    public static final int WHAT_ADVERTISE_SUCCEED = 6;
    public static final int WHAT_ADVERTISE_FAILURE = 7;

    public static final int DATA_NONE = 0;
    public static final int DATA_CHARACTERISTIC_READ = 1;
    public static final int DATA_CHARACTERISTIC_WRITE = 2;
    public static final int DATA_DESCRIPTOR_READ = 3;
    public static final int DATA_DESCRIPTOR_WRITE = 4;
    public static final int DATA_SPP_READ = 5;
    public static final int DATA_SPP_WRITE = 6;

    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 服务名称
     */
    private String name;
    /**
     * 蓝牙UUID
     */
    private BUUID buuid;
    /**
     * 超时时间
     */
    private int timeOut = -1;
    /**
     * 服务
     */
    private SPPServer sppServer;
    /**
     * 模型
     */
    private int mode;
    /**
     * UI Handler
     */
    private ServerHandler serverHandler;
    /**
     * 服务端监听
     */
    private OnBluetoothServerListener onBluetoothServerListener;
    /**
     * BLE服务端
     */
    private BleServer bleServer;
    /**
     * BLE -制造商ID（广播使用）
     */
    private int bleManufacturerId;
    /**
     * BLE - 服务数据（广播使用）
     */
    private byte[] bleServiceData;
    /**
     * BLE - 制造商数据（广播使用）
     */
    private byte[] bleManufacturerData;

    /**
     * 服务端
     *
     * @param context 上下文对象
     * @param mode    名称
     * @param name    名称
     * @param buuid   蓝牙UUID
     */
    public BluetoothServer(Context context, int mode, String name, BUUID buuid) {
        this.context = context;
        this.mode = mode;
        this.name = name;
        this.buuid = buuid;
        serverHandler = new ServerHandler();
    }

    /**
     * 写入数据
     *
     * @param data 数据
     */
    public void write(byte[] data) {
        if (mode == MODE_SPP) {
            if (sppServer != null) {
                sppServer.write(data);
            }
        }
        if (mode == MODE_BLE) {
            if (bleServer != null) {
                bleServer.write(data);
            }
        }
    }

    /**
     * 开启服务
     */
    public void start() {
        if (mode == MODE_SPP) {
            if (sppServer != null) {
                sppServer.close();
                sppServer = null;
            }
            sppServer = new SPPServer(this);
            sppServer.start();
        }
        if (mode == MODE_BLE) {
            if (bleServer != null) {
                bleServer.close();
                bleServer = null;
            }
            bleServer = new BleServer(this);
            bleServer.start();
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        if (mode == MODE_SPP) {
            if (sppServer != null) {
                sppServer.close();
            }
        }
        if (mode == MODE_BLE) {
            if (sppServer != null) {
                sppServer.close();
            }
        }
    }

    /**
     * 传递数据
     *
     * @param what             类型
     * @param device           设备
     * @param dataType         数据类型
     * @param data             数据
     * @param status           状态
     * @param error            错误
     * @param e                异常
     * @param service          服务
     * @param settingsInEffect 广播设置
     */
    public void transmit(int what, BluetoothDevice device, int dataType, byte[] data, int status, int error, IOException e, BluetoothGattService service, AdvertiseSettings settingsInEffect) {
        if (serverHandler == null) {
            return;
        }
        ServerBody body = new ServerBody();
        body.setMode(mode);
        body.setBluetoothDevice(device);
        body.setDataType(dataType);
        body.setData(data);
        body.setStatus(status);
        body.setError(error);
        body.setException(e);
        body.setService(service);
        body.setAdvertiseSettings(settingsInEffect);
        body.setOnBluetoothServerListener(onBluetoothServerListener);
        Message message = serverHandler.obtainMessage();
        message.what = what;
        message.obj = body;
        serverHandler.sendMessage(message);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BUUID getBUUID() {
        return buuid;
    }

    public void setBUUID(BUUID buuid) {
        this.buuid = buuid;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public SPPServer getSppServer() {
        return sppServer;
    }

    public void setSppServer(SPPServer sppServer) {
        this.sppServer = sppServer;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public OnBluetoothServerListener getOnBluetoothServerListener() {
        return onBluetoothServerListener;
    }

    public void setOnBluetoothServerListener(OnBluetoothServerListener onBluetoothServerListener) {
        this.onBluetoothServerListener = onBluetoothServerListener;
    }

    public BleServer getBleServer() {
        return bleServer;
    }

    public void setBleServer(BleServer bleServer) {
        this.bleServer = bleServer;
    }

    public int getBleManufacturerId() {
        return bleManufacturerId;
    }

    public void setBleManufacturerId(int bleManufacturerId) {
        this.bleManufacturerId = bleManufacturerId;
    }

    public byte[] getBleServiceData() {
        return bleServiceData;
    }

    public void setBleServiceData(byte[] bleServiceData) {
        this.bleServiceData = bleServiceData;
    }

    public byte[] getBleManufacturerData() {
        return bleManufacturerData;
    }

    public void setBleManufacturerData(byte[] bleManufacturerData) {
        this.bleManufacturerData = bleManufacturerData;
    }
}
