package com.swein.appanalysisreport.util.toast;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {

    public static void showShortToastNormal(Context context, String string) {

        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();

    }

    public static void showLongToastNormal(Context context, String string) {

        Toast.makeText(context, string, Toast.LENGTH_LONG).show();

    }
}
