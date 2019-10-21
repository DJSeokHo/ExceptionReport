package com.swein.exceptionreport.util.email;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;

public class EmailUtil {

    public static void mailTo(Context context, String email, String title, String message) {

        Uri uri = Uri.parse("mailto:" + email);
        String[] emailInfo = {email};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

        intent.putExtra(Intent.EXTRA_CC, emailInfo);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);

        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            context.startActivity(Intent.createChooser(intent, "이메일 앱 선택하세요"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void mailToWithText(Context context, String[] emailReceiver, String title, String message) {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("plain/text");
        email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReceiver);
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        email.putExtra(android.content.Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

    }

    public static void mailToWithFile(Context context, File file, String[] emailReceiver, String title, String message) {

        if(file == null) {
            return;
        }

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("application/octet-stream");

        intent.putExtra(android.content.Intent.EXTRA_EMAIL, emailReceiver);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(Intent.createChooser(intent, "select email app please"));


    }

}
