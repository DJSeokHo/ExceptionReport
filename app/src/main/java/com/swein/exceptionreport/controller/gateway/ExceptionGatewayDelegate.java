package com.swein.exceptionreport.controller.gateway;


import com.swein.exceptionreport.model.ExceptionModel;

public interface ExceptionGatewayDelegate {

    void setReport(ExceptionModel exceptionModel);
    void sendReport();

}
