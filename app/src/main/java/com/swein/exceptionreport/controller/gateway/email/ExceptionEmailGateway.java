package com.swein.exceptionreport.controller.gateway.email;

import android.content.Context;

import com.swein.exceptionreport.constants.EConstants;
import com.swein.exceptionreport.controller.gateway.ExceptionGatewayDelegate;
import com.swein.exceptionreport.database.ERSQLiteController;
import com.swein.exceptionreport.model.ExceptionModel;
import com.swein.exceptionreport.util.debug.log.ILog;
import com.swein.exceptionreport.util.email.EmailUtil;

import java.util.List;

public class ExceptionEmailGateway implements ExceptionGatewayDelegate {

    private final static String TAG = "ExceptionEmailGateway";

    private Context context;

    public ExceptionEmailGateway(Context context) {
        this.context = context;
    }

    @Override
    public void setReport(ExceptionModel exceptionModel) {

        ERSQLiteController ersqLiteController = new ERSQLiteController(context);
        ersqLiteController.insert(exceptionModel);
    }

    @Override
    public void sendReport() {

        ERSQLiteController ersqLiteController = new ERSQLiteController(context);
        List<ExceptionModel> exceptionModelList = ersqLiteController.getData(0, 30);

        if(0 == exceptionModelList.size()) {

            return;
        }

        StringBuilder report = new StringBuilder();

        for(ExceptionModel exceptionModel : exceptionModelList) {
            report.append(exceptionModel.toReportString()).append("\n\n--------------------------\n\n");
        }

        EmailUtil.mailTo(context, EConstants.REPORT_EMAIL, EConstants.REPORT_TITLE, report.toString());
        ILog.iLogDebug(TAG, report.toString());

    }
}
