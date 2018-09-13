package com.swein.exceptionreport.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by seokho on 17/11/2016.
 */

public class DialogUtil {


    public static void createNormalDialogWithTwoButton(Context context, String title, String message, boolean cancelAble, String positiveButtonText, String negativeButtonText,
                                                       DialogInterface.OnClickListener positiveButton, DialogInterface.OnClickListener negativeButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        if(title == null || title.length() == 0) {

        }

        if(message == null || message.length() == 0) {

        }

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(cancelAble)
                .setPositiveButton(positiveButtonText, positiveButton)
                .setNegativeButton(negativeButtonText, negativeButton);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
