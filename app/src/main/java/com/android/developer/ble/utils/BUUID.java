package com.android.developer.ble.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用UUID
 */
public class BUUID {

    public static final int TYPE_SERVICE = 0;
    public static final int TYPE_CHARACTERISTICS_READ = 1;
    public static final int TYPE_CHARACTERISTICS_WRITE = 2;
    public static final int TYPE_CHARACTERISTICS_NOTIFY = 3;
    public static final int TYPE_DESCRIPTOR = 4;

    public static final String NAME_UNKNOWN = "Unknown";
    public static final String NAME_SERVICE = "SERVICE";
    public static final String NAME_CHARACTERISTIC_READ = "CHARACTERISTIC_READ";
    public static final String NAME_CHARACTERISTIC_WRITE = "CHARACTERISTIC_WRITE";
    public static final String NAME_CHARACTERISTIC_NOTIFY = "CHARACTERISTIC_NOTIFY";
    public static final String NAME_DESCRIPTOR = "DESCRIPTOR";
    /**
     * 默认服务ID
     */
    private String UUID_SERVICE = "00001101-0000-1000-8000-00805f9b34fb";
    /**
     * 特征ID - 读取权限
     */
    private String UUID_CHARACTERISTIC_READ = "00003a00-0000-1000-8000-00805f9b34fb";
    /**
     * 特征ID - 写入权限
     */
    private String UUID_CHARACTERISTIC_WRITE = "00003a01-0000-1000-8000-00805f9b34fb";
    /**
     * 特征ID - 通知权限
     */
    private String UUID_CHARACTERISTIC_NOTIFY = "00003a02-0000-1000-8000-00805f9b34fb";
    /**
     * 描述ID
     */
    private String UUID_DESCRIPTOR = "00003ae1-0000-1000-8000-00805f9b34fb";
    /**
     * 服务
     */
    private Map<String, String> services;
    /**
     * 特征
     */
    private Map<String, String> characteristics;
    /**
     * 描述
     */
    private Map<String, String> descriptors;

    public BUUID() {
        initUUIDs();
    }

    /**
     * 添加UUID
     *
     * @param type UUID类型
     * @param uuid UUID
     * @param name 名称
     */
    public void put(int type, String uuid, String name) {
        if (type == TYPE_SERVICE) {
            services.put(uuid, name);
        }
        if (type == TYPE_CHARACTERISTICS_READ) {
            characteristics.put(uuid, name);
        }
        if (type == TYPE_CHARACTERISTICS_WRITE) {
            characteristics.put(uuid, name);
        }
        if (type == TYPE_CHARACTERISTICS_NOTIFY) {
            characteristics.put(uuid, name);
        }
        if (type == TYPE_DESCRIPTOR) {
            characteristics.put(uuid, name);
        }
    }

    /**
     * 添加UUID
     *
     * @param type UUID类型
     * @param uuid UUID
     */
    public void put(int type, String uuid) {
        String descriptor = NAME_UNKNOWN;
        if (type == TYPE_SERVICE) {
            descriptor = NAME_SERVICE;
        }
        if (type == TYPE_CHARACTERISTICS_READ) {
            descriptor = NAME_CHARACTERISTIC_READ;
        }
        if (type == TYPE_CHARACTERISTICS_WRITE) {
            descriptor = NAME_CHARACTERISTIC_WRITE;
        }
        if (type == TYPE_CHARACTERISTICS_NOTIFY) {
            descriptor = NAME_CHARACTERISTIC_NOTIFY;
        }
        if (type == TYPE_DESCRIPTOR) {
            descriptor = NAME_DESCRIPTOR;
        }
        put(type, uuid, descriptor);
    }

    /**
     * 通过名称查找UUID
     *
     * @param name
     * @return
     */
    public String value(String name) {
        for (String key : services.keySet()) {
            if (name.equals(services.get(key))) {
                return key;
            }
        }
        for (String key : characteristics.keySet()) {
            if (name.equals(characteristics.get(key))) {
                return key;
            }
        }
        for (String key : descriptors.keySet()) {
            if (name.equals(descriptors.get(key))) {
                return key;
            }
        }
        return "";
    }

    /**
     * 获取所有的服务列表
     *
     * @return
     */
    public Map<String, String> getServices() {
        return services;
    }

    /**
     * 获取所有的特征信息
     *
     * @return
     */
    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    /**
     * 获取所有的描述信息
     *
     * @return
     */
    public Map<String, String> getDescriptors() {
        return descriptors;
    }

    /**
     * 查找Service
     *
     * @param uuid Service UUID
     * @return
     */
    public String findServiceName(String uuid) {
        return services.get(uuid) == null ? NAME_UNKNOWN : services.get(uuid);
    }

    /**
     * 查找特征
     *
     * @param uuid 特征UUID
     * @return
     */
    public String findCharacteristicName(String uuid) {
        return characteristics.get(uuid) == null ? NAME_UNKNOWN : characteristics.get(uuid);
    }

    /**
     * 查找描述
     *
     * @param uuid 描述UUID
     * @return
     */
    public String findDescriptorName(String uuid) {
        return descriptors.get(uuid) == null ? NAME_UNKNOWN : descriptors.get(uuid);
    }

    /**
     * 查找对应ID名称
     *
     * @param uuid
     * @return
     */
    public String find(String uuid) {
        String name;
        name = findServiceName(uuid);
        if (name == null) {
            name = findCharacteristicName(uuid);
        }
        if (name == null) {
            name = findDescriptorName(uuid);
        }
        return name == null ? NAME_UNKNOWN : name;
    }

    /**
     * 初始化UUID
     */
    private void initUUIDs() {
        services = new HashMap<>();
        services.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        services.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        services.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        services.put("00001800-0000-1000-8000-00805f9b34fb", "GenericAccess");
        services.put("00001801-0000-1000-8000-00805f9b34fb", "GenericAttribute");
        services.put("00001802-0000-1000-8000-00805f9b34fb", "Immediate Alert");
        services.put("00001804-0000-1000-8000-00805f9b34fb", "Tx Power");
        services.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");
        services.put("00001806-0000-1000-8000-00805f9b34fb", "Reference Time Update Service");
        services.put("00001807-0000-1000-8000-00805f9b34fb", "Next DST Change Service");
        services.put("00001808-0000-1000-8000-00805f9b34fb", "Glucose");
        services.put("00001809-0000-1000-8000-00805f9b34fb", "Health Thermometer");
        services.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information");
        services.put("0000180b-0000-1000-8000-00805f9b34fb", "Network Availability");
        services.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate");
        services.put("0000180e-0000-1000-8000-00805f9b34fb", "Phone Alert Status Service");
        services.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
        services.put("00001810-0000-1000-8000-00805f9b34fb", "Blood Pressure");
        services.put("00001811-0000-1000-8000-00805f9b34fb", "Alert Notification Service");
        services.put("00001812-0000-1000-8000-00805f9b34fb", "Human Interface Device");
        services.put("00001813-0000-1000-8000-00805f9b34fb", "Scan Parameters");
        services.put("00001814-0000-1000-8000-00805f9b34fb", "Running Speed and Cadence");
        services.put("00001816-0000-1000-8000-00805f9b34fb", "Cycling Speed and Cadence");
        services.put("00001818-0000-1000-8000-00805f9b34fb", "Cycling Power");
        services.put("00001819-0000-1000-8000-00805f9b34fb", "Location and Navigation");
        services.put("00001802-0000-1000-8000-00805f9b34fb", "Immediate Alert");
        services.put("00001803-0000-1000-8000-00805f9b34fb", "Link Loss");
        services.put("00001804-0000-1000-8000-00805f9b34fb", "Tx Power");
        services.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");
        services.put("00001806-0000-1000-8000-00805f9b34fb", "Reference Time Update Service");
        services.put("00001807-0000-1000-8000-00805f9b34fb", "Next DST Change Service");
        services.put("00001808-0000-1000-8000-00805f9b34fb", "Glucose");
        services.put("00001809-0000-1000-8000-00805f9b34fb", "Health Thermometer");
        services.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information");
        services.put("0000180b-0000-1000-8000-00805f9b34fb", "Network Availability");
        services.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate");
        services.put("0000180e-0000-1000-8000-00805f9b34fb", "Phone Alert Status Service");
        services.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
        services.put("00001810-0000-1000-8000-00805f9b34fb", "Blood Pressure");
        services.put("00001811-0000-1000-8000-00805f9b34fb", "Alert Notification Service");
        services.put("00001812-0000-1000-8000-00805f9b34fb", "Human Interface Device");
        services.put("00001813-0000-1000-8000-00805f9b34fb", "Scan Parameters");
        services.put("00001814-0000-1000-8000-00805f9b34fb", "Running Speed and Cadence");
        services.put("00001816-0000-1000-8000-00805f9b34fb", "Cycling Speed and Cadence");
        services.put("00001818-0000-1000-8000-00805f9b34fb", "Cycling Power");
        services.put("00001819-0000-1000-8000-00805f9b34fb", "Location and Navigation");


        characteristics = new HashMap<>();
        characteristics.put("00002a37-0000-1000-8000-00805f9b34fb", "Heart Rate Measurement");
        characteristics.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        characteristics.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        characteristics.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        characteristics.put("00002a02-0000-1000-8000-00805f9b34fb", "Peripheral Privacy Flag");
        characteristics.put("00002a03-0000-1000-8000-00805f9b34fb", "Reconnection Address");
        characteristics.put("00002a04-0000-1000-8000-00805f9b34fb", "PPCP");
        characteristics.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
        characteristics.put("00002a06-0000-1000-8000-00805f9b34fb", "Alert Level");
        characteristics.put("00002a07-0000-1000-8000-00805f9b34fb", "Tx Power Level");
        characteristics.put("00002a08-0000-1000-8000-00805f9b34fb", "Date Time");
        characteristics.put("00002a09-0000-1000-8000-00805f9b34fb", "Day of Week");
        characteristics.put("00002a0a-0000-1000-8000-00805f9b34fb", "Day Date Time");
        characteristics.put("00002a0c-0000-1000-8000-00805f9b34fb", "Exact Time 256");
        characteristics.put("00002a0d-0000-1000-8000-00805f9b34fb", "DST Offset");
        characteristics.put("00002a0e-0000-1000-8000-00805f9b34fb", "Time Zone");
        characteristics.put("00002a0f-0000-1000-8000-00805f9b34fb", "Local Time Information");
        characteristics.put("00002a11-0000-1000-8000-00805f9b34fb", "Time with DST");
        characteristics.put("00002a12-0000-1000-8000-00805f9b34fb", "Time Accuracy");
        characteristics.put("00002a13-0000-1000-8000-00805f9b34fb", "Time Source");
        characteristics.put("00002a14-0000-1000-8000-00805f9b34fb", "Reference Time Information");
        characteristics.put("00002a16-0000-1000-8000-00805f9b34fb", "Time Update Control Point");
        characteristics.put("00002a17-0000-1000-8000-00805f9b34fb", "Time Update State");
        characteristics.put("00002a18-0000-1000-8000-00805f9b34fb", "Glucose Measurement");
        characteristics.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");
        characteristics.put("00002a1c-0000-1000-8000-00805f9b34fb", "Temperature Measurement");
        characteristics.put("00002a1d-0000-1000-8000-00805f9b34fb", "Temperature Type");
        characteristics.put("00002a1e-0000-1000-8000-00805f9b34fb", "Intermediate Temperature");
        characteristics.put("00002a21-0000-1000-8000-00805f9b34fb", "Measurement Interval");
        characteristics.put("00002a22-0000-1000-8000-00805f9b34fb", "Boot Keyboard Input Report");
        characteristics.put("00002a23-0000-1000-8000-00805f9b34fb", "System ID");
        characteristics.put("00002a24-0000-1000-8000-00805f9b34fb", "Model Number String");
        characteristics.put("00002a25-0000-1000-8000-00805f9b34fb", "Serial Number String");
        characteristics.put("00002a26-0000-1000-8000-00805f9b34fb", "Firmware Revision String");
        characteristics.put("00002a27-0000-1000-8000-00805f9b34fb", "Hardware Revision String");
        characteristics.put("00002a28-0000-1000-8000-00805f9b34fb", "Software Revision String");
        characteristics.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        characteristics.put("00002a2a-0000-1000-8000-00805f9b34fb", "IEEE 11073-20601 Regulatory Certification Data List");
        characteristics.put("00002a2b-0000-1000-8000-00805f9b34fb", "Current Time");
        characteristics.put("00002a31-0000-1000-8000-00805f9b34fb", "Scan Refresh");
        characteristics.put("00002a32-0000-1000-8000-00805f9b34fb", "Boot Keyboard Output Report");
        characteristics.put("00002a33-0000-1000-8000-00805f9b34fb", "Boot Mouse Input Report");
        characteristics.put("00002a34-0000-1000-8000-00805f9b34fb", "Glucose Measurement Context");
        characteristics.put("00002a35-0000-1000-8000-00805f9b34fb", "Blood Pressure Measurement");
        characteristics.put("00002a36-0000-1000-8000-00805f9b34fb", "Intermediate Cuff Pressure");
        characteristics.put("00002a37-0000-1000-8000-00805f9b34fb", "Heart Rate Measurement");
        characteristics.put("00002a38-0000-1000-8000-00805f9b34fb", "Body Sensor Location");
        characteristics.put("00002a39-0000-1000-8000-00805f9b34fb", "Heart Rate Control Point");
        characteristics.put("00002a3e-0000-1000-8000-00805f9b34fb", "Network Availability");
        characteristics.put("00002a3f-0000-1000-8000-00805f9b34fb", "Alert Status");
        characteristics.put("00002a40-0000-1000-8000-00805f9b34fb", "Ringer Control Point");
        characteristics.put("00002a41-0000-1000-8000-00805f9b34fb", "Ringer Setting");
        characteristics.put("00002a42-0000-1000-8000-00805f9b34fb", "Alert Category ID Bit Mask");
        characteristics.put("00002a43-0000-1000-8000-00805f9b34fb", "Alert Category ID");
        characteristics.put("00002a44-0000-1000-8000-00805f9b34fb", "Alert Notification Control Point");
        characteristics.put("00002a45-0000-1000-8000-00805f9b34fb", "Unread Alert Status");
        characteristics.put("00002a46-0000-1000-8000-00805f9b34fb", "New Alert");
        characteristics.put("00002a47-0000-1000-8000-00805f9b34fb", "Supported New Alert Category");
        characteristics.put("00002a48-0000-1000-8000-00805f9b34fb", "Supported Unread Alert Category");
        characteristics.put("00002a49-0000-1000-8000-00805f9b34fb", "Blood Pressure Feature");
        characteristics.put("00002a4a-0000-1000-8000-00805f9b34fb", "HID Information");
        characteristics.put("00002a4b-0000-1000-8000-00805f9b34fb", "Report Map");
        characteristics.put("00002a4c-0000-1000-8000-00805f9b34fb", "HID Control Point");
        characteristics.put("00002a4d-0000-1000-8000-00805f9b34fb", "Report");
        characteristics.put("00002a4e-0000-1000-8000-00805f9b34fb", "Protocol Mode");
        characteristics.put("00002a4f-0000-1000-8000-00805f9b34fb", "Scan Interval Window");
        characteristics.put("00002a50-0000-1000-8000-00805f9b34fb", "PnP ID");
        characteristics.put("00002a51-0000-1000-8000-00805f9b34fb", "Glucose Feature");
        characteristics.put("00002a52-0000-1000-8000-00805f9b34fb", "Record Access Control Point");
        characteristics.put("00002a53-0000-1000-8000-00805f9b34fb", "RSC Measurement");
        characteristics.put("00002a54-0000-1000-8000-00805f9b34fb", "RSC Feature");
        characteristics.put("00002a55-0000-1000-8000-00805f9b34fb", "SC Control Point");
        characteristics.put("00002a5b-0000-1000-8000-00805f9b34fb", "CSC Measurement");
        characteristics.put("00002a5c-0000-1000-8000-00805f9b34fb", "CSC Feature");
        characteristics.put("00002a5d-0000-1000-8000-00805f9b34fb", "Sensor Location");
        characteristics.put("00002a63-0000-1000-8000-00805f9b34fb", "Cycling Power Measurement");
        characteristics.put("00002a64-0000-1000-8000-00805f9b34fb", "Cycling Power Vector");
        characteristics.put("00002a65-0000-1000-8000-00805f9b34fb", "Cycling Power Feature");
        characteristics.put("00002a66-0000-1000-8000-00805f9b34fb", "Cycling Power Control Point");
        characteristics.put("00002a67-0000-1000-8000-00805f9b34fb", "Location and Speed");
        characteristics.put("00002a68-0000-1000-8000-00805f9b34fb", "Navigation");
        characteristics.put("00002a69-0000-1000-8000-00805f9b34fb", "Position Quality");
        characteristics.put("00002a6a-0000-1000-8000-00805f9b34fb", "LN Feature");
        characteristics.put("00002a6b-0000-1000-8000-00805f9b34fb", "LN Control Point");
        characteristics.put("00002aa6-0000-1000-8000-00805f9b34fb", "Appearance");

        descriptors = new HashMap<>();
        descriptors.put("00002900-0000-1000-8000-00805f9b34fb", "Characteristic Extended Properties");
        descriptors.put("00002901-0000-1000-8000-00805f9b34fb", "Characteristic User Description");
        descriptors.put("00002902-0000-1000-8000-00805f9b34fb", "Client Characteristic Configuration");
        descriptors.put("00002903-0000-1000-8000-00805f9b34fb", "Server Characteristic Configuration");
        descriptors.put("00002904-0000-1000-8000-00805f9b34fb", "Characteristic Presentation Format");
        descriptors.put("00002905-0000-1000-8000-00805f9b34fb", "Characteristic Aggregate Format");
        descriptors.put("00002906-0000-1000-8000-00805f9b34fb", "Valid Range");
        descriptors.put("00002907-0000-1000-8000-00805f9b34fb", "External Report Reference Descriptor");
        descriptors.put("00002908-0000-1000-8000-00805f9b34fb", "Report Reference Descriptor");

        //加入默认的设备UUID
        put(TYPE_SERVICE, UUID_SERVICE, NAME_SERVICE);
        put(TYPE_CHARACTERISTICS_READ, UUID_CHARACTERISTIC_READ, NAME_CHARACTERISTIC_READ);
        put(TYPE_CHARACTERISTICS_WRITE, UUID_CHARACTERISTIC_WRITE, NAME_CHARACTERISTIC_WRITE);
        put(TYPE_CHARACTERISTICS_NOTIFY, UUID_CHARACTERISTIC_NOTIFY, NAME_CHARACTERISTIC_NOTIFY);
        put(TYPE_DESCRIPTOR, UUID_DESCRIPTOR, NAME_DESCRIPTOR);
    }

}
