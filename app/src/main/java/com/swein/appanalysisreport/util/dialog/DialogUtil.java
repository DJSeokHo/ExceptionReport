package com.swein.appanalysisreport.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class DialogUtil {



    public static void createNormalDialogWithThreeButton(Context context, String title, String message, boolean cancelAble, String positiveButtonText, String negativeButtonText, String otherButtonText,
                                                         DialogInterface.OnClickListener positiveButton, DialogInterface.OnClickListener negativeButton, DialogInterface.OnClickListener neutralButton) {

        if(title == null || title.length() == 0) {
            return;
        }

        if(message == null || message.length() == 0) {
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(cancelAble)
                .setPositiveButton(positiveButtonText, positiveButton)
                .setNegativeButton(negativeButtonText, negativeButton)
                .setNeutralButton(otherButtonText, neutralButton);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
