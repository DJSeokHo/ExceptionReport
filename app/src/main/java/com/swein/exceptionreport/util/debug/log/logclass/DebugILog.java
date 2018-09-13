package com.swein.exceptionreport.util.debug.log.logclass;


import com.swein.exceptionreport.util.debug.log.ILogFactory;
import com.swein.exceptionreport.util.debug.log.factroy.basiclog.BasicLog;
import com.swein.exceptionreport.util.debug.log.factroy.logs.DebugLog;

/**
 * Created by seokho on 19/04/2017.
 */

public class DebugILog implements ILogFactory {
    @Override
    public BasicLog getBasicLog() {
        return new DebugLog();
    }
}
