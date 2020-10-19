package com.android.developer.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.developer.ble.listener.OnBluetoothScanListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 蓝牙操作对象
 */
public class Bluetooth {

    /**
     * 日志标识
     */
    public static final String TAG = "Bluetooth";
    /**
     * 设置蓝牙可用
     */
    public static final int REQUEST_ENABLE_BT = 520;
    /**
     * 模式 - 传统蓝牙模式
     */
    public static final int MODE_SPP = 0;
    /**
     * 模式 - 低功耗蓝牙模式
     */
    public static final int MODE_BLE = 1;
    /**
     * 绑定状态 - 初始状态
     */
    public static final int BOND_NONE = BluetoothDevice.BOND_NONE;
    /**
     * 绑定状态 - 正在绑定
     */
    public static final int BOND_BONDING = BluetoothDevice.BOND_BONDING;
    /**
     * 绑定状态 - 绑定成功
     */
    public static final int BOND_BONDED = BluetoothDevice.BOND_BONDED;
    /**
     * 为每个匹配过滤条件的蓝牙广告触发回调,如果没有过滤器是活动的，所有的广告包被报告。
     */
    public static final int CALLBACK_TYPE_ALL_MATCHES = ScanSettings.CALLBACK_TYPE_ALL_MATCHES;
    /**
     * 只对收到的第一个匹配的广告包触发结果回调过滤标准。
     */
    public static final int CALLBACK_TYPE_FIRST_MATCH = ScanSettings.CALLBACK_TYPE_FIRST_MATCH;
    /**
     * 收到一个回调当广告不再从一个已收到的设备收到,以前由第一个匹配回调报告。
     */
    public static final int CALLBACK_TYPE_MATCH_LOST = ScanSettings.CALLBACK_TYPE_MATCH_LOST;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 蓝牙对象
     */
    private static Bluetooth bluetooth;
    /**
     * 蓝牙适配器
     */
    private static BluetoothAdapter bluetoothAdapter;
    /**
     * 管理器
     */
    private BluetoothManager bluetoothManager;
    /**
     * SPP接收器
     */
    private SPPReceiver sppReceiver;

    private Bluetooth() {

    }

    /**
     * 构造函数
     *
     * @param context
     */
    private Bluetooth(Context context) {
        this.context = context;
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e(TAG, "->The device does not support bluetooth.");
        }
    }

    /**
     * 获取单例模式
     *
     * @param context 上下文对象
     * @return
     */
    public static Bluetooth with(Context context) {
        if (bluetooth == null) {
            synchronized (Bluetooth.class) {
                if (bluetooth == null) {
                    bluetooth = new Bluetooth(context);
                }
            }
        }
        bluetooth.setContext(context);
        return bluetooth;
    }

    /**
     * 获取蓝牙管理器
     *
     * @return
     */
    public BluetoothManager bluetoothManager() {
        return bluetoothManager;
    }

    /**
     * 设置上下文对象
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * 蓝牙适配器
     *
     * @return
     */
    public BluetoothAdapter bluetoothAdapter() {
        return bluetoothAdapter;
    }

    /**
     * 是否支持蓝牙
     *
     * @return
     */
    public boolean isSupport() {
        return bluetoothAdapter != null;
    }

    /**
     * 蓝牙是否可用
     *
     * @return
     */
    public boolean isEnabled() {
        if (bluetoothAdapter == null) {
            return false;
        }
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 开启蓝牙（隐式打开）
     *
     * @return
     * @confirm 是否弹出确认对话框
     */
    public Bluetooth enable(boolean confirm) {
        if (confirm) {
            if (context instanceof AppCompatActivity) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                appCompatActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            if (bluetoothAdapter != null) {
                bluetoothAdapter.enable();
            }
        }
        return bluetooth;
    }

    /**
     * 关闭蓝牙
     *
     * @return
     */
    public Bluetooth disable() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.disable();
        }
        return bluetooth;
    }

    /**
     * 获取已经配对过的设备的集合
     *
     * @return
     */
    public Set<BluetoothDevice> bondedDevices() {
        if (bluetoothAdapter == null) {
            return null;
        }
        return bluetoothAdapter.getBondedDevices();
    }

    /**
     * 配对
     *
     * @param device 配对设备
     * @return
     * @throws Exception
     */
    public boolean createBond(BluetoothDevice device) throws Exception {
        Method createBondMethod = device.getClass().getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     * 移除配对
     *
     * @param device 配对设备
     * @return
     * @throws Exception
     */
    public boolean removeBond(BluetoothDevice device) throws Exception {
        Method removeBondMethod = device.getClass().getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     * 让蓝牙可以被找到
     */
    public Bluetooth discoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            context.startActivity(discoverableIntent);
        } else {
            Log.i(TAG, "->discoverable is able");
        }
        return bluetooth;
    }

    /**
     * 设置发现蓝牙的超时时间
     *
     * @param time 时间（建议100）
     * @return
     */
    public Bluetooth timeOut(int time) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(adapter, time);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bluetooth;
    }

    /**
     * 开始扫描
     *
     * @param mode     传统蓝牙模式：{@link Bluetooth#MODE_SPP},低功耗模式：{@link Bluetooth#MODE_BLE}
     * @param listener 回调函数
     * @return
     */
    public Bluetooth startScan(int mode, OnBluetoothScanListener listener) {
        return startScan(mode, null, null, listener);
    }

    /**
     * 开始扫描
     *
     * @param mode         传统蓝牙模式：{@link Bluetooth#MODE_SPP},低功耗模式：{@link Bluetooth#MODE_BLE}
     * @param filters      BLE扫描过滤
     * @param scanSettings BLE扫描设置
     * @param listener     回调函数
     * @return
     */
    public Bluetooth startScan(int mode, List<ScanFilter> filters, ScanSettings scanSettings, OnBluetoothScanListener listener) {
        if (bluetoothAdapter == null) {
            return bluetooth;
        }
        if (mode == MODE_SPP) {
            bluetoothAdapter.startDiscovery();
            if (sppReceiver != null) {
                context.unregisterReceiver(sppReceiver);
                sppReceiver = null;
            }
            if (sppReceiver == null) {
                sppReceiver = new SPPReceiver(listener);
                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                context.registerReceiver(sppReceiver, filter);
            }
        }
        if (mode == MODE_BLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (filters != null && scanSettings != null) {
                    bluetoothAdapter.getBluetoothLeScanner().startScan(filters, scanSettings, new BLEScanCallBack(listener));
                } else {
                    bluetoothAdapter.getBluetoothLeScanner().startScan(new BLEScanCallBack(listener));
                }
            }
        }
        return bluetooth;
    }

    /**
     * 构建搜索设置
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        return builder.build();
    }

    /**
     * 构建扫描过滤器
     *
     * @param ids
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<ScanFilter> buildScanFilters(String[] ids) {
        List<ScanFilter> scanFilters = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            scanFilters.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ids[i])).build());
        }
        return scanFilters;
    }

    /**
     * 停止扫描
     *
     * @param mode 传统蓝牙模式：{@link Bluetooth#MODE_SPP},低功耗模式：{@link Bluetooth#MODE_BLE}
     */
    public void stopScan(int mode) {
        stopScan(mode, null);
    }

    /**
     * 停止扫描
     *
     * @param mode     传统蓝牙模式：{@link Bluetooth#MODE_SPP},低功耗模式：{@link Bluetooth#MODE_BLE}
     * @param listener 回调监听
     */
    public void stopScan(int mode, OnBluetoothScanListener listener) {
        if (mode == MODE_SPP) {
            if (bluetoothAdapter != null) {
                bluetoothAdapter.cancelDiscovery();
            }
            if (sppReceiver != null) {
                context.unregisterReceiver(sppReceiver);
                sppReceiver = null;
            }
        }
        if (mode == MODE_BLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothAdapter.getBluetoothLeScanner().stopScan(new BLEScanCallBack(listener));
            }
        }
    }


    /**
     * 传统蓝牙接收器
     */
    private class SPPReceiver extends BroadcastReceiver {

        public OnBluetoothScanListener listener;

        public SPPReceiver(OnBluetoothScanListener listener) {
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "->onBluetoothFound() - name=" + device.getName() + " , address=" + device.getAddress());
                if (listener != null) {
                    listener.onBluetoothFound(device);
                }
            }
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Log.i(TAG, "->onBluetoothDiscoveryFinished()");
                if (listener != null) {
                    listener.onBluetoothDiscoveryFinished();
                }
                stopScan(MODE_SPP);
            }
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "->onBluetoothBondStateChanged() - name=" + device.getName() + " , address=" + device.getAddress());
                if (listener != null) {
                    listener.onBluetoothBondStateChanged(device.getBondState());
                }
            }
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "->onBluetoothConnected() - name=" + device.getName() + " , address=" + device.getAddress());
                if (listener != null) {
                    listener.onBluetoothConnected(device);
                }
            }
            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "->onBluetoothDisconnected() - name=" + device.getName() + " , address=" + device.getAddress());
                if (listener != null) {
                    listener.onBluetoothDisconnected(device);
                }
            }
        }
    }

    /**
     * BLE低功耗扫描回调
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class BLEScanCallBack extends ScanCallback {

        public OnBluetoothScanListener listener;

        public BLEScanCallBack(OnBluetoothScanListener listener) {
            this.listener = listener;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (listener != null) {
                listener.onBluetoothLeScanResult(callbackType, result);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            if (listener != null) {
                listener.onBluetoothLeBatchScanResults(results);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i(TAG, "->onScanFailed() - errorCode=" + errorCode);
            if (listener != null) {
                listener.onBluetoothScanFailed(errorCode);
            }
        }

    }

}
