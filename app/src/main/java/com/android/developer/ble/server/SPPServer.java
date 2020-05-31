package com.android.developer.ble.server;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.android.developer.ble.Bluetooth;
import com.android.developer.ble.listener.OnBluetoothServiceListener;
import com.android.developer.ble.socket.SocketService;
import com.android.developer.ble.utils.BUUID;
import com.android.developer.ble.utils.ByteBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Relin
 * Describe:服务端服务
 * Date:2020/5/29 14:44
 */
public class SPPServer extends Thread implements OnBluetoothServiceListener {

    private String TAG = "ServerService";
    private boolean isClose;
    private BluetoothServer server;
    private SocketService service;
    private BluetoothDevice device;
    private BluetoothServerSocket serverSocket;
    private HashMap<String, BluetoothSocket> clients;
    private HashMap<String, BluetoothDevice> devices;
    private HashMap<String, SocketService> services;

    public SPPServer(BluetoothServer server) {
        clients = new HashMap<>();
        devices = new HashMap<>();
        services = new HashMap<>();
        this.server = server;
    }

    @Override
    public void run() {
        super.run();
        try {
            String uuid = server.getBUUID().value(BUUID.NAME_SERVICE);
            serverSocket = Bluetooth.with(server.getContext()).bluetoothAdapter().listenUsingRfcommWithServiceRecord(server.getName(), UUID.fromString(uuid));
            if (serverSocket != null) {
                Log.i(TAG, "->start server succeed , wait device connect ...");
                while (true) {
                    if (isClose) {
                        break;
                    }
                    BluetoothSocket socket = serverSocket.accept(server.getTimeOut());
                    device = socket.getRemoteDevice();
                    if (!isConnect(device)) {
                        String name = device.getName();
                        String address = device.getAddress();
                        service = new SocketService(socket);
                        service.setOnBluetoothServiceListener(this);
                        service.start();
                        devices.put(address, device);
                        clients.put(address, socket);
                        services.put(address, service);
                        Log.i(TAG, "->new device connect server -  name = " + name + " , address = " + address);
                        server.transmit(BluetoothServer.WHAT_CONNECTED, device, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, null,null);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            server.transmit(BluetoothServer.WHAT_ERROR, device, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, e, null,null);
            close();
        }
    }

    /**
     * 当前设备写入数据（最新连接的设备）
     *
     * @param data
     */
    public void write(byte[] data) {
        if (service == null) {
            return;
        }
        service.write(data);
    }

    /**
     * 写入数据
     *
     * @param device 蓝牙设备
     * @param data   数据
     */
    public void write(BluetoothDevice device, byte[] data) {
        if (device == null) {
            return;
        }
        this.device = device;
        service = services.get(device.getAddress());
        if (service == null) {
            return;
        }
        service.write(data);
    }

    /**
     * 关闭服务
     */
    public void close() {
        isClose = true;
        if (device == null) {
            return;
        }
        remove(device);
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 是否已连接
     *
     * @param device
     * @return
     */
    public boolean isConnect(BluetoothDevice device) {
        String deviceKey = device.getAddress();
        for (String key : clients.keySet()) {
            if (key.equals(deviceKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移除设备
     *
     * @param bluetoothDevice
     */
    public void remove(BluetoothDevice bluetoothDevice) {
        String name = bluetoothDevice.getName();
        String address = bluetoothDevice.getAddress();
        String deviceKey = device.getAddress();
        Log.i(TAG, "->remove() - name = " + name + " , address = " + address);
        for (String key : clients.keySet()) {
            if (key.equals(deviceKey)) {
                try {
                    clients.get(key).close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    clients.remove(key);
                    Log.i(TAG, "->remove() client removed");
                }
            }
        }
        for (String key : services.keySet()) {
            if (key.equals(deviceKey)) {
                services.get(key).close();
                services.remove(key);
                Log.i(TAG, "->remove() service removed");
            }
        }
        for (String key : devices.keySet()) {
            if (key.equals(deviceKey)) {
                devices.remove(key);
                Log.i(TAG, "->remove() device removed");
            }
        }
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public HashMap<String, BluetoothDevice> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, BluetoothDevice> devices) {
        this.devices = devices;
    }

    public HashMap<String, BluetoothSocket> getClients() {
        return clients;
    }

    public void setClients(HashMap<String, BluetoothSocket> clients) {
        this.clients = clients;
    }

    @Override
    public void onBluetoothServiceWrite(byte[] data) {
        Log.i(TAG, "->onBluetoothServiceWrite() - data = " + ByteBus.encodeHex(data));
        server.transmit(BluetoothServer.WHAT_WRITE, device, BluetoothServer.DATA_SPP_WRITE, data, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, null, null);
    }

    @Override
    public void onBluetoothServiceRead(byte[] data) {
        Log.i(TAG, "->onBluetoothServiceRead() - data = " + ByteBus.encodeHex(data));
        server.transmit(BluetoothServer.WHAT_READ, device, BluetoothServer.DATA_SPP_READ, data, BluetoothServer.STATUS_NONE, BluetoothServer.ERROR_NONE, null, null, null);
    }

    @Override
    public void onBluetoothServiceError(int error, IOException e) {
        Log.i(TAG, "->onBluetoothServiceError() - error = " + error + " , IOException = " + e.toString());
        server.transmit(BluetoothServer.WHAT_ERROR, device, BluetoothServer.DATA_NONE, null, BluetoothServer.STATUS_NONE, error, e, null, null);
        //处理已掉线设备
        if (error == BluetoothServer.ERROR_WRITE || error == BluetoothServer.ERROR_READ) {
            remove(device);
        }
    }

    public BluetoothServer getServer() {
        return server;
    }

    public void setServer(BluetoothServer server) {
        this.server = server;
    }

    public SocketService getService() {
        return service;
    }

    public void setService(SocketService service) {
        this.service = service;
    }

    public BluetoothServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(BluetoothServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public HashMap<String, SocketService> getServices() {
        return services;
    }

    public void setServices(HashMap<String, SocketService> services) {
        this.services = services;
    }

}