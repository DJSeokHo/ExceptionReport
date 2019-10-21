package com.swein.appanalysisreport.applicationhandler.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.swein.appanalysisreport.R;
import com.swein.appanalysisreport.constants.Constants;
import com.swein.appanalysisreport.logger.Logger;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;
import com.swein.appanalysisreport.util.dialog.DialogUtil;

public class AppCrashReportActivity extends Activity {

    private final static String TAG = "AppCrashReportActivity";

    private Button buttonExit;
    private Button buttonSendExceptionEmail;

    private Button buttonResetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_crash_report);


        String message = getIntent().getStringExtra("message");
        String location = getIntent().getStringExtra("location");

        Logger.getInstance().trackException(location, message, LoggerProperty.EVENT_GROUP_CRASH, "", LoggerProperty.EVENT_GROUP_CRASH);

        buttonExit = findViewById(R.id.buttonExit);
        buttonSendExceptionEmail = findViewById(R.id.buttonSendExceptionEmail);

        buttonResetDB = findViewById(R.id.buttonResetDB);
        buttonResetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.getInstance().clear();
            }
        });

        buttonSendExceptionEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.createNormalDialogWithThreeButton(AppCrashReportActivity.this,
                        getString(R.string.alert_title), getString(R.string.alert_message), false, getString(R.string.send_with_id), getString(R.string.alert_cancel),  getString(R.string.send_without_id),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppCrashReportActivity.this, false, Constants.TEST_USER_ID);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppCrashReportActivity.this, true, Constants.TEST_USER_ID);
                            }
                        });

            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
