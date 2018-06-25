package com.example.hung_pc.sqlitedemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Hung-pc on 6/25/2018.
 */

public class MySQLiteOpenHelper extends SQLiteAssetHelper {

    private final String        TAG         = "LocalDatabase";

    private static final String DB_NAME     = "student.db";
    private static final int    DB_VERSION  = 1;

    public MySQLiteOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void readStudent(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM info",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.e(
                    TAG,
                    "ID: " + cursor.getString(0)
                            + ", Name: " + cursor.getString(1)
                            + ", Age: " + cursor.getInt(2)
            );
            cursor.moveToNext();
        }
        db.close();
    }
}
