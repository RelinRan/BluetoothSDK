package com.android.developer.ble.utils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Author: Relin
 * Describe:进制转换
 * Date:2020/5/15 17:05
 */
public class ByteBus {

    /**
     * 升序
     */
    public static int SORT_ASC = 0;
    /**
     * 降序
     */
    public static int SORT_DESC = 1;
    /**
     * 反序
     */
    public static int SORT_REVERSE = 2;
    /**
     * 字节存储小端模式
     */
    public static int ENDIAN_LITTLE = 3;
    /**
     * 字节存储大端模式
     */
    public static int ENDIAN_BIG = 4;
    /**
     * 数字+字母表
     */
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public ByteBus() {
    }

    /**
     * byte[]转16进制
     *
     * @param data
     * @return
     */
    public static String encodeHex(byte[] data) {
        if (data==null||data.length==0){
            return "";
        }
        return encodeHex(data, 0);
    }

    /**
     * byte[]转16进制
     *
     * @param data
     * @param group
     * @return
     */
    public static String encodeHex(byte[] data, int group) {
        int l = data.length;
        char[] out = new char[(l << 1) + (group > 0 ? l / group : 0)];
        int i = 0;
        for (int j = 0; i < l; ++i) {
            if (group > 0 && i % group == 0 && j > 0) {
                out[j++] = '-';
            }
            out[j++] = DIGITS[(240 & data[i]) >>> 4];
            out[j++] = DIGITS[15 & data[i]];
        }
        return new String(out);
    }

    /**
     * 16进制转byte[]
     *
     * @param hexString
     * @return
     */
    public static byte[] decodeHex(String hexString) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hexString.length(); i += 2) {
            int b = Integer.parseInt(hexString.substring(i, i + 2), 16);
            bas.write(b);
        }
        return bas.toByteArray();
    }

    /**
     * 填充0
     *
     * @param targetLength 目标长度
     * @param origin       源数据
     * @return
     */
    public static String fillZero(int targetLength, String origin) {
        int needSize = targetLength - origin.length();
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < needSize; i++) {
            sb.append("0");
        }
        return sb.toString() + origin;
    }

    /**
     * 二进制字符串转16进制
     *
     * @param binStr
     * @return
     */
    public static String binaryToHex(String binStr) {
        return Integer.toHexString(Integer.parseInt(binStr.trim(), 2));
    }

    /**
     * 16进制转2进制字符串
     *
     * @param hexStr
     * @return
     */
    public static String hexToBinary(String hexStr) {
        return Integer.toBinaryString(Integer.parseInt(hexStr.trim(), 16));
    }

    /**
     * Bytes转Int
     * @param data 数据
     * @return
     */
    public static int bytesToInt(byte[] data){
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * Bytes转Int
     * @param data 数据
     * @param order 字节存储顺序
     * @return
     */
    public static int bytesToInt(byte[] data, ByteOrder order){
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.order(order);
        buffer.put(data);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * Int转 Bytes
     * @param value
     * @return
     */
    public static byte[] intToBytes(int value){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data,0,buffer.limit());
        return data;
    }

    /**
     * Int转Bytes
     * @param value 数据
     * @param order 字节存储顺序
     * @return
     */
    public static byte[] intToBytes(int value,ByteOrder order){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(order);
        buffer.putInt(value);
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data,0,buffer.limit());
        return data;
    }

    /**
     * 16进制转10进制
     *
     * @param hex
     * @return
     */
    public static int hexToDec(String hex) {
        BigInteger bigint = new BigInteger(hex, 16);
        return bigint.intValue();
    }

    /**
     * 10进制转16进制
     *
     * @param dec
     * @return
     */
    public static String decToHex(int dec) {
        return Integer.toHexString(dec);
    }

    /**
     * 字节存储次序
     * @param data    字节数组
     * @param endian  储次序,小端模式：{@link ByteBus#ENDIAN_LITTLE}；大端模式：{@link ByteBus#ENDIAN_BIG}
     * @return
     */
    public static byte[] endian(byte[] data,int endian){
        if (data==null||data.length==0||data.length==1){
            return data;
        }
        String hexStart = encodeHex(new byte[]{data[0]});
        String hexEnd = encodeHex(new byte[]{data[data.length-1]});
        boolean isLittle = hexToDec(hexStart) <= hexToDec(hexEnd);
        if (endian==ENDIAN_LITTLE&&isLittle){
            return data;
        }
        if (endian==ENDIAN_BIG&&!isLittle){
            return data;
        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                byte temp = data[data.length-1-j];
                data[data.length-1-j] = data[j];
                data[j] = temp;
            }
        }
        return data;
    }

    /**
     * 排序
     *
     * @param bytes byte数组
     * @param type  升序：{@link ByteBus#SORT_ASC};降序：{@link ByteBus#SORT_DESC}
     * @return
     */
    public static byte[] sort(byte[] bytes, int type) {
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes.length - i - 1; j++) {
                if (type == 0 ? byteHexIntValue(bytes[j]) > byteHexIntValue(bytes[j + 1]) : byteHexIntValue(bytes[j]) < byteHexIntValue(bytes[j + 1])) {
                    byte temp = bytes[j + 1];
                    bytes[j + 1] = bytes[j];
                    bytes[j] = temp;
                }
            }
        }
        return bytes;
    }

    /**
     * byte转16进制的字符数据总和
     *
     * @param bt
     * @return
     */
    private static int byteHexIntValue(byte bt) {
        String hex = ByteBus.encodeHex(new byte[]{bt});
        return hexToDec(hex);
    }

    /**
     * 二进制取反
     *
     * @param data 二进制数据
     * @return
     */
    public static byte[] binaryInversion(byte[] data) {
        String hex = encodeHex(data);
        String binary = hexToBinary(hex);
        //取反操作
        char[] binaryChars = binary.toCharArray();
        char[] newBinaryChars = binary.toCharArray();
        for (int i = 0; i < binaryChars.length; i++) {
            newBinaryChars[i] = binaryChars[i] == '0' ? '1' : '0';
        }
        String hexStr = binaryToHex(String.valueOf(newBinaryChars));
        hexStr = ByteBus.fillZero(data.length * 2, hexStr);
        return decodeHex(hexStr);
    }

}
