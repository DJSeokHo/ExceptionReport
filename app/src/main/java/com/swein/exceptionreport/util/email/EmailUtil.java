package com.swein.exceptionreport.util.email;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.swein.exceptionreport.constants.EConstants;

import java.io.IOException;

public class EmailUtil {

    public static void mailTo(Context context, String email, String title, String message) {

        Uri uri = Uri.parse("mailto:" + email);

        String[] emailCC = {email};

        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.putExtra(Intent.EXTRA_CC, emailCC);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);

        context.startActivity(Intent.createChooser(intent, EConstants.REPORT_APP_SELECT));
    }

}
