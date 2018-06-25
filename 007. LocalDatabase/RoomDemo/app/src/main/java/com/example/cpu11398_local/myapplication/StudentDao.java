package com.example.cpu11398_local.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface StudentDao {

    @Query("SELECT * FROM Student")
    List<Student>  getAll();

    @Query("SELECT * FROM Student WHERE id LIKE :studentId")
    Student findById(String studentId);

    @Insert
    void insert(Student student);

    @Delete
    void delete(Student student);
}
