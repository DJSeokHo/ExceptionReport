package com.swein.exceptionreport.model;

public class ExceptionModel {

    private final static String APP_VERSION_KEY = "앱 버전: ";
    private final static String PHONE_MODEL_KEY = "폰 모델명: ";
    private final static String USER_ID_KEY = "사용자 계정: ";
    private final static String DATE_TIME_KEY = "오류 발생 일시: ";
    private final static String CLASS_FILE_NAME_KEY = "오류 발생한 파일: ";
    private final static String METHOD_NAME_KEY = "오류 발생한 메소드: ";
    private final static String LINE_NUMBER_KEY = "오류 발생한 라인: ";
    private final static String MESSAGE_KEY = "오류 발생한 정보: ";

    
    /* 앱 버전 */
    private String appVersion;

    /* 폰 model 명 */
    private String phoneModel;

    /* 사용자 ID */
    private String userID;

    /* 시간 */
    private String dateTime;

    /* 오류 발생하는 파일명 */
    private String classFileName;

    /* 오류 발생하는 메소드명 */
    private String methodName;

    /* 오류 발생하는 line 번호 */
    private String lineNumber;

    /* 오류 메시지 */
    private String exceptionMessage;

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setClassFileName(String classFileName) {
        this.classFileName = classFileName;
    }

    public String getClassFileName() {
        return classFileName;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return appVersion + " " + phoneModel + " " + userID + " " + dateTime + " " + classFileName + " " + methodName + " " + lineNumber + " " + exceptionMessage;
    }



    public String toReportString() {

        return APP_VERSION_KEY + appVersion + "\n" +
                PHONE_MODEL_KEY + phoneModel + "\n" +
                USER_ID_KEY + userID + "\n" +
                DATE_TIME_KEY + dateTime + "\n" +
                CLASS_FILE_NAME_KEY + classFileName + "\n" +
                METHOD_NAME_KEY + methodName + "\n" +
                LINE_NUMBER_KEY + lineNumber + "\n" +
                MESSAGE_KEY + exceptionMessage;

    }


}
