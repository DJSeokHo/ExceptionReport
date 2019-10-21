package com.swein.appanalysisreport.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swein.appanalysisreport.data.db.dbcrypt.SQLCipherHelper;
import com.swein.appanalysisreport.data.model.impl.DeviceUserData;
import com.swein.appanalysisreport.data.model.impl.ExceptionData;
import com.swein.appanalysisreport.data.model.impl.OperationData;
import com.swein.appanalysisreport.loggerproperty.LoggerProperty;
import com.swein.appanalysisreport.util.file.FileIOUtil;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

public class AppAnalysisReportDBController extends SQLiteOpenHelper {

    private final static String TAG = "AppAnalysisReportDBController";

    private final static int DB_VERSION = 1;

    private final static String DB_KEY = "878905o5i4ifi3i33332opjfif93934tif9303033jof3oltkth31";
    /* 사용자&기기 */
    private final static String DEVICE_USER_TABLE_NAME = "TB_DEVICE_USER";

    private final static String TABLE_COL_DEVICE_UUID = "DEVICE_UUID";
    private final static String TABLE_COL_DEVICE_MODEL = "DEVICE_MODEL";
    private final static String TABLE_COL_OS_VERSION = "OS_VERSION";
    private final static String TABLE_COL_APP_NAME = "APP_NAME";
    private final static String TABLE_COL_APP_VERSION = "APP_VERSION";
    private final static String TABLE_COL_OTHER = "OTHER";
    private final static String TABLE_COL_NOTE = "NOTE";

    /* 공통 */
    private final static String TABLE_COL_UUID = "UUID";
    private final static String TABLE_COL_DATE_TIME = "DATE_TIME";
    private final static String TABLE_COL_LOCATION = "LOCATION";
    private final static String TABLE_COL_EVENT_GROUP = "EVENT_GROUP";

    /* 행동 */
    private final static String OPERATION_REPORT_TABLE_NAME = "TB_OPERATION_REPORT";

    private final static String TABLE_COL_OPERATION_TYPE = "OPERATION_TYPE";

    /* 오류 */
    private final static String EXCEPTION_REPORT_TABLE_NAME = "TB_EXCEPTION_REPORT";

    private final static String TABLE_COL_OPERATION_RELATE_ID = "OPERATION_RELATE_ID";
    private final static String TABLE_COL_MESSAGE = "MESSAGE";


    private SQLiteDatabase db;

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

        String stringBuilder = "CREATE TABLE IF NOT EXISTS " +
                DEVICE_USER_TABLE_NAME +
                '(' +
                    TABLE_COL_DEVICE_UUID + " TEXT NOT NULL PRIMARY KEY," +
                    TABLE_COL_DEVICE_MODEL + " TEXT," +
                    TABLE_COL_OS_VERSION + " TEXT," +
                    TABLE_COL_APP_NAME + " TEXT," +
                    TABLE_COL_APP_VERSION + " TEXT," +
                    TABLE_COL_OTHER + " TEXT" +
                ')';

        db.execSQL(stringBuilder);
    }

    private void createOperationReportTable(SQLiteDatabase db) {

        String stringBuilder = "CREATE TABLE IF NOT EXISTS " +
                OPERATION_REPORT_TABLE_NAME +
                '(' +
                    TABLE_COL_UUID + " TEXT NOT NULL PRIMARY KEY," +
                    TABLE_COL_DATE_TIME + " TEXT," +
                    TABLE_COL_LOCATION + " TEXT," +
                    TABLE_COL_OPERATION_TYPE + " TEXT," +
                    TABLE_COL_EVENT_GROUP + " TEXT," +
                    TABLE_COL_NOTE + " TEXT" +
                ')';

        db.execSQL(stringBuilder);
    }

    private void createExceptionReportTable(SQLiteDatabase db) {

        String stringBuilder = "CREATE TABLE IF NOT EXISTS " +
                EXCEPTION_REPORT_TABLE_NAME +
                '(' +
                    TABLE_COL_UUID + " TEXT NOT NULL PRIMARY KEY," +
                    TABLE_COL_DATE_TIME + " TEXT," +
                    TABLE_COL_LOCATION + " TEXT," +
                    TABLE_COL_MESSAGE + " TEXT," +
                    TABLE_COL_EVENT_GROUP + " TEXT," +
                    TABLE_COL_OPERATION_RELATE_ID + " TEXT," +
                    TABLE_COL_NOTE + " TEXT" +
                ')';

        db.execSQL(stringBuilder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OPERATION_REPORT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCEPTION_REPORT_TABLE_NAME);

        onCreate(db);
    }

    public void deleteDatabase(Context context) {
        close();
        context.deleteDatabase(LoggerProperty.DB_FILE_TEMP_NAME);
    }

    public void clearDataBase() {
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OPERATION_REPORT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCEPTION_REPORT_TABLE_NAME);

        createDeviceUserTable(db);
        createOperationReportTable(db);
        createExceptionReportTable(db);
    }

    public void closeDataBase() {
        close();
    }

    public void deleteDBFileToOutsideFolderForTemp() {
        File file = new File(LoggerProperty.DB_FILE_TEMP_PATH, LoggerProperty.DB_FILE_TEMP_NAME);
        if(file.exists()) {
            file.delete();
        }
    }

    public File copyDBFileToOutsideFolderForTemp() {

        /*
         * ************ DB copy just for debugging. Do not delete this part ************ from here [DB test issue]
         * //check here before release !!!!warning: [this part] only for debugging, commented out code between [this part] in release
         *
         */
        File folder = new File(LoggerProperty.DB_FILE_TEMP_PATH);
        if(!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(LoggerProperty.DB_FILE_TEMP_PATH, LoggerProperty.DB_FILE_TEMP_NAME);

        if(file.exists()) {
            boolean success = file.delete();
            if(!success) {
                return null;
            }
        }

        try {
            FileIOUtil.copyFileUsingFileChannels(new File(getReadableDatabase(DB_KEY).getPath()), file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void openDB() {
        db = getWritableDatabase(DB_KEY);
    }

    public void insertOperation(OperationData operationData) {

        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_COL_UUID, operationData.uuid);
            contentValues.put(TABLE_COL_DATE_TIME, operationData.dateTime);
            contentValues.put(TABLE_COL_LOCATION, operationData.location);
            contentValues.put(TABLE_COL_OPERATION_TYPE, operationData.getOperationTypeString(operationData.operationType));
            contentValues.put(TABLE_COL_EVENT_GROUP, operationData.eventGroup);
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
            }
        }
    }

    public String getLastOperationUUID() {
        String sql = "SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT ORDER BY TB_OPERATION_REPORT.DATE_TIME DESC LIMIT 0, 1;";

        Cursor cursor = getReadableDatabase(DB_KEY).rawQuery(sql, null);

        String id = "NONE";

        while (cursor.moveToNext()) {

            id = cursor.getString(cursor.getColumnIndex(TABLE_COL_UUID));
        }
        cursor.close();
        return id;
    }

    public void deleteOperationInDateTimeRange(int day) {
        /*
            DELETE FROM TB_OPERATION_REPORT
            WHERE TB_OPERATION_REPORT.UUID IN (SELECT TB_OPERATION_REPORT.UUID FROM TB_OPERATION_REPORT
            WHERE strftime('%s','now') - strftime('%s', TB_OPERATION_REPORT.DATE_TIME) > (86400 * 7));
         */
        String stringBuilder = "DELETE FROM " + OPERATION_REPORT_TABLE_NAME +
                " WHERE " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + OPERATION_REPORT_TABLE_NAME +
                " WHERE strftime('%s','now') - strftime('%s', " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + ") > (" + LoggerProperty.SECONDS_IN_DAY * day + ")" +
                ");";

        db.execSQL(stringBuilder);
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
        String stringBuilder = "DELETE FROM " + OPERATION_REPORT_TABLE_NAME + " WHERE " +
                "(" +
                "SELECT COUNT(" + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + OPERATION_REPORT_TABLE_NAME +
                ") > " + totalNumber + " AND " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + OPERATION_REPORT_TABLE_NAME +
                " ORDER BY " + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + " DESC LIMIT " +
                "(SELECT COUNT(" + OPERATION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + OPERATION_REPORT_TABLE_NAME + ") OFFSET " + totalNumber +
                ");";

        db.execSQL(stringBuilder);
    }

    public void insertException(ExceptionData exceptionData) {

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
            }
        }
    }


    public void deleteExceptionInDateTimeRange(int day) {
        /*
            DELETE FROM TB_EXCEPTION_REPORT
            WHERE TB_EXCEPTION_REPORT.UUID IN (SELECT TB_EXCEPTION_REPORT.UUID FROM TB_EXCEPTION_REPORT
            WHERE strftime('%s','now') - strftime('%s', TB_EXCEPTION_REPORT.DATE_TIME) > (86400 * 7));
         */
        String stringBuilder = "DELETE FROM " + EXCEPTION_REPORT_TABLE_NAME +
                " WHERE " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + EXCEPTION_REPORT_TABLE_NAME +
                " WHERE strftime('%s','now') - strftime('%s', " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + ") > (" + LoggerProperty.SECONDS_IN_DAY * day + ")" +
                ");";

        db.execSQL(stringBuilder);
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
        String stringBuilder = "DELETE FROM " + EXCEPTION_REPORT_TABLE_NAME + " WHERE " +
                "(" +
                "SELECT COUNT(" + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + EXCEPTION_REPORT_TABLE_NAME +
                ") > " + totalNumber + " AND " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " IN " +
                "(" +
                "SELECT " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + " FROM " + EXCEPTION_REPORT_TABLE_NAME +
                " ORDER BY " + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_DATE_TIME + " DESC LIMIT " +
                "(SELECT COUNT(" + EXCEPTION_REPORT_TABLE_NAME + "." + TABLE_COL_UUID + ") FROM " + EXCEPTION_REPORT_TABLE_NAME + ") OFFSET " + totalNumber +
                ");";

        db.execSQL(stringBuilder);
    }

    public void insertDeviceUser(DeviceUserData deviceUserData) {
        try {

            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_COL_DEVICE_UUID, deviceUserData.deviceUUID);
            contentValues.put(TABLE_COL_DEVICE_MODEL, deviceUserData.deviceModel);
            contentValues.put(TABLE_COL_OS_VERSION, deviceUserData.osVersion);
            contentValues.put(TABLE_COL_APP_NAME, deviceUserData.appName);
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
            }
        }
    }

//    public List<OperationData> getData(int offset, int limit) {
//
//        Cursor cursor = getReadableDatabase(DB_KEY).rawQuery("SELECT * FROM " + OPERATION_REPORT_TABLE_NAME + " ORDER BY " + TABLE_COL_DATE_TIME + " DESC" + " LIMIT " + limit + " OFFSET " + offset, null);
//
//        ArrayList<OperationData> operationDataArrayList = new ArrayList<>();
//
//        while (cursor.moveToNext()) {
//
//            OperationData operationData = new OperationData(
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_UUID)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DATE_TIME)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_LOCATION)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_EVENT_GROUP)),
//                    OperationData.getOperationType(cursor.getString(cursor.getColumnIndex(TABLE_COL_OPERATION_TYPE))),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_NOTE))
//            );
//
//            operationDataArrayList.add(operationData);
//        }
//        close();
//
//        return operationDataArrayList;
//    }


//    public List<ExceptionData> getData(int offset, int limit) {
//
//        Cursor cursor = getReadableDatabase(DB_KEY).rawQuery("SELECT * FROM " + EXCEPTION_REPORT_TABLE_NAME + " ORDER BY " + TABLE_COL_DATE_TIME + " DESC" + " LIMIT " + limit + " OFFSET " + offset, null);
//
//        ArrayList<ExceptionData> exceptionModelArrayList = new ArrayList<>();
//
//        while (cursor.moveToNext()) {
//
//            ExceptionData exceptionData = new ExceptionData(
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_UUID)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DATE_TIME)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_LOCATION)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_MESSAGE)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_EVENT_GROUP)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_OPERATION_RELATE_ID)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_NOTE))
//            );
//
//            exceptionModelArrayList.add(exceptionData);
//        }
//        close();
//
//        return exceptionModelArrayList;
//    }


//    public List<DeviceUserData> getData() {
//
//        Cursor cursor = getReadableDatabase(DB_KEY).rawQuery("SELECT * FROM " + DEVICE_USER_TABLE_NAME, null);
//
//        ArrayList<DeviceUserData> deviceUserDataArrayList = new ArrayList<>();
//
//        while (cursor.moveToNext()) {
//            DeviceUserData deviceUserData = new DeviceUserData(
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DEVICE_UUID)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_DEVICE_MODEL)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_OS_VERSION)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_APP_NAME)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_APP_VERSION)),
//                    cursor.getString(cursor.getColumnIndex(TABLE_COL_OTHER))
//            );
//
//            deviceUserDataArrayList.add(deviceUserData);
//        }
//        close();
//
//        return deviceUserDataArrayList;
//    }

}
