package com.swein.appanalysisreport.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swein.appanalysisreport.data.db.dbcrypt.SQLCipherHelper;
import com.swein.appanalysisreport.data.model.impl.DeviceUserData;
import com.swein.appanalysisreport.data.model.impl.ExceptionData;
import com.swein.appanalysisreport.data.model.impl.OperationData;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AppAnalysisReportDBController extends SQLiteOpenHelper {

    private final static String TAG = "AppAnalysisReportDBController";

    private final static int DB_VERSION = 1;

    private final static String DB_KEY = "878905o5i4ifi3i33332opjfif93934tif9303033jof3oltkth31";
    /* 用户&手机&App信息 */
    private final static String DEVICE_USER_TABLE_NAME = "TB_DEVICE_USER";

    private final static String TABLE_COL_DEVICE_UUID = "DEVICE_UUID";
    private final static String TABLE_COL_DEVICE_MODEL = "DEVICE_MODEL";
    private final static String TABLE_COL_OS_VERSION = "OS_VERSION";
    private final static String TABLE_COL_APP_PACKAGE_NAME = "APP_PACKAGE_NAME";
    private final static String TABLE_COL_APP_VERSION = "APP_VERSION";
    private final static String TABLE_COL_OTHER = "OTHER";
    private final static String TABLE_COL_NOTE = "NOTE";

    /* 共通 */
    private final static String TABLE_COL_UUID = "UUID";
    private final static String TABLE_COL_DATE_TIME = "DATE_TIME";
    private final static String TABLE_COL_LOCATION = "LOCATION";
    private final static String TABLE_COL_EVENT_GROUP = "EVENT_GROUP";

    /* 行为 */
    private final static String OPERATION_REPORT_TABLE_NAME = "TB_OPERATION_REPORT";

    private final static String TABLE_COL_OPERATION_TYPE = "OPERATION_TYPE";

    /* 异常 */
    private final static String EXCEPTION_REPORT_TABLE_NAME = "TB_EXCEPTION_REPORT";

    private final static String TABLE_COL_OPERATION_RELATE_ID = "OPERATION_RELATE_ID";
    private final static String TABLE_COL_MESSAGE = "MESSAGE";

    public AppAnalysisReportDBController(Context context) {
        super(context, LoggerProperty.DB_FILE_TEMP_NAME, null, DB_VERSION);
        net.sqlcipher.database.SQLiteDatabase.loadLibs( context );
        SQLCipherHelper.autoEncryptDB(this, context, LoggerProperty.DB_FILE_TEMP_NAME, DB_KEY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDeviceUserTable(db);
        createOperationReportTable(db);
        createExceptionReportTable(db);
    }

    private void createDeviceUserTable(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " +
                DEVICE_USER_TABLE_NAME +
                '(' +
                    TABLE_COL_DEVICE_UUID + " TEXT NOT NULL PRIMARY KEY," +
                    TABLE_COL_DEVICE_MODEL + " TEXT," +
                    TABLE_COL_OS_VERSION + " TEXT," +
                    TABLE_COL_APP_PACKAGE_NAME + " TEXT," +
                    TABLE_COL_APP_VERSION + " TEXT," +
                    TABLE_COL_OTHER + " TEXT" +
                ')';

        db.execSQL(sql);
    }

    private void createOperationReportTable(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " +
                OPERATION_REPORT_TABLE_NAME +
                '(' +
                    TABLE_COL_UUID + " TEXT NOT NULL PRIMARY KEY," +
                    TABLE_COL_EVENT_GROUP + " TEXT," +
                    TABLE_COL_OPERATION_TYPE + " TEXT," +
                    TABLE_COL_LOCATION + " TEXT," +
                    TABLE_COL_DATE_TIME + " TEXT," +
                    TABLE_COL_NOTE + " TEXT" +
                ')';

        db.execSQL(sql);
    }

    private void createExceptionReportTable(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " +
                EXCEPTION_REPORT_TABLE_NAME +
                '(' +
                    TABLE_COL_UUID + " TEXT NOT NULL PRIMARY KEY," +
                    TABLE_COL_OPERATION_RELATE_ID + " TEXT," +
                    TABLE_COL_EVENT_GROUP + " TEXT," +
                    TABLE_COL_LOCATION + " TEXT," +
                    TABLE_COL_MESSAGE + " TEXT," +
                    TABLE_COL_DATE_TIME + " TEXT," +
                    TABLE_COL_NOTE + " TEXT" +
                ')';

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OPERATION_REPORT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCEPTION_REPORT_TABLE_NAME);
        onCreate(db);
    }

    public void clearDataBase() {

        SQLiteDatabase db = getWritableDatabase(DB_KEY);

        db.execSQL("DELETE FROM " + DEVICE_USER_TABLE_NAME);
        db.execSQL("DELETE FROM " + OPERATION_REPORT_TABLE_NAME);
        db.execSQL("DELETE FROM " + EXCEPTION_REPORT_TABLE_NAME);

        db.close();
    }

    public void insertOperation(OperationData operationData) {

        SQLiteDatabase db = getWritableDatabase(DB_KEY);

        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_COL_UUID, operationData.uuid);
            contentValues.put(TABLE_COL_EVENT_GROUP, operationData.eventGroup);
            contentValues.put(TABLE_COL_OPERATION_TYPE, operationData.getOperationTypeString(operationData.operationType));
            contentValues.put(TABLE_COL_LOCATION, operationData.location);
            contentValues.put(TABLE_COL_DATE_TIME, operationData.dateTime);
            contentValues.put(TABLE_COL_NOTE, operationData.note);

            db.replace(OPERATION_REPORT_TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public String getLastOperationUUID() {

        String sql = "SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT ORDER BY TB_OPERATION_REPORT.DATE_TIME DESC LIMIT 0, 1;";

        SQLiteDatabase db = getReadableDatabase(DB_KEY);

        Cursor cursor = db.rawQuery(sql, null);

        String id = "NONE";

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(TABLE_COL_UUID));
        }
        cursor.close();
        db.close();

        return id;
    }

    public void deleteOperationInDateTimeRange(int day) {
        /*
            DELETE FROM TB_OPERATION_REPORT
            WHERE TB_OPERATION_REPORT.UUID IN (SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT
            WHERE strftime('%s','now') - strftime('%s', TB_OPERATION_REPORT.DATE_TIME) > (86400 * 7));
         */
        String sql = "DELETE FROM " + OPERATION_REPORT_TABLE_NAME +
                " WHERE " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + OPERATION_REPORT_TABLE_NAME +
                " WHERE strftime('%s','now') - strftime('%s', " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + ") > (" + LoggerProperty.SECONDS_IN_DAY * day + ")" +
                ");";

        SQLiteDatabase db = getWritableDatabase(DB_KEY);
        db.execSQL(sql);
        db.close();
    }

    public void deleteOperationInRecordTotalNumberRange(int totalNumber) {
        /*
            DELETE FROM TB_OPERATION_REPORT WHERE
            (SELECT COUNT(TB_OPERATION_REPORT.UUID) FROM TB_OPERATION_REPORT
            ) > 5000 AND TB_OPERATION_REPORT.UUID IN
            (SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT
            ORDER BY TB_OPERATION_REPORT.DATE_TIME DESC LIMIT
            (SELECT COUNT(TB_OPERATION_REPORT.UUID) FROM TB_OPERATION_REPORT) OFFSET 5000);
         */
        String sql = "DELETE FROM " + OPERATION_REPORT_TABLE_NAME + " WHERE " +
                "(" +
                "SELECT COUNT(" + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + OPERATION_REPORT_TABLE_NAME +
                ") > " + totalNumber + " AND " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + OPERATION_REPORT_TABLE_NAME +
                " ORDER BY " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + " DESC LIMIT " +
                "(SELECT COUNT(" + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + OPERATION_REPORT_TABLE_NAME + ") OFFSET " + totalNumber +
                ");";

        SQLiteDatabase db = getWritableDatabase(DB_KEY);
        db.execSQL(sql);
        db.close();
    }

    public void insertException(ExceptionData exceptionData) {

        SQLiteDatabase db = getWritableDatabase(DB_KEY);

        try {

            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_COL_UUID, exceptionData.uuid);
            contentValues.put(TABLE_COL_DATE_TIME, exceptionData.dateTime);
            contentValues.put(TABLE_COL_LOCATION, exceptionData.location);
            contentValues.put(TABLE_COL_MESSAGE, exceptionData.exceptionMessage);
            contentValues.put(TABLE_COL_EVENT_GROUP, exceptionData.eventGroup);
            contentValues.put(TABLE_COL_OPERATION_RELATE_ID, exceptionData.operationRelateID);
            contentValues.put(TABLE_COL_NOTE, exceptionData.note);

            db.replace(EXCEPTION_REPORT_TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    public void deleteExceptionInDateTimeRange(int day) {
        /*
            DELETE FROM TB_EXCEPTION_REPORT
            WHERE TB_EXCEPTION_REPORT.UUID IN (SELECT TB_EXCEPTION_REPORT.UUID FROM TB_EXCEPTION_REPORT
            WHERE strftime('%s','now') - strftime('%s', TB_EXCEPTION_REPORT.DATE_TIME) > (86400 * 7));
         */
        String sql = "DELETE FROM " + EXCEPTION_REPORT_TABLE_NAME +
                " WHERE " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + EXCEPTION_REPORT_TABLE_NAME +
                " WHERE strftime('%s','now') - strftime('%s', " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + ") > (" + LoggerProperty.SECONDS_IN_DAY * day + ")" +
                ");";

        SQLiteDatabase db = getWritableDatabase(DB_KEY);
        db.execSQL(sql);
        db.close();
    }

    public void deleteExceptionInRecordTotalNumberRange(int totalNumber) {
        /*
            DELETE FROM TB_EXCEPTION_REPORT WHERE
            (SELECT COUNT(TB_EXCEPTION_REPORT.UUID) FROM TB_EXCEPTION_REPORT
            ) > 5000 AND TB_EXCEPTION_REPORT.UUID IN
            (SELECT TB_EXCEPTION_REPORT.UUID FROM TB_EXCEPTION_REPORT
            ORDER BY TB_EXCEPTION_REPORT.DATE_TIME DESC LIMIT
            (SELECT COUNT(TB_EXCEPTION_REPORT.UUID) FROM TB_EXCEPTION_REPORT) OFFSET 5000);
         */
        String sql = "DELETE FROM " + EXCEPTION_REPORT_TABLE_NAME + " WHERE " +
                "(" +
                "SELECT COUNT(" + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + EXCEPTION_REPORT_TABLE_NAME +
                ") > " + totalNumber + " AND " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + EXCEPTION_REPORT_TABLE_NAME +
                " ORDER BY " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + " DESC LIMIT " +
                "(SELECT COUNT(" + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + EXCEPTION_REPORT_TABLE_NAME + ") OFFSET " + totalNumber +
                ");";

        SQLiteDatabase db = getWritableDatabase(DB_KEY);
        db.execSQL(sql);
        db.close();
    }

    public void insertDeviceUser(DeviceUserData deviceUserData) {

        SQLiteDatabase db = getWritableDatabase(DB_KEY);

        try {

            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_COL_DEVICE_UUID, deviceUserData.deviceUUID);
            contentValues.put(TABLE_COL_DEVICE_MODEL, deviceUserData.deviceModel);
            contentValues.put(TABLE_COL_OS_VERSION, deviceUserData.osVersion);
            contentValues.put(TABLE_COL_APP_PACKAGE_NAME, deviceUserData.appPackageName);
            contentValues.put(TABLE_COL_APP_VERSION, deviceUserData.appVersion);
            contentValues.put(TABLE_COL_OTHER, deviceUserData.other);

            db.replace(DEVICE_USER_TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public List<OperationData> getOperationDataList() {

        String sql = "SELECT * FROM " + OPERATION_REPORT_TABLE_NAME + " ORDER BY " + TABLE_COL_DATE_TIME + " ASC";
        SQLiteDatabase db = getReadableDatabase(DB_KEY);
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<OperationData> operationDataArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {

            OperationData operationData = new OperationData(
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_UUID)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_EVENT_GROUP)),
                    OperationData.getOperationType(cursor.getString(cursor.getColumnIndex(TABLE_COL_OPERATION_TYPE))),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_LOCATION)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DATE_TIME)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_NOTE))
            );

            operationDataArrayList.add(operationData);
        }
        cursor.close();
        db.close();

        return operationDataArrayList;
    }


    public List<ExceptionData> getExceptionDataList() {

        String sql = "SELECT * FROM " + EXCEPTION_REPORT_TABLE_NAME + " ORDER BY " + TABLE_COL_DATE_TIME + " ASC";

        SQLiteDatabase db = getReadableDatabase(DB_KEY);
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<ExceptionData> exceptionModelArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {

            ExceptionData exceptionData = new ExceptionData(
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_UUID)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_OPERATION_RELATE_ID)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_EVENT_GROUP)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_LOCATION)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_MESSAGE)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DATE_TIME)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_NOTE))
            );

            exceptionModelArrayList.add(exceptionData);
        }
        cursor.close();
        db.close();

        return exceptionModelArrayList;
    }


    public List<DeviceUserData> getDeviceUserDataList() {

        String sql = "SELECT * FROM " + DEVICE_USER_TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase(DB_KEY);
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<DeviceUserData> deviceUserDataArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            DeviceUserData deviceUserData = new DeviceUserData(
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DEVICE_UUID)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DEVICE_MODEL)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_OS_VERSION)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_APP_PACKAGE_NAME)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_APP_VERSION)),
                    cursor.getString(cursor.getColumnIndex(TABLE_COL_OTHER))
            );

            deviceUserDataArrayList.add(deviceUserData);
        }
        cursor.close();
        db.close();

        return deviceUserDataArrayList;
    }

}
