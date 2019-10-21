package com.swein.exceptionreport.data.db.dbcrypt;

import android.content.Context;
import android.database.Cursor;

import com.swein.exceptionreport.util.debug.log.ILog;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

public class SQLCipherHelper {

    public static SQLiteDatabase autoEncryptDB(SQLiteOpenHelper dbHelper, Context context, String databaseName, String dbPassphase) {
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase(dbPassphase);
        }
        catch (Exception e) {

            e.printStackTrace();
            db = dbHelper.getWritableDatabase("");
            try {

                db.close();

                encryptDB(context, databaseName, dbPassphase);

                db = dbHelper.getWritableDatabase(dbPassphase);
            }
            catch (Exception e1) {

                e1.printStackTrace();
                db.close();
            }
            finally {
                db.close();
            }
        }
        finally {
            db.close();
        }

        return db;
    }


    private static void encryptDB(Context ctxt, String dbName, String passphrase)
            throws IOException {

        File originalFile = ctxt.getDatabasePath(dbName);

        if (originalFile.exists()) {
            File newFile = File.createTempFile("sqlcipherutils", "tmp",
                    ctxt.getCacheDir());
            SQLiteDatabase db = SQLiteDatabase.openDatabase(
                    originalFile.getAbsolutePath(), "", null,
                    SQLiteDatabase.OPEN_READWRITE);

            db.rawExecSQL(String.format(
                    "ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                    newFile.getAbsolutePath(), passphrase));
            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
            db.rawExecSQL("DETACH DATABASE encrypted;");

            int version = db.getVersion();

            db.close();

            db = SQLiteDatabase.openDatabase(newFile.getAbsolutePath(), passphrase, null, SQLiteDatabase.OPEN_READWRITE);
            db.setVersion(version);
            db.close();

            originalFile.delete();
            newFile.renameTo(originalFile);
        }
    }


    public static void logDBSQLCipherVersion(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("PRAGMA cipher_version;", null);
        cursor.moveToFirst();
        String version = cursor.getString(0);
        ILog.iLogError("version", String.format("SQLCipher version:%s", version));
        cursor.close();
    }

}
