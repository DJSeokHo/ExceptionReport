package com.swein.exceptionreport.application;

import android.app.Application;

import com.swein.exceptionreport.logger.Logger;
import com.swein.exceptionreport.loggerproperty.LoggerProperty;

public class ERApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.getInstance().init(getApplicationContext(), LoggerProperty.REPORT_RECORD_MANAGE_TYPE.ONE_MONTH);
    }

    @Override
    protected void finalize() throws Throwable {
        Logger.getInstance().close();
        super.finalize();
    }
}
