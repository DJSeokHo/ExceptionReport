package com.swein.exceptionreport.util.debug.log.factroy.logs;

import android.util.Log;

import com.swein.exceptionreport.util.debug.log.factroy.basiclog.BasicLog;


/**
 * Created by seokho on 19/04/2017.
 */

public class ErrorLog implements BasicLog {
    @Override
    public void iLog(String tag, Object content) {
        Log.e( tag, String.valueOf( content ) );
    }
}
