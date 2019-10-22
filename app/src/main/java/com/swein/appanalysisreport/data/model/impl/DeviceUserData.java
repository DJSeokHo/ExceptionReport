package com.swein.appanalysisreport.data.model.impl;


import com.swein.appanalysisreport.data.model.AppAnalysisData;

public class DeviceUserData implements AppAnalysisData {

    private final static String DEVICE_UUID_TITLE = "手机的 UUID: ";
    private final static String DEVICE_MODEL_TITLE = "手机型号: ";
    private final static String OS_VERSION_TITLE = "系统版本: ";
    private final static String APP_PACKAGE_NAME_TITLE = "App包名: ";
    private final static String APP_VERSION_TITLE = "App版本: ";
    private final static String OTHER_TITLE = "备注: ";

    /* 手机的 UUID (不变且唯一) */
    public String deviceUUID = "";

    /* 手机型号 */
    public String deviceModel = "";

    /* 系统版本 */
    public String osVersion = "";

    /* App包名 */
    public String appPackageName = "";

    /* App版本 */
    public String appVersion = "";

    /* 备注 */
    public String other = "";


    public DeviceUserData(String deviceUUID, String deviceModel, String osVersion, String appPackageName, String appVersion, String other) {
        this.deviceUUID = deviceUUID;
        this.deviceModel = deviceModel;
        this.osVersion = osVersion;
        this.appPackageName = appPackageName;
        this.appVersion = appVersion;
        this.other = other;
    }

    @Override
    public String toString() {
        return deviceUUID + " " + deviceModel + " " + osVersion + " " + appPackageName + " " + appVersion + " " + other;
    }

    @Override
    public String toReport() {

        return
                DEVICE_UUID_TITLE + deviceUUID + "\r\n" +
                DEVICE_MODEL_TITLE + deviceModel + "\r\n" +
                OS_VERSION_TITLE + osVersion + "\r\n" +
                APP_PACKAGE_NAME_TITLE + appPackageName + "\r\n" +
                APP_VERSION_TITLE + appVersion + "\r\n" +
                OTHER_TITLE + other + "\r\n\r\n";
    }

}
