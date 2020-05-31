package com.android.developer.ble.utils;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

/**
 * Author: Relin
 * Describe: 扫描数据
 * Date:2020/5/31 8:14
 */
public class ScanRecordParser {

    private static String TAG = "ParseScanRecord";
    /**
     * bit 0: LE 有限发现模式
     * bit 1: LE 普通发现模式
     * bit 2: 不支持 BR/EDR
     * bit 3: 对 Same Device Capable(Controller) 同时支持 BLE 和 BR/EDR
     * bit 4: 对 Same Device Capable(Host) 同时支持 BLE 和 BR/EDR
     * bit 5..7: 预留
     */
    public final static byte FUNCTION = (byte)0x01;
    /**
     * 非完整的 16 bit UUID 列表
     */
    public final static byte SERVICE_UUID_16_UN_INTACT = (byte)0x02;
    /**
     * 完整的 16 bit UUID 列表
     */
    public final static byte SERVICE_UUID_16_INTACT = (byte)0x03;
    /**
     * 非完整的 32 bit UUID 列表
     */
    public final static byte SERVICE_UUID_32_UN_INTACT = (byte)0x04;
    /**
     * 完整的 32 bit UUID 列表
     */
    public final static byte SERVICE_UUID_32_INTACT = (byte)0x05;
    /**
     * 非完整的 128 bit UUID 列表
     */
    public final static byte SERVICE_UUID_128_UN_INTACT = (byte)0x06;
    /**
     * 完整的 128 bit UUID 列表
     */
    public final static byte SERVICE_UUID_128_INTACT = (byte)0x07;
    /**
     * 设备全名
     */
    public final static byte DEVICE_NAME_INTACT = (byte)0x08;
    /**
     * 设备简称
     */
    public final static byte DEVICE_NAME_SHORT = (byte)0x09;
    /**
     * 信号强度
     * DATA 部分是一个字节,表示 -127 到 + 127 dBm
     */
    public final static byte TX_POWER_LEVEL = (byte)0x0A;
    /**
     * 安全管理
     * bit 0: OOB Flag，0 表示没有 OOB 数据，1 表示有
     * bit 1: 支持 LE
     * bit 2: 对 Same Device Capable(Host) 同时支持 BLE 和 BR/EDR
     * bit 3: 地址类型，0 表示公开地址，1 表示随机地址
     */
    public final static byte SECURITY_MANAGER_OUT_OF_BAND = (byte)0x11;
    /**
     * 外设（Slave）连接间隔范围
     * 数据中定义了 Slave 最大和最小连接间隔，数据包含 4 个字节
     * 前 2 字节：定义最小连接间隔，取值范围：0x0006 ~ 0x0C80，而 0xFFFF 表示未定义；
     * 后 2 字节：定义最大连接间隔，同上，不过需要保证最大连接间隔大于或者等于最小连接间隔。
     */
    public final static byte CONNECTION_INTERVAL_RANGE = (byte)0x12;
    /**
     * 服务搜寻 - 16 bit UUID 列表
     */
    public final static byte SEARCH_SERVICE_UUID_16 = (byte)0x14;
    /**
     * 服务搜寻 - 128 bit UUID 列表
     */
    public final static byte SEARCH_SERVICE_UUID_128 = (byte)0x15;
    /**
     * Service Data
     * 16 bit UUID Service: TYPE = 0x16, 前 2 字节是 UUID，后面是 Service 的数据；
     * 前 2 字节是 UUID，后面是 Service 的数据
     */
    public final static byte SERVICE_DATA_UUID_16 = (byte)0x16;
    /**
     * Service Data
     * 32 bit UUID Service: TYPE = 0x20, 前 4 字节是 UUID，后面是 Service 的数据；
     */
    public final static byte SERVICE_DATA_UUID_32 = (byte)0x20;
    /**
     * Service Data
     * 128 bit UUID Service: TYPE = 0x21, 前 16 字节是 UUID，后面是 Service 的数据；
     */
    public final static byte SERVICE_DATA_UUID_128 = (byte)0x21;
    /**
     *公开目标地址
     * 表示希望这个广播包被指定的目标设备处理，此设备绑定了公开地址，DATA 是目标地址列表，每个地址 6 字节。
     */
    public final static byte OPEN_TARGET_ADDRESS = (byte)0x17;
    /**
     * 随机目标地址
     */
    public final static byte RANDOM_TARGET_ADDRESS = (byte)0x18;
    /**
     * 外观
     */
    public final static byte APPEARANCE = (byte)0x19;
    /**
     * 自定义数据
     */
    public final static byte CUSTOM_DATA = (byte)0xFF;

    /**
     * 解析数据
     * @param data
     * @return
     */
    public static HashMap<Byte,byte[]> decode(byte[] data){
        Log.i(TAG,"->Hex = "+ByteBus.encodeHex(data));
        HashMap<Byte,byte[]> map = new HashMap<>();
        int index =0;
        while (true){
            int length = ByteBus.bytesToInt(new byte[]{(byte)0x00,(byte)0x00,(byte)0x00, data[index]});
            byte key = data[index+1];
            if (length==0){
                break;
            }
            byte[] value = new byte[length-1];
            System.arraycopy(data,index+2,value,0,length-1);
            map.put(key,value);
            Log.i(TAG,"->key = "+ByteBus.encodeHex(new byte[]{key})+" , value = "+ ByteBus.encodeHex(value));
            index += length+1;
        }
        return map;
    }

}
