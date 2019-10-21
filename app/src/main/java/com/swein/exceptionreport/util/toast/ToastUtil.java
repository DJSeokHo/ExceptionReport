package com.swein.exceptionreport.util.toast;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtil {

    public static void showShortToastNormal(Context context, String string) {

        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();

    }

    public static void showLongToastNormal(Context context, String string) {

        Toast.makeText(context, string, Toast.LENGTH_LONG).show();

    }
}
