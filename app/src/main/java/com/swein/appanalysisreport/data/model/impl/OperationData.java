package com.swein.appanalysisreport.data.model.impl;


import com.swein.appanalysisreport.data.model.AppAnalysisData;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;
import com.swein.appanalysisreport.util.date.DateUtil;
import com.swein.appanalysisreport.util.uuid.UUIDUtil;

public class OperationData implements AppAnalysisData {

    private final static String LOCATION_TITLE = "位置: ";
    private final static String DATE_TIME_TITLE = "时间: ";
    private final static String OPERATION_TYPE_TITLE = "操作类型: ";
    private final static String EVENT_GROUP_TITLE = "事件组: ";
    private final static String NOTE_TITLE = "备注: ";


    /* 记录的uuid */
    public String uuid = "";

    /* 位置 */
    public String location = "";

    /* 时间 */
    public String dateTime = "";

    /* 事件组 */
    public String eventGroup = "";

    /* 操作类型 */
    public LoggerProperty.OPERATION_TYPE operationType = LoggerProperty.OPERATION_TYPE.NONE;

    /* 备注 */
    public String note = "";

    public OperationData(String location, String eventGroup, LoggerProperty.OPERATION_TYPE operationType, String note) {
        this.uuid = UUIDUtil.getUUIDString();
        this.dateTime = DateUtil.getCurrentDateTimeString();
        this.location = location;
        this.eventGroup = eventGroup;
        this.operationType = operationType;
        this.note = note;
    }

    public OperationData(String uuid, String location, String dateTime, String eventGroup, LoggerProperty.OPERATION_TYPE operationType, String note) {
        this.uuid = uuid;
        this.location = location;
        this.dateTime = dateTime;
        this.eventGroup = eventGroup;
        this.operationType = operationType;
        this.note = note;
    }

    @Override
    public String toString() {
        return uuid + " " + location + " " + dateTime + " " + getOperationTypeString(operationType) + " " + eventGroup + " " + note;
    }

    @Override
    public String toReport() {

        return LOCATION_TITLE + location + "\n" +
                DATE_TIME_TITLE + dateTime + "\n" +
                OPERATION_TYPE_TITLE + getOperationTypeString(operationType) + "\n" +
                EVENT_GROUP_TITLE + eventGroup + "\n" +
                NOTE_TITLE + note;

    }

    /**
     * 根据 操作类别字符串 获取 操作类别
     */
    public static LoggerProperty.OPERATION_TYPE getOperationType(String operationTypeString) {
        switch (operationTypeString) {
            case LoggerProperty.OPERATION_CLICK:
                return LoggerProperty.OPERATION_TYPE.CLICK;

            case LoggerProperty.OPERATION_LONG_CLICK:
                return LoggerProperty.OPERATION_TYPE.LONG_CLICK;

            case LoggerProperty.OPERATION_SCROLL_UP:
                return LoggerProperty.OPERATION_TYPE.SCROLL_UP;

            case LoggerProperty.OPERATION_SCROLL_DOWN:
                return LoggerProperty.OPERATION_TYPE.SCROLL_DOWN;

            case LoggerProperty.OPERATION_NONE:

            default:
                return LoggerProperty.OPERATION_TYPE.NONE;
        }
    }

    /**
     * 根据 操作类别 获取 操作类别字符串
     */
    private String getOperationTypeString(LoggerProperty.OPERATION_TYPE operationType) {
        switch (operationType) {
            case CLICK:
                return LoggerProperty.OPERATION_CLICK;

            case LONG_CLICK:
                return LoggerProperty.OPERATION_LONG_CLICK;

            case SCROLL_DOWN:
                return LoggerProperty.OPERATION_SCROLL_DOWN;

            case SCROLL_UP:
                return LoggerProperty.OPERATION_SCROLL_UP;

            case NONE:

            default:
                return LoggerProperty.OPERATION_NONE;
        }
    }
}
