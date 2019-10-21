package com.swein.exceptionreport.loggerproperty;

import android.os.Environment;

public class LoggerProperty {

    public final static int SECONDS_IN_DAY = 86400;

    public final static String APP_ANALYSIS_REPORT_TITLE = "오류 리포트";
    public final static String APP_ANALYSIS_REPORT_CONTENT = "사용자 보낸 리포트입니다.";
    public final static String TEST_USER_ID = "user01";

    public final static String DB_FILE_TEMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.AppNameAnalysisReportTemp/";
    public final static String DB_FILE_TEMP_NAME = "AppNameAppAnalysisReport.db";
    public final static String EMAIL_RECEIVER = "djseokho@gmail.com";
    public final static String ANONYMOUS_USER_TEMP_KEY = "app_anonymous_user";

    /*
     * you can add your own event group here
     * what is event group ?
     *
     * ex:
     * login events can join group "LOGIN"
     * register events can join group "REGISTER"
     *
     * you can custom group by your self
     * =================================================
     */
    public final static String EVENT_GROUP_CRASH = "crash";
    public final static String EVENT_GROUP_LOGIN = "login";
    public final static String EVENT_GROUP_REGISTER = "register";

    public final static String EVENT_GROUP_CHANGE_SCREEN = "change screen";
    public final static String EVENT_GROUP_REQUEST_API = "request api";
    public final static String EVENT_GROUP_WEB_VIEW = "web view";

    public final static String EVENT_GROUP_AUTO_RUN_METHOD = "auto run method";

    public final static String EVENT_GROUP_ACTIVITY_STATE = "activity state";

    // =================================================






    /*
     * you can add your own custom operation here
     * =================================================
     */
    public final static String OPERATION_C = "click";
    public final static String OPERATION_LC = "long click";
    public final static String OPERATION_SU = "scroll up";
    public final static String OPERATION_SD = "scroll down";
    public final static String OPERATION_NONE = "none";

    /**
     * @see {@link #EVENT_GROUP_CHANGE_SCREEN }
     */
    public final static String OPERATION_LEAVE_SCREEN = "leave screen";
    public final static String OPERATION_ENTER_SCREEN = "enter screen";

    /**
     * @see {@link #EVENT_GROUP_ACTIVITY_STATE }
     */
    public final static String OPERATION_BACKGROUND = "background";
    public final static String OPERATION_FOREGROUND = "foreground";


    public enum OPERATION_TYPE {
        /*
            행동 유형 자유롭게 추가 가능

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
        /*
            ONE_WEEK: 최근 1주 동안 기록만 저장 (default)
            ONE_MONTH: 최근 1개월 동안 기록만 저장
            RECORD_MAX_5000: 최대 5000 개 기록 저장
            RECORD_MAX_10000: 최대 10000 개 기록 저장
        */
        TODAY, ONE_WEEK, ONE_MONTH, RECORD_MAX_ONE_K, RECORD_MAX_FIVE_K, RECORD_MAX_TEN_K, FOR_TEST
    }

}
