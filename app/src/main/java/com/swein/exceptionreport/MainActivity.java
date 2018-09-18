package com.swein.exceptionreport;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.swein.exceptionreport.controller.ExceptionReportController;
import com.swein.exceptionreport.controller.gateway.email.ExceptionEmailGateway;
import com.swein.exceptionreport.util.dialog.DialogUtil;

import java.util.List;

public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";

    private Button buttonMakeCrashException;
    private Button buttonMakeNormalException;
    private Button buttonMakeAPIException;
    private Button buttonSendReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMakeCrashException = findViewById(R.id.buttonMakeCrashException);
        buttonMakeNormalException = findViewById(R.id.buttonMakeNormalException);
        buttonMakeAPIException = findViewById(R.id.buttonMakeAPIException);
        buttonSendReport = findViewById(R.id.buttonSendReport);

        buttonMakeCrashException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodMakeCrashException();
            }
        });

        buttonMakeNormalException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                methodMakeNormalException();

            }
        });

        buttonMakeAPIException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                methodMakeErrorResponse();

            }
        });

        buttonSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendExceptionReport();
            }
        });

    }

    private void sendExceptionReport() {

        final ExceptionReportController exceptionReportController = new ExceptionReportController(this, new ExceptionEmailGateway(this));

        DialogUtil.createNormalDialogWithTwoButton(MainActivity.this, "app name  오류 보고", "오류 정보를 이메일으로 개발자에게 보내시겠습니까?", false, "네", "아니요",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        exceptionReportController.sendReport();

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }

    private void methodMakeNormalException() {

        try {
            List list = null;
            list.get(0);
        }
        catch (Exception e) {

            final ExceptionReportController exceptionReportController = new ExceptionReportController(this, new ExceptionEmailGateway(this));
            exceptionReportController.setReport(e);

        }

    }

    private void methodMakeCrashException() {

        List list = null;
        list.get(0);

    }

    private void methodMakeErrorResponse() {

        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElements[0];

        final ExceptionReportController exceptionReportController = new ExceptionReportController(this, new ExceptionEmailGateway(this));
        exceptionReportController.setReport(stackTraceElement.getClassName(), String.valueOf(stackTraceElement.getLineNumber()), stackTraceElement.getMethodName(), "SystemOid is wrong");


    }

}
