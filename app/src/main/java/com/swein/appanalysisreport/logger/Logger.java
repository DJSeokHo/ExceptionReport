package com.swein.appanalysisreport.logger;

import android.annotation.SuppressLint;
import android.content.Context;

import com.swein.appanalysisreport.R;
import com.swein.appanalysisreport.applicationhandler.CrashExceptionReportHandler;
import com.swein.appanalysisreport.data.db.AppAnalysisReportDBController;
import com.swein.appanalysisreport.data.model.AppAnalysisData;
import com.swein.appanalysisreport.data.model.impl.DeviceUserData;
import com.swein.appanalysisreport.data.model.impl.ExceptionData;
import com.swein.appanalysisreport.data.model.impl.OperationData;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;
import com.swein.appanalysisreport.util.appinfo.AppInfoUtil;
import com.swein.appanalysisreport.util.device.DeviceInfoUtil;
import com.swein.appanalysisreport.util.email.EmailUtil;
import com.swein.appanalysisreport.util.queuemanager.QueueManager;
import com.swein.appanalysisreport.util.thread.ThreadUtil;
import com.swein.appanalysisreport.util.toast.ToastUtil;
import com.swein.appanalysisreport.util.uuid.Installation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * how to check report in your SQLite Client
 *
 *
SELECT
TB_OPERATION_REPORT.*,
TB_EXCEPTION_REPORT.DATE_TIME, TB_EXCEPTION_REPORT.LOCATION, TB_EXCEPTION_REPORT.EVENT_GROUP, TB_EXCEPTION_REPORT.MESSAGE, TB_EXCEPTION_REPORT.NOTE
FROM
TB_OPERATION_REPORT
LEFT JOIN TB_EXCEPTION_REPORT
ON
TB_OPERATION_REPORT.UUID = TB_EXCEPTION_REPORT.OPERATION_RELATE_ID;
 *
 */
public class Logger {

    private AppAnalysisReportDBController appAnalysisReportDBController;
    private Context context;

    private Logger() {}

    @SuppressLint("StaticFieldLeak")
    private static Logger instance = new Logger();

    public static Logger getInstance() {
        return instance;
    }

    /**
     * add this method to your application
     * @param context context
     */
    public void init(Context context, LoggerProperty.REPORT_RECORD_MANAGE_TYPE recordLimit) {
        this.context = context;
        CrashExceptionReportHandler.getInstance().init(context);

        if(appAnalysisReportDBController == null) {
            appAnalysisReportDBController = new AppAnalysisReportDBController(context);
        }

        saveAppAnalysisIntoDB(new DeviceUserData(
                Installation.id(context),
                DeviceInfoUtil.getDeviceModel(),
                DeviceInfoUtil.getDeviceOSVersion(),
                AppInfoUtil.getPackageName(context),
                AppInfoUtil.getVersionName(context),
                ""
        ));

        checkReportRecord(recordLimit);
    }


    public void trackException(String location, String exceptionMessage, String eventGroup, String operationRelateID, String note) {

        if("".equals(operationRelateID)) {

            saveAppAnalysisIntoDB(new ExceptionData(
                    location, exceptionMessage, eventGroup,appAnalysisReportDBController.getLastOperationUUID(), note
            ));

            return;
        }

        saveAppAnalysisIntoDB(new ExceptionData(
                location, exceptionMessage, eventGroup, operationRelateID, note
        ));
    }

    public String trackOperation(String location, String eventGroup, LoggerProperty.OPERATION_TYPE operationType, String note) {
        AppAnalysisData appAnalysisData = new OperationData(
                location, eventGroup, operationType, note
        );
        saveAppAnalysisIntoDB(appAnalysisData);

        return ((OperationData) appAnalysisData).uuid;
    }

    public void sendAppAnalysisReportByEmail(Context context, boolean anonymous, String userID) {

        ThreadUtil.startThread(new Runnable() {
            @Override
            public void run() {

                try {

                    File folder = new File(LoggerProperty.REPORT_FILE_PATH);
                    if(!folder.exists()) {
                        folder.mkdir();
                    }

                    File file = new File(LoggerProperty.REPORT_FILE_PATH, LoggerProperty.REPORT_FILE_NAME);
                    if(file.exists()) {
                        boolean success = file.delete();
                        if(!success) {
                            return;
                        }
                    }

                    try {
                        file.createNewFile();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }


                    FileOutputStream fileOutputStream;
                    BufferedWriter bufferedWriter;

                    fileOutputStream = new FileOutputStream(file);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                    List<DeviceUserData> deviceUserDataList = appAnalysisReportDBController.getDeviceUserDataList();
                    for(DeviceUserData deviceUserData : deviceUserDataList) {
                        bufferedWriter.write(deviceUserData.toReport());
                    }

                    List<OperationData> operationDataList = appAnalysisReportDBController.getOperationDataList();
                    for(OperationData operationData : operationDataList) {
                        bufferedWriter.write(operationData.toReport());
                    }

                    List<ExceptionData> exceptionDataList = appAnalysisReportDBController.getExceptionDataList();
                    for(ExceptionData exceptionData : exceptionDataList) {
                        bufferedWriter.write(exceptionData.toReport());
                    }

                    bufferedWriter.close();
                    fileOutputStream.close();



                    ThreadUtil.startUIThread(0, new Runnable() {
                        @Override
                        public void run() {

                            if(operationDataList.isEmpty() && exceptionDataList.isEmpty()) {
                                ToastUtil.showShortToastNormal(context, context.getString(R.string.report_is_null));
                                return;
                            }

                            String title = LoggerProperty.APP_ANALYSIS_REPORT_TITLE;

                            String content;

                            if(anonymous) {
                                content = LoggerProperty.APP_ANALYSIS_REPORT_CONTENT;
                            }
                            else {
                                content = userID + " " + LoggerProperty.APP_ANALYSIS_REPORT_CONTENT;
                            }

                            EmailUtil.mailToWithFile(context, file, new String[]{LoggerProperty.EMAIL_RECEIVER, LoggerProperty.EMAIL_RECEIVER},
                                    title, content);

                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void saveAppAnalysisIntoDB(final AppAnalysisData appAnalysisData) {

        QueueManager.getInstance().addTask(new Runnable() {

            @Override
            public void run() {

                if(appAnalysisData instanceof DeviceUserData) {
                    appAnalysisReportDBController.insertDeviceUser((DeviceUserData) appAnalysisData);
                }
                else if(appAnalysisData instanceof OperationData) {
                    appAnalysisReportDBController.insertOperation((OperationData) appAnalysisData);
                }
                else if(appAnalysisData instanceof ExceptionData) {
                    appAnalysisReportDBController.insertException((ExceptionData) appAnalysisData);
                }
            }
        });
    }

    public void clear() {

        if(appAnalysisReportDBController == null) {
            appAnalysisReportDBController = new AppAnalysisReportDBController(context);
        }

        appAnalysisReportDBController.clearDataBase();

    }

    private void checkReportRecord(LoggerProperty.REPORT_RECORD_MANAGE_TYPE reportRecordManageType) {

        switch (reportRecordManageType) {

            case TODAY:
                /* 删除一天之前的记录 */
                deleteInDateTimeRange(1);
                break;

            default:

            case ONE_WEEK:
                /* 删除一周之前的记录 */
                deleteInDateTimeRange(7);
                break;

            case ONE_MONTH:
                /* 删除一个月之前的记录 */
                deleteInDateTimeRange(30);
                break;

            case RECORD_MAX_ONE_K:
                /* 记录只保留1000条，超过时自动删除最早的记录 */
                deleteInRecordTotalNumberRange(1000);
                break;

            case RECORD_MAX_FIVE_K:
                /* 记录只保留5000条，超过时自动删除最早的记录 */
                deleteInRecordTotalNumberRange(5000);
                break;

            case RECORD_MAX_TEN_K:
                /* 记录只保留10000条，超过时自动删除最早的记录 */
                deleteInRecordTotalNumberRange(10000);
                break;

            case FOR_TEST:
                /* 推荐组合 */
                deleteInDateTimeRange(1);
                deleteInRecordTotalNumberRange(10000);

                break;
        }
    }

    private void deleteInDateTimeRange(int day) {
        appAnalysisReportDBController.deleteExceptionInDateTimeRange(day);
        appAnalysisReportDBController.deleteOperationInDateTimeRange(day);
    }

    private void deleteInRecordTotalNumberRange(int totalNumber) {
        appAnalysisReportDBController.deleteExceptionInRecordTotalNumberRange(totalNumber);
        appAnalysisReportDBController.deleteOperationInRecordTotalNumberRange(totalNumber);
    }

}
