package com.swein.exceptionreport.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.swein.exceptionreport.model.ExceptionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ERSQLiteController extends SQLiteOpenHelper {

    private final static String TAG = "ERSQLiteController";

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "ExceptionReport.db";
    private final static String TABLE_NAME = "TB_EXCEPTION_REPORT";

    private final static String TABLE_COL_UUID = "UUID";
    private final static String TABLE_COL_APP_VERSION = "APP_VERSION";
    private final static String TABLE_COL_PHONE_MODEL = "PHONE_MODEL";
    private final static String TABLE_COL_USER_ID = "USER_ID";
    private final static String TABLE_COL_DATE_TIME = "DATE_TIME";
    private final static String TABLE_COL_CLASS_FILE_NAME = "CLASS_FILE_NAME";
    private final static String TABLE_COL_METHOD_NAME = "METHOD_NAME";
    private final static String TABLE_COL_LINE_NUMBER = "LINE_NUMBER";
    private final static String TABLE_COL_MESSAGE = "MESSAGE";


    public ERSQLiteController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append('(')
                .append(TABLE_COL_UUID).append(" TEXT NOT NULL PRIMARY KEY,")
                .append(TABLE_COL_APP_VERSION).append(" TEXT,")
                .append(TABLE_COL_PHONE_MODEL).append(" TEXT,")
                .append(TABLE_COL_USER_ID).append(" TEXT,")
                .append(TABLE_COL_DATE_TIME).append(" TEXT,")
                .append(TABLE_COL_CLASS_FILE_NAME).append(" TEXT,")
                .append(TABLE_COL_METHOD_NAME).append(" TEXT,")
                .append(TABLE_COL_LINE_NUMBER).append(" TEXT,")
                .append(TABLE_COL_MESSAGE).append(" TEXT")
                .append(')');

        db.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }


    public void insert(ExceptionModel exceptionModel) {
        SQLiteDatabase db = null;
        try {

            db = getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_COL_UUID, String.valueOf(UUID.randomUUID().toString().replaceAll("-", "")));
            contentValues.put(TABLE_COL_APP_VERSION, exceptionModel.getAppVersion());
            contentValues.put(TABLE_COL_PHONE_MODEL, exceptionModel.getPhoneModel());
            contentValues.put(TABLE_COL_USER_ID, exceptionModel.getUserID());
            contentValues.put(TABLE_COL_DATE_TIME, exceptionModel.getDateTime());
            contentValues.put(TABLE_COL_CLASS_FILE_NAME, exceptionModel.getClassFileName());
            contentValues.put(TABLE_COL_METHOD_NAME, exceptionModel.getMethodName());
            contentValues.put(TABLE_COL_LINE_NUMBER, exceptionModel.getLineNumber());
            contentValues.put(TABLE_COL_MESSAGE, exceptionModel.getExceptionMessage());

            db.insertOrThrow(TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
            close();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                close();
            }
        }
    }

    public List<ExceptionModel> getData(int offset, int limit) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + TABLE_COL_DATE_TIME + " DESC" + " LIMIT " + limit + " OFFSET " + offset, null);

        ArrayList<ExceptionModel> exceptionModelArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {

            ExceptionModel exceptionModel = new ExceptionModel();
            exceptionModel.setAppVersion(cursor.getString(cursor.getColumnIndex(TABLE_COL_APP_VERSION)));
            exceptionModel.setPhoneModel(cursor.getString(cursor.getColumnIndex(TABLE_COL_PHONE_MODEL)));
            exceptionModel.setUserID(cursor.getString(cursor.getColumnIndex(TABLE_COL_USER_ID)));
            exceptionModel.setDateTime(cursor.getString(cursor.getColumnIndex(TABLE_COL_DATE_TIME)));
            exceptionModel.setClassFileName(cursor.getString(cursor.getColumnIndex(TABLE_COL_CLASS_FILE_NAME)));
            exceptionModel.setMethodName(cursor.getString(cursor.getColumnIndex(TABLE_COL_METHOD_NAME)));
            exceptionModel.setLineNumber(cursor.getString(cursor.getColumnIndex(TABLE_COL_LINE_NUMBER)));
            exceptionModel.setExceptionMessage(cursor.getString(cursor.getColumnIndex(TABLE_COL_MESSAGE)));

            exceptionModelArrayList.add(exceptionModel);
        }
        close();

        return exceptionModelArrayList;
    }

    public void deleteDatabase(Context context) {
//        ILog.iLogDebug(TAG, "deleteDatabase");
        close();
        context.deleteDatabase(this.getDatabaseName());
    }

}
