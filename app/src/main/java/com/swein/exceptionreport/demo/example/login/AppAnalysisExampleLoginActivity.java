package com.swein.exceptionreport.demo.example.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.swein.exceptionreport.R;
import com.swein.exceptionreport.data.parser.LoggerParser;
import com.swein.exceptionreport.demo.example.home.AppAnalysisExampleHomeActivity;
import com.swein.exceptionreport.logger.Logger;
import com.swein.exceptionreport.loggerproperty.LoggerProperty;
import com.swein.exceptionreport.util.dialog.DialogUtil;
import com.swein.exceptionreport.util.thread.ThreadUtil;
import com.swein.exceptionreport.util.toast.ToastUtil;

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

        editTextID.setText("sh");
        editTextPassword.setText("qq");

        buttonSendExceptionEmail = findViewById(R.id.buttonSendExceptionEmail);
        buttonSendExceptionEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.createNormalDialogWithThreeButton(AppAnalysisExampleLoginActivity.this,
                        "리포트", "개인 정보 포함해서 같이 보내시겠습니까?", false, "같이 보내기", "취소",  "익명 보내기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppAnalysisExampleLoginActivity.this, false, LoggerProperty.TEST_USER_ID);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.getInstance().sendAppAnalysisReportByEmail(AppAnalysisExampleLoginActivity.this, true, LoggerProperty.TEST_USER_ID);
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

                loginWithAPI();

            }
        });

        startActivity(new Intent(this, AppAnalysisExampleHomeActivity.class));
    }

    private boolean checkInput() {

        operationRelateID = Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_LOGIN,
                LoggerProperty.OPERATION_TYPE.NONE,
                "check input id and password"
        );


        if("".equals(editTextID.getText().toString()) || "".equals(editTextPassword.getText().toString()) || "ID".equals(editTextID.getText().toString()) || "Password".equals(editTextPassword.getText().toString())) {

            Logger.getInstance().trackException(
                    LoggerParser.getLocationFromThrowable(new Throwable()),
                    "input id or password was wrong",
                    LoggerProperty.EVENT_GROUP_LOGIN,
                    operationRelateID,
                    ""
            );

            return false;
        }

        return true;
    }


    private void loginWithAPI() {

        operationRelateID = Logger.getInstance().trackOperation(
                LoggerParser.getLocationFromThrowable(new Throwable()),
                LoggerProperty.EVENT_GROUP_LOGIN,
                LoggerProperty.OPERATION_TYPE.NONE,
                "login api started"
        );

        String id = editTextID.getText().toString();

        if(!"sh".equals(id)) {

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
