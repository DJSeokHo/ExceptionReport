package com.swein.appanalysisreport.util.device;

import android.os.Build;

public class DeviceInfoUtil {

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}
