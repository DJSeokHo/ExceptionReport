package com.swein.appanalysisreport.data.parser;

public class LoggerParser {

    public static String getLocationFromThrowable(Throwable throwable) {
        String location = "";

        if(throwable == null) {
            return location;
        }

        if(throwable.getStackTrace() == null) {

            location = throwable.toString();
            return location;
        }

        location += "[ " + throwable.getStackTrace()[0].getFileName() + " ]\n";
        location += "[ " + throwable.getStackTrace()[0].getMethodName() + " ]\n";
        location += "[ " + throwable.getStackTrace()[0].getLineNumber() + " ]";

        return location;
    }

    public static String getExceptionMessage(Throwable exception) {

        String exceptionMessage = "";

        Throwable temp = exception.getCause();

        if(temp == null) {
            /*
                try catch in thread
                exception.getCause() can be null
             */
            temp = exception;
        }

        StackTraceElement[] stackTraceElements = temp.getStackTrace();
        exceptionMessage += temp.getMessage() + "\n";
        for(StackTraceElement stackTraceElement : stackTraceElements) {
            exceptionMessage += stackTraceElement.toString() + "\n";
        }

        return exceptionMessage;
    }
}
