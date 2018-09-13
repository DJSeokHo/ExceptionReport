package com.swein.exceptionreport.application;

import android.app.Application;

import com.swein.exceptionreport.application.handler.CrashExceptionHandler;

public class ERApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashExceptionHandler.getInstance().init( getApplicationContext() );
    }
}
