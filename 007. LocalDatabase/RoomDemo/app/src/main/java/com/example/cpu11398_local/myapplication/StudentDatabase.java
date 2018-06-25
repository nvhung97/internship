package com.example.cpu11398_local.myapplication;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.content.Context;

@Database(entities = {Student.class}, version = 1)
public abstract class StudentDatabase extends RoomDatabase{

    public abstract StudentDao studentDao();

    private final static String DATABASE_NAME  = "StudentInfo";
    private static StudentDatabase db = null;

    public static StudentDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(
                    context.getApplicationContext(),
                    StudentDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return db;
    }
}