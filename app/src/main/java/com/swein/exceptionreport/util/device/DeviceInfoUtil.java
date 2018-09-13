package com.swein.exceptionreport.util.device;

import android.os.Build;

/**
 * Created by seokho on 14/01/2017.
 */

public class DeviceInfoUtil {

    public static String getDeviceModel() {
        return Build.MODEL;
    }

}
