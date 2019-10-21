package com.swein.appanalysisreport.application;

import android.app.Application;

import com.swein.appanalysisreport.logger.Logger;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;

public class ARApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.getInstance().init(getApplicationContext(), LoggerProperty.REPORT_RECORD_MANAGE_TYPE.ONE_MONTH);
    }

}
