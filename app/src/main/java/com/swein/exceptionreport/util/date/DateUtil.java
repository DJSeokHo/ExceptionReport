package com.swein.exceptionreport.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{

    public static String getCurrentDateTimeString() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(System.currentTimeMillis()));

    }

}
