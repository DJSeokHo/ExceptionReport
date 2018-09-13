package com.swein.exceptionreport.util.debug.time;


import com.swein.exceptionreport.util.debug.log.ILog;

/**
 * Created by seokho on 13/06/2017.
 */

public class DebugTime {

    private long startTime;
    private long endTime;
    private String methodName;

    private DebugTime() {}

    private static DebugTime instance = new DebugTime();

    public static DebugTime getInstance() {

        return instance;
    }

    public void setStart(String methodName) {
        startTime = System.currentTimeMillis();
        this.methodName = methodName;
    }

    public void setStop() {
        endTime = System.currentTimeMillis();

        long time = endTime - startTime;
        if(time > 1000) {
            ILog.iLogDebug(this.methodName, "running time : " + time / 1000 + "s");
        }
        else {
            ILog.iLogDebug(this.methodName, " running time : " + (endTime - startTime) + "ms");
        }
    }
}
