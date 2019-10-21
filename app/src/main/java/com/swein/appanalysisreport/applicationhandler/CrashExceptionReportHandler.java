package com.swein.appanalysisreport.applicationhandler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.swein.appanalysisreport.applicationhandler.ui.AppCrashReportActivity;
import com.swein.appanalysisreport.data.parser.LoggerParser;


public class CrashExceptionReportHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = "CrashExceptionReportHandler";

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    @SuppressLint("StaticFieldLeak")
    private static CrashExceptionReportHandler instance = new CrashExceptionReportHandler();

    private Context context;

    private CrashExceptionReportHandler() {
    }

    public static CrashExceptionReportHandler getInstance() {
        return instance;
    }

    /**
     * init
     *
     * @param context context
     */
    public void init(Context context) {
        this.context = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * when uncaught exception
     *
     * @param thread    thread
     * @param exception exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {

        if (!handleException(exception) && uncaughtExceptionHandler != null) {

            uncaughtExceptionHandler.uncaughtException(thread, exception);
            exception.printStackTrace();
        }
        else {
            exception.printStackTrace();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AppCrashReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle bundle = new Bundle();
            bundle.putString("message", LoggerParser.getExceptionMessage(exception));
            bundle.putString("location", LoggerParser.getLocationFromThrowable(exception));
            intent.putExtra("bundle", bundle);

            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            System.gc();

        }
    }

    /**
     * custom defined exception handler
     *
     * @param exception exception
     * @return boolean
     */
    private boolean handleException(final Throwable exception) {
        if (null == exception) {
            return false;
        }

        /*
         * here is crash exception handler.
         * if app crash because exception from somewhere that you don't catch
         * here will go to send exception crash report view.
         */


        return true;

    }
}
