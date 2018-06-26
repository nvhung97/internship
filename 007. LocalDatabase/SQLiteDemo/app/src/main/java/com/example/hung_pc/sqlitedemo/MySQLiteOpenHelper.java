package com.example.hung_pc.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Hung-pc on 6/25/2018.
 */

public class MySQLiteOpenHelper extends SQLiteAssetHelper {

    private final String        TAG         = "SQLite";

    private static final String DB_NAME     = "student.db";
    private static final int    DB_VERSION  = 1;

    private long logTime;

    public MySQLiteOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void insertStudent() {
        logTime = System.currentTimeMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < 10000; ++i) {
            ContentValues record = new ContentValues();
            record.put("id", String.valueOf(i));
            record.put("name", "Nguyễn Văn A");
            record.put("age", i);
            db.insert("info", null, record);
            if ((i + 1) % 1000 == 0) {
                Log.e(TAG, "1000-record " + ((i + 1) / 1000) + "th");
            }
        }
        db.close();
        Log.e(TAG, "Save cost: " + (System.currentTimeMillis() - logTime));
    }

    public void readStudent(){
        logTime = System.currentTimeMillis();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM info",null);
        cursor.moveToFirst();
        Log.e(TAG, "Number of records: "+ cursor.getCount());
        Log.e(TAG, "Load cost: " + (System.currentTimeMillis() - logTime));
        /*while (!cursor.isAfterLast()) {
            Log.e(
                    TAG,
                    "ID: " + cursor.getString(0)
                            + ", Name: " + cursor.getString(1)
                            + ", Age: " + cursor.getInt(2)
            );
            cursor.moveToNext();
        }*/
        db.close();
    }
}
