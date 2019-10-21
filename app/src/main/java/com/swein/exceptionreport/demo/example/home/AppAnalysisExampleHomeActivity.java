package com.swein.exceptionreport.demo.example.home;

import android.app.Activity;
import android.os.Bundle;

import com.swein.exceptionreport.R;
import com.swein.exceptionreport.data.parser.LoggerParser;
import com.swein.exceptionreport.logger.Logger;
import com.swein.exceptionreport.loggerproperty.LoggerProperty;
import com.swein.exceptionreport.util.debug.log.ILog;
import com.swein.exceptionreport.util.thread.ThreadUtil;
import com.swein.exceptionreport.util.toast.ToastUtil;

public class AppAnalysisExampleHomeActivity extends Activity {

    private final static String TAG = "AppAnalysisExampleHomeActivity";

    private String operationRelateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_analysis_example_home);


        Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_CHANGE_SCREEN,
                LoggerProperty.OPERATION_TYPE.NONE,
                ""
        );

        autoStartSomeMethod();

        ThreadUtil.startUIThread(2000, new Runnable() {
            @Override
            public void run() {

                finish();
            }
        });
    }

    private void autoStartSomeMethod() {

        operationRelateID = Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_AUTO_RUN_METHOD,
                LoggerProperty.OPERATION_TYPE.NONE,
                ""
        );

        ThreadUtil.startThread(new Runnable() {

            @Override
            public void run() {

                try {
                    int i = 5/0;
                    ILog.iLogDebug(TAG, i);
                }
                catch (Throwable throwable) {

                    Logger.getInstance().trackException(
                            LoggerParser.getLocationFromThrowable(throwable),
                            LoggerParser.getExceptionMessage(throwable),
                            LoggerProperty.EVENT_GROUP_REQUEST_API,
                            operationRelateID,
                            "check api success or not"
                    );

                    ThreadUtil.startUIThread(100, new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToastNormal(AppAnalysisExampleHomeActivity.this, "초기화 오류");
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void onBackPressed() {

        return;
    }
}
