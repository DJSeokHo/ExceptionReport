package com.swein.appanalysisreport.demo.example.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.swein.appanalysisreport.R;
import com.swein.appanalysisreport.constants.Constants;
import com.swein.appanalysisreport.data.parser.LoggerParser;
import com.swein.appanalysisreport.demo.example.home.AppAnalysisExampleHomeActivity;
import com.swein.appanalysisreport.logger.Logger;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;
import com.swein.appanalysisreport.util.dialog.DialogUtil;
import com.swein.appanalysisreport.util.thread.ThreadUtil;
import com.swein.appanalysisreport.util.toast.ToastUtil;

public class AppAnalysisExampleLoginActivity extends Activity {

    private EditText editTextID;
    private EditText editTextPassword;

    private Button buttonLogin;
    private Button buttonSendExceptionEmail;
    private Button buttonResetDB;

    private String operationRelateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_analysis_example_login);

        Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_CHANGE_SCREEN,
                LoggerProperty.OPERATION_TYPE.NONE,
                ""
        );

        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonSendExceptionEmail = findViewById(R.id.buttonSendExceptionEmail);
        buttonSendExceptionEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.createNormalDialogWithThreeButton(AppAnalysisExampleLoginActivity.this,
                        getString(R.string.alert_title), getString(R.string.alert_message), false, getString(R.string.send_with_id), getString(R.string.alert_cancel),  getString(R.string.send_without_id),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppAnalysisExampleLoginActivity.this, false, Constants.TEST_USER_ID);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppAnalysisExampleLoginActivity.this, true, Constants.TEST_USER_ID);
                            }
                        });
            }
        });

        buttonResetDB = findViewById(R.id.buttonResetDB);
        buttonResetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Logger.getInstance().clear();
            }
        });


        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Logger.getInstance().trackOperation(
                        LoggerParser.getLocationFromThrowable(new Throwable()),
                        LoggerProperty.EVENT_GROUP_LOGIN,
                        LoggerProperty.OPERATION_TYPE.C,
                        "click buttonLogin to login"
                );

                if(!checkInput()) {
                    ToastUtil.showShortToastNormal(AppAnalysisExampleLoginActivity.this, "check id or password");
                    return;
                }

                login();

            }
        });

    }

    private boolean checkInput() {

        operationRelateID = Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_LOGIN,
                LoggerProperty.OPERATION_TYPE.NONE,
                "check id and password"
        );


        if("".equals(editTextID.getText().toString()) || "".equals(editTextPassword.getText().toString())) {

            Logger.getInstance().trackException(
                    LoggerParser.getLocationFromThrowable(new Throwable()),
                    "id or password is null",
                    LoggerProperty.EVENT_GROUP_LOGIN,
                    operationRelateID,
                    ""
            );

            return false;
        }

        return true;
    }


    private void login() {

        operationRelateID = Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_LOGIN,
                LoggerProperty.OPERATION_TYPE.NONE,
                "login api started"
        );

        String id = editTextID.getText().toString().trim();;
        if(!Constants.TEST_USER_ID.equals(id)) {

            Logger.getInstance().trackException(
                    LoggerParser.getLocationFromThrowable(new Throwable()),
                    "id not right",
                    LoggerProperty.EVENT_GROUP_LOGIN,
                    operationRelateID,
                    ""
            );
            ToastUtil.showShortToastNormal(AppAnalysisExampleLoginActivity.this, "id not right");
            return;
        }

        String pw = editTextPassword.getText().toString().trim();
        if(!Constants.TEST_USER_PW.equals(pw)) {

            Logger.getInstance().trackException(
                    LoggerParser.getLocationFromThrowable(new Throwable()),
                    "password not right",
                    LoggerProperty.EVENT_GROUP_LOGIN,
                    operationRelateID,
                    ""
            );
            ToastUtil.showShortToastNormal(AppAnalysisExampleLoginActivity.this, "id not right");
            return;
        }


        ToastUtil.showShortToastNormal(AppAnalysisExampleLoginActivity.this, "login success");

        ThreadUtil.startThread(new Runnable() {

            @Override
            public void run() {

                ThreadUtil.startUIThread(2000, new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(AppAnalysisExampleLoginActivity.this, AppAnalysisExampleHomeActivity.class));

                    }
                });
            }
        });
    }


}
