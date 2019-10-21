package com.swein.appanalysisreport.loggerproperty;

import android.os.Environment;

public class LoggerProperty {

    public final static int SECONDS_IN_DAY = 86400;

    public final static String APP_ANALYSIS_REPORT_TITLE = "app 分析日志";
    public final static String APP_ANALYSIS_REPORT_CONTENT = "用户反馈的日志";

    public final static String DB_FILE_TEMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.AppNameAnalysisReportTemp/";
    public final static String DB_FILE_TEMP_NAME = "AppNameAppAnalysisReport.db";

    // 接收app 分析日志的开发的个人邮箱或者公司邮箱，这里换成你的即可
    public final static String EMAIL_RECEIVER = "djseokho@gmail.com";

    /*
     * 你可以在这里添加自己的事件组
     * 什么是事件组?
     *
     * 例:
     * 用户登录事件可以加入到 "LOGIN" 事件组
     * 用户注册事件可以加入到 "REGISTER" 事件组
     *
     * 又或者用户登录，注册可以加入到 "USER" 事件组
     *
     * 要怎么划分，全部按需求来自定义即可
     * =================================================
     */
    public final static String EVENT_GROUP_CRASH = "crash";
    public final static String EVENT_GROUP_LOGIN = "login";

    public final static String EVENT_GROUP_CHANGE_SCREEN = "change screen";
    public final static String EVENT_GROUP_REQUEST_API = "request api";

    public final static String EVENT_GROUP_AUTO_RUN_METHOD = "auto run method";

    // =================================================






    /*
     * 在这里你可以添加自定义操作
     * 比如，用户点击 可以是 OPERATION_CLICK，或者你可以用你想用的名字
     *
     * 每一个操作又3个部分组成
     * 静态常量名，静态常量值，以及枚举值
     * =================================================
     */
    public final static String OPERATION_C = "click";
    public final static String OPERATION_LC = "long click";
    public final static String OPERATION_SU = "scroll up";
    public final static String OPERATION_SD = "scroll down";
    public final static String OPERATION_NONE = "none";


    public final static String OPERATION_LEAVE_SCREEN = "leave screen";
    public final static String OPERATION_ENTER_SCREEN = "enter screen";


    public final static String OPERATION_BACKGROUND = "background";
    public final static String OPERATION_FOREGROUND = "foreground";


    public enum OPERATION_TYPE {
        /*
            NONE: none (default)
            SU: Scroll Up
            SD: Scroll Down
            C: Click
            LC: Long Click
        */
        SU, SD, C, LC, NONE
    }
    // =================================================


    /*

        数据库里，记录条数的限制条件

        ONE_WEEK:

        ex:
        DELETE FROM TB_EXCEPTION_REPORT
        WHERE TB_EXCEPTION_REPORT.UUID IN (SELECT TB_EXCEPTION_REPORT.UUID FROM TB_EXCEPTION_REPORT
        WHERE strftime('%s','now') - strftime('%s', TB_EXCEPTION_REPORT.DATE_TIME) > (86400 * 7));

        DELETE FROM TB_OPERATION_REPORT
        WHERE TB_OPERATION_REPORT.UUID IN (SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT
        WHERE strftime('%s','now') - strftime('%s', TB_OPERATION_REPORT.DATE_TIME) > (86400 * 7));


        RECORD_MAX_FIVE_K:

        ex:
        DELETE FROM TB_OPERATION_REPORT WHERE
        (SELECT COUNT(TB_OPERATION_REPORT.UUID) FROM TB_OPERATION_REPORT
        ) > 5000 AND TB_OPERATION_REPORT.UUID IN
        (SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT
        ORDER BY TB_OPERATION_REPORT.DATE_TIME DESC LIMIT
        (SELECT COUNT(TB_OPERATION_REPORT.UUID) FROM TB_OPERATION_REPORT) OFFSET 5000 );

        DELETE FROM TB_EXCEPTION_REPORT WHERE
        (SELECT COUNT(TB_EXCEPTION_REPORT.UUID) FROM TB_EXCEPTION_REPORT
        ) > 5000 AND TB_EXCEPTION_REPORT.UUID IN
        (SELECT TB_EXCEPTION_REPORT.UUID FROM TB_EXCEPTION_REPORT
        ORDER BY TB_EXCEPTION_REPORT.DATE_TIME DESC LIMIT
        (SELECT COUNT(TB_EXCEPTION_REPORT.UUID) FROM TB_EXCEPTION_REPORT) OFFSET 5000 );
     */
    public enum REPORT_RECORD_MANAGE_TYPE {
        TODAY, ONE_WEEK, ONE_MONTH, RECORD_MAX_ONE_K, RECORD_MAX_FIVE_K, RECORD_MAX_TEN_K, FOR_TEST
    }

}
