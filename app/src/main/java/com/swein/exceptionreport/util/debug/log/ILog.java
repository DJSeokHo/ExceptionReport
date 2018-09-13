package com.swein.exceptionreport.util.debug.log;

import com.swein.exceptionreport.BuildConfig;
import com.swein.exceptionreport.util.debug.log.factroy.basiclog.BasicLog;
import com.swein.exceptionreport.util.debug.log.logclass.DebugILog;
import com.swein.exceptionreport.util.debug.log.logclass.ErrorILog;
import com.swein.exceptionreport.util.debug.log.logclass.InfoILog;
import com.swein.exceptionreport.util.debug.log.logclass.WarnILog;

/**
 *
 * Created by seokho on 13/12/2016.
 */

public class ILog {

    private static String HEAD = "[- ILog Print -] ";
    private static String TAG  = " ||===>> ";

    public static void iLogDebug( String tag, Object content ) {
        if( BuildConfig.DEBUG) {
            ILogFactory iLogFactory = new DebugILog();
            BasicLog basicLog    = iLogFactory.getBasicLog();
            basicLog.iLog( HEAD + TAG + tag, content );
        }
    }

    public static void iLogInfo(String tag, Object content) {
        if( BuildConfig.DEBUG) {
            ILogFactory iLogFactory = new InfoILog();
            BasicLog    basicLog    = iLogFactory.getBasicLog();
            basicLog.iLog( HEAD + TAG + tag, content );
        }
    }

    public static void iLogError(String tag, Object content) {
        if( BuildConfig.DEBUG) {
            ILogFactory iLogFactory = new ErrorILog();
            BasicLog    basicLog    = iLogFactory.getBasicLog();
            basicLog.iLog( HEAD + TAG + tag, content );
        }
    }

    public static void iLogWarn(String tag, Object content) {
        if( BuildConfig.DEBUG) {
            ILogFactory iLogFactory = new WarnILog();
            BasicLog    basicLog    = iLogFactory.getBasicLog();
            basicLog.iLog( HEAD + TAG + tag, content );
        }
    }
}
