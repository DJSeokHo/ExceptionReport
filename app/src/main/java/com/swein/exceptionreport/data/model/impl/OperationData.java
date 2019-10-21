package com.swein.exceptionreport.data.model.impl;


import com.swein.exceptionreport.data.model.AppAnalysisData;
import com.swein.exceptionreport.loggerproperty.LoggerProperty;
import com.swein.exceptionreport.util.date.DateUtil;
import com.swein.exceptionreport.util.uuid.UUIDUtil;

public class OperationData implements AppAnalysisData {

    /* uuid */
    private String uuid = "";

    /* class file name */
    /* 클릭한 뷰 이름 */
    private String location = "";

    /* 시간 */
    private String dateTime = "";

    /* 이벤트 그룹 */
    private String eventGroup = "";

    private LoggerProperty.OPERATION_TYPE operationType = LoggerProperty.OPERATION_TYPE.NONE;

    /* 기타정보 */
    private String note = "";

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

    public String getUuid() {
        return uuid;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getOperationType() {
        return getOperationTypeString(operationType);
    }

    public String getEventGroup() {
        return eventGroup;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return uuid + " " + location + " " + dateTime + " " + getOperationTypeString(operationType) + " " + eventGroup + " " + note;
    }

    private final static String LOCATION_KEY = "위치: ";
    private final static String DATE_TIME_KEY = "시간: ";
    private final static String OPERATION_TYPE_KEY = "행동 방식: ";
    private final static String EVENT_GROUP_KEY = "이벤트 그룹: ";
    private final static String NOTE_KEY = "비고: ";

    @Override
    public String toReport() {

        return LOCATION_KEY + location + "\n" +
                DATE_TIME_KEY + dateTime + "\n" +
                OPERATION_TYPE_KEY + getOperationTypeString(operationType) + "\n" +
                EVENT_GROUP_KEY + eventGroup + "\n" +
                NOTE_KEY + note;

    }

    public static LoggerProperty.OPERATION_TYPE getOperationType(String operationTypeString) {
        switch (operationTypeString) {
            case LoggerProperty.OPERATION_C:
                return LoggerProperty.OPERATION_TYPE.C;

            case LoggerProperty.OPERATION_LC:
                return LoggerProperty.OPERATION_TYPE.LC;

            case LoggerProperty.OPERATION_SU:
                return LoggerProperty.OPERATION_TYPE.SU;

            case LoggerProperty.OPERATION_SD:
                return LoggerProperty.OPERATION_TYPE.SD;

            case LoggerProperty.OPERATION_NONE:

            default:
                return LoggerProperty.OPERATION_TYPE.NONE;
        }
    }

    private String getOperationTypeString(LoggerProperty.OPERATION_TYPE operationType) {
        switch (operationType) {
            case C:
                return LoggerProperty.OPERATION_C;

            case LC:
                return LoggerProperty.OPERATION_LC;

            case SD:
                return LoggerProperty.OPERATION_SD;

            case SU:
                return LoggerProperty.OPERATION_SU;

            case NONE:

            default:
                return LoggerProperty.OPERATION_NONE;
        }
    }
}
