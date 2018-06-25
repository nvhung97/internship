package com.example.cpu11398_local.myapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Student {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String studentId;

    @NonNull
    @ColumnInfo(name = "name")
    private String studentName;

    @NonNull
    @ColumnInfo(name = "age")
    private int studentAge;

    public Student() {

    }

    public Student(@NonNull String studentId, @NonNull String studentName, @NonNull int studentAge) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentAge = studentAge;
    }

    @NonNull
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(@NonNull String studentId) {
        this.studentId = studentId;
    }

    @NonNull
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(@NonNull String studentName) {
        this.studentName = studentName;
    }

    @NonNull
    public int getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(@NonNull int studentAge) {
        this.studentAge = studentAge;
    }
}
