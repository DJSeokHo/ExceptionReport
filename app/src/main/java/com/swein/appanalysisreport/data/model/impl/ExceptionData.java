package com.swein.appanalysisreport.data.model.impl;

import com.swein.appanalysisreport.data.model.AppAnalysisData;
import com.swein.appanalysisreport.util.date.DateUtil;
import com.swein.appanalysisreport.util.uuid.UUIDUtil;

public class ExceptionData implements AppAnalysisData {

    private final static String DATE_TIME_TITLE = "异常发生的时间: ";
    private final static String LOCATION_TITLE = "异常发生的位置: ";
    private final static String MESSAGE_TITLE = "异常信息: ";
    private final static String EVENT_GROUP_TITLE = "事件组: ";
    private final static String OPERATION_RELATE_ID_TITLE = "用户行为: ";
    private final static String NOTE_TITLE = "备注: ";

    /* 记录的uuid */
    public String uuid = "";

    /* 时间 */
    public String dateTime = "";

    /* 异常发生的文件名，方法名，行号 */
    public String location = "";

    /* 异常信息 */
    public String exceptionMessage = "";

    /* 事件组 */
    public String eventGroup = "";

    /* 这条异常发生时的相关用户操作 */
    public String operationRelateID = "";

    /* 备注 */
    public String note = "";

    public ExceptionData(String location, String exceptionMessage, String eventGroup, String operationRelateID, String note) {
        this.uuid = UUIDUtil.getUUIDString();
        this.dateTime = DateUtil.getCurrentDateTimeString();
        this.location = location;
        this.exceptionMessage = exceptionMessage;
        this.eventGroup = eventGroup;
        this.operationRelateID = operationRelateID;
        this.note = note;
    }

    public ExceptionData(String uuid, String dateTime, String location, String exceptionMessage, String eventGroup, String operationRelateID, String note) {
        this.uuid = uuid;
        this.dateTime = dateTime;
        this.location = location;
        this.exceptionMessage = exceptionMessage;
        this.eventGroup = eventGroup;
        this.operationRelateID = operationRelateID;
        this.note = note;
    }

    @Override
    public String toString() {
        return uuid + " " + dateTime + " " + location + " " + exceptionMessage + " " + eventGroup + " " + operationRelateID + " " + note;
    }

    @Override
    public String toReport() {

        return DATE_TIME_TITLE + dateTime + "\n" +
                LOCATION_TITLE + location + "\n" +
                MESSAGE_TITLE + exceptionMessage + "\n" +
                NOTE_TITLE + note + "\n" +
                OPERATION_RELATE_ID_TITLE + operationRelateID + "\n" +
                EVENT_GROUP_TITLE + eventGroup;
    }

}
