# BluetoothSDK
Android蓝牙SDK | 蓝牙广播 | BLE蓝牙 | SPP 蓝牙

# 方法一 ARR依赖
[AndroidKit.arr](https://github.com/RelinRan/BluetoothSDK/blob/master/BluetoothSDK.aar)
```
android {
    ....
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}

dependencies {
    implementation(name: 'BluetoothSDK', ext: 'aar')
}

```

# 方法二   JitPack依赖
## A.项目/build.grade
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
## B.项目/app/build.grade
```
	dependencies {
	        implementation 'com.github.RelinRan:BluetoothSDK:1.0.0'
	}
```

# 蓝牙搜索
```
//权限申请
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
}
//权限申请成功进行蓝牙搜索,Bluetooth.MODE_SPP为经典搜索方式；Bluetooth.MODE_BLE为广播搜索方式
Bluetooth.with(this).enable(false).startScan(Bluetooth.MODE_SPP, onBluetoothScanListener);
```
# 蓝牙搜索监听
```
public interface OnBluetoothScanListener {

    /**
     * 发现新设备（SPP）
     *
     * @param device
     */
    void onBluetoothFound(BluetoothDevice device);

    /**
     * 设备扫描完毕（SPP）
     */
    void onBluetoothDiscoveryFinished();

    /**
     * 设备绑定状态（SPP）
     *
     * @param state {@link Bluetooth#BOND_BONDED}
     */
    void onBluetoothBondStateChanged(int state);

    /**
     * 设备连接成功（SPP）
     *
     * @param device
     */
    void onBluetoothConnected(BluetoothDevice device);

    /**
     * 设备断开连接(SPP)
     *
     * @param device
     */
    void onBluetoothDisconnected(BluetoothDevice device);

    /**
     * 低功耗设备扫描结果（BLE）
     *
     * @param callbackType {@link Bluetooth#CALLBACK_TYPE_ALL_MATCHES}
     * @param result
     */
    void onBluetoothLeScanResult(int callbackType, ScanResult result);

    /**
     * 低功耗批量处理扫描结果（BLE）
     *
     * @param results
     */
    void onBluetoothLeBatchScanResults(List<ScanResult> results);

    /**
     * 低功耗扫描失败（BLE）
     *
     * @param error
     */
    void onBluetoothScanFailed(int error);

}
```

# 蓝牙客户端连接
```
BUUID buuid = new BUUID();
//以下UUID如果不设置会有默认UUID
buuid.put(BUUID.TYPE_SERVICE, "服务UUID");//默认：BUUID.DEFAULT_UUID_SERVICE
buuid.put(BUUID.TYPE_CHARACTERISTICS_READ, "特征UUID-读");//默认：BUUID.DEFAULT_UUID_CHARACTERISTIC_READ
buuid.put(BUUID.TYPE_CHARACTERISTICS_WRITE, "特征UUID-写");//默认：BUUID.DEFAULT_UUID_CHARACTERISTIC_WRITE
buuid.put(BUUID.TYPE_CHARACTERISTICS_NOTIFY, "特征UUID-通知");//默认：DEFAULT_UUID_CHARACTERISTIC_NOTIFY
buuid.put(BUUID.TYPE_DESCRIPTOR, "描述UUID");//默认：BUUID.DEFAULT_UUID_DESCRIPTOR
client = new BluetoothClient(context, BluetoothClient.MODE_SPP, "扫描出的蓝牙地址", buuid);
client.setOnBluetoothClientListener(onBluetoothClientListener);
client.connect();
```
# 蓝牙客户端连接监听
```
public interface OnBluetoothClientListener {

    /**
     * 连接成功
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     */
    void onBluetoothClientConnected(int mode, BluetoothDevice device, BluetoothGatt gatt);

    /**
     * 断开连接
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     */
    void onBluetoothClientDisconnect(int mode, BluetoothDevice device, BluetoothGatt gatt);

    /**
     * 错误
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     * @param code   错误代码
     * @param e      异常信息
     */
    void onBluetoothClientError(int mode, BluetoothDevice device, BluetoothGatt gatt, int code, IOException e);

    /**
     * 读取数据
     *
     * @param mode     模式
     * @param device   设备
     * @param gatt     Ble对象
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothClientRead(int mode, BluetoothDevice device, BluetoothGatt gatt, int dataType, byte[] data);

    /**
     * 写入数据
     *
     * @param mode     模式
     * @param device   设备
     * @param gatt     Ble对象
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothClientWrite(int mode, BluetoothDevice device, BluetoothGatt gatt, int dataType, byte[] data);

    /**
     * 新服务发现
     *
     * @param mode   模式
     * @param device 设备
     * @param gatt   Ble对象
     * @param status 状态
     */
    void onBluetoothClientServicesDiscovered(int mode, BluetoothDevice device, BluetoothGatt gatt, int status);

}
```
# 蓝牙服务端连接
```
BUUID buuid = new BUUID();
//以下UUID如果不设置会有默认UUID
buuid.put(BUUID.TYPE_SERVICE, "服务UUID");//默认：BUUID.DEFAULT_UUID_SERVICE
buuid.put(BUUID.TYPE_CHARACTERISTICS_READ, "特征UUID-读");//默认：BUUID.DEFAULT_UUID_CHARACTERISTIC_READ
buuid.put(BUUID.TYPE_CHARACTERISTICS_WRITE, "特征UUID-写");//默认：BUUID.DEFAULT_UUID_CHARACTERISTIC_WRITE
buuid.put(BUUID.TYPE_CHARACTERISTICS_NOTIFY, "特征UUID-通知");//默认：DEFAULT_UUID_CHARACTERISTIC_NOTIFY
buuid.put(BUUID.TYPE_DESCRIPTOR, "描述UUID");//默认：BUUID.DEFAULT_UUID_DESCRIPTOR
server = new BluetoothServer(context, BluetoothClient.MODE_SPP, "服务名称", buuid);
//广播数据配置，注意：mode = BluetoothClient.MODE_BLE 才能使用广播模式；BluetoothClient.MODE_SPP 模式下删除以下配置
server.setBleManufacturerId(520);
server.setBleManufacturerData("11".getBytes());
//server.setBleServiceData("22".getBytes());//manufacturer 和 Service 数据只能选一种，不然就报错数据传输过大错误。
//设置监听开启服务
server.setOnBluetoothServerListener(this);
server.start();
```
# 蓝牙服务端连接监听
```
public interface OnBluetoothServerListener {

    /**
     * 开启广播成功
     *
     * @param mode             模式
     * @param settingsInEffect 设置
     */
    void onBluetoothServerStartAdvertiseSuccess(int mode, AdvertiseSettings settingsInEffect);

    /**
     * 开启广播失败
     *
     * @param mode  模式
     * @param error 错误代码
     */
    void onBluetoothServerStartAdvertiseFailure(int mode, int error);

    /**
     * 服务添加
     *
     * @param mode    模式
     * @param service 服务
     * @param status  状态
     */
    void onBluetoothServerServiceAdded(int mode, BluetoothGattService service, int status);

    /**
     * 连接成功
     *
     * @param mode   模式
     * @param device 设备
     */
    void onBluetoothServerConnected(int mode, BluetoothDevice device);

    /**
     * 断开连接
     *
     * @param mode   模式
     * @param device 设备
     */
    void onBluetoothServerDisconnect(int mode, BluetoothDevice device);

    /**
     * 读取数据
     *
     * @param mode     模式
     * @param device   设备
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothServerRead(int mode, BluetoothDevice device, int dataType, byte[] data);

    /**
     * 写入数据
     *
     * @param mode     模式
     * @param device   设备
     * @param dataType 数据类型
     * @param data     数据
     */
    void onBluetoothServerWrite(int mode, BluetoothDevice device, int dataType, byte[] data);

    /**
     * 错误
     *
     * @param mode   模式
     * @param device 设备
     * @param code   错误代码
     * @param e      异常信息
     */
    void onBluetoothServerError(int mode, BluetoothDevice device, int code, IOException e);

}
```