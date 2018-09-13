package com.swein.exceptionreport.util.appinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by seokho on 29/03/2017.
 */

public class AppInfoUtil {

    public static String getVersionName(Context context)
    {

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo( context.getPackageName(), 0);
        }
        catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }
        return packageInfo.versionName;

    }

    public static int getVersionCode(Context context)
    {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo    packageInfo    = null;
        int versionCode               = 0;
        try {
            packageInfo = packageManager.getPackageInfo( context.getPackageName(), PackageManager.GET_ACTIVITIES );
        }
        catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }

        if ( packageInfo != null ) {

            versionCode = packageInfo.versionCode;

        }

        return versionCode;

    }

    public static String getPackageName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo    packageInfo    = null;
        String packageName = null;
        try {
            packageInfo = packageManager.getPackageInfo( context.getPackageName(), PackageManager.GET_ACTIVITIES );
        }
        catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }

        if ( packageInfo != null ) {

            packageName = packageInfo.packageName;

        }

        return packageName;
    }

    public static boolean isThisApkInDebugMode(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isOtherApkInDebugMode(Context context,String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            if (pkginfo != null ) {
                ApplicationInfo info= pkginfo.applicationInfo;
                return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
