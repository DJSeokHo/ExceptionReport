package com.swein.exceptionreport.applicationhandler.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.swein.exceptionreport.R;
import com.swein.exceptionreport.logger.Logger;
import com.swein.exceptionreport.loggerproperty.LoggerProperty;
import com.swein.exceptionreport.util.dialog.DialogUtil;

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
                        "리포트", "개인 정보 포함해서 같이 보내시겠습니까?", false, "같이 보내기", "취소",  "익명 보내기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppCrashReportActivity.this, false, LoggerProperty.TEST_USER_ID);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppCrashReportActivity.this, true, LoggerProperty.TEST_USER_ID);
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
