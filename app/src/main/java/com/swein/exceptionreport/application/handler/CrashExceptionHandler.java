package com.swein.exceptionreport.application.handler;

import android.content.Context;
import android.os.Looper;

import com.swein.exceptionreport.constants.EConstants;
import com.swein.exceptionreport.controller.ExceptionReportController;

/**
 * Created by seokho on 23/11/2016.
 */

public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = "CrashExceptionHandler";

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private static CrashExceptionHandler INSTANCE = new CrashExceptionHandler();

    private Context context;

    private CrashExceptionHandler() {}

    public static CrashExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * init
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * when uncaught exception
     * @param thread
     * @param exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {

        if ( !handleException(exception) && uncaughtExceptionHandler != null ) {

            uncaughtExceptionHandler.uncaughtException(thread, exception);
        }
        else {

            try {
                Thread.sleep(2000);
            }
            catch ( InterruptedException e ) {
                e.printStackTrace();
            }

            //exit
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * custom defined exception handler
     * @param exception
     * @return
     */
    private boolean handleException(Throwable exception) {
        if( null == exception ) {
            return false;
        }

        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                Looper.loop();
            }
        }.start();

        /**
         * here is crash exception handler.
         * if app crash because exception from somewhere that you don't catch
         * here will send exception crash report.
         */

        ExceptionReportController exceptionReportController = new ExceptionReportController(context, EConstants.REPORT_WAY.EMAIL);
        exceptionReportController.setReport(exception);

        return true;

    }
}
