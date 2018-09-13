package com.swein.exceptionreport.controller;

import android.content.Context;

import com.swein.exceptionreport.controller.gateway.ExceptionGatewayDelegate;
import com.swein.exceptionreport.model.ExceptionModel;
import com.swein.exceptionreport.util.appinfo.AppInfoUtil;
import com.swein.exceptionreport.util.date.DateUtil;
import com.swein.exceptionreport.util.device.DeviceInfoUtil;

import java.util.Arrays;

public class ExceptionReportController {

    private final static String TAG = "ExceptionReportController";

    private ExceptionGatewayDelegate exceptionGatewayDelegate;
    private Context context;

    public ExceptionReportController(Context context, ExceptionGatewayDelegate exceptionGatewayDelegate) {

        this.context = context;
        this.exceptionGatewayDelegate = exceptionGatewayDelegate;

    }

    public void setReport(Throwable e) {

        ExceptionModel exceptionModel = new ExceptionModel();

        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        exceptionModel.setAppVersion(String.valueOf(AppInfoUtil.getVersionCode(context)));
        exceptionModel.setPhoneModel(DeviceInfoUtil.getDeviceModel());
        exceptionModel.setClassFileName(stackTraceElement.getFileName());
        exceptionModel.setDateTime(DateUtil.getCurrentDateTimeString());
        exceptionModel.setLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
        exceptionModel.setMethodName(stackTraceElement.getMethodName());
        exceptionModel.setUserID("user01");

        exceptionModel.setExceptionMessage(Arrays.toString(e.getStackTrace()));

        exceptionGatewayDelegate.setReport(exceptionModel);

    }

    public void setReport(Exception e) {

        ExceptionModel exceptionModel = new ExceptionModel();

        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        exceptionModel.setAppVersion(String.valueOf(AppInfoUtil.getVersionCode(context)));
        exceptionModel.setPhoneModel(DeviceInfoUtil.getDeviceModel());
        exceptionModel.setClassFileName(stackTraceElement.getFileName());
        exceptionModel.setDateTime(DateUtil.getCurrentDateTimeString());
        exceptionModel.setLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
        exceptionModel.setMethodName(stackTraceElement.getMethodName());
        exceptionModel.setUserID("user01");

        exceptionModel.setExceptionMessage(Arrays.toString(e.getStackTrace()));

        exceptionGatewayDelegate.setReport(exceptionModel);

    }

    public void setReport(String classFileName, String lineNumber, String methodName, String response) {

        ExceptionModel exceptionModel = new ExceptionModel();

        exceptionModel.setAppVersion(String.valueOf(AppInfoUtil.getVersionCode(context)));
        exceptionModel.setPhoneModel(DeviceInfoUtil.getDeviceModel());
        exceptionModel.setClassFileName(classFileName);
        exceptionModel.setDateTime(DateUtil.getCurrentDateTimeString());
        exceptionModel.setLineNumber(lineNumber);
        exceptionModel.setMethodName(methodName);
        exceptionModel.setUserID("user01");

        exceptionModel.setExceptionMessage(response);

        exceptionGatewayDelegate.setReport(exceptionModel);

    }

    public void sendReport() {
        exceptionGatewayDelegate.sendReport();
    }



}
