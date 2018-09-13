package com.swein.exceptionreport.util.debug.log.logclass;


import com.swein.exceptionreport.util.debug.log.ILogFactory;
import com.swein.exceptionreport.util.debug.log.factroy.basiclog.BasicLog;
import com.swein.exceptionreport.util.debug.log.factroy.logs.ErrorLog;

/**
 * Created by seokho on 19/04/2017.
 */

public class ErrorILog implements ILogFactory {
    @Override
    public BasicLog getBasicLog() {
        return new ErrorLog();
    }
}
