package com.swein.exceptionreport.util.debug.log;


import com.swein.exceptionreport.util.debug.log.factroy.basiclog.BasicLog;

/**
 * Created by seokho on 19/04/2017.
 */

public interface ILogFactory {
    BasicLog getBasicLog();
}
