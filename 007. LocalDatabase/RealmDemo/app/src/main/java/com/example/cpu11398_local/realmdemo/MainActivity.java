package com.example.cpu11398_local.realmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Realm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        /**
         * Delete realm in case have some change
         */
       /* Realm.deleteRealm(
                new RealmConfiguration
                        .Builder()
                        .schemaVersion(BuildConfig.VERSION_CODE)
                        .build()
        );*/
        Realm realm = Realm.getDefaultInstance();

        /**
         * insert
         */
        /*realm.beginTransaction();
            Student student = realm.createObject(Student.class, "1511360");
            student.setName("Nguyễn Văn Hùng");
            student.setAge(21);
        realm.commitTransaction();*/

        /**
         * delete
         */
        /*realm.beginTransaction();
        RealmResults<Student> delRet = realm.where(Student.class).equalTo("id", "1511232").findAll();
        delRet.deleteAllFromRealm();
        realm.commitTransaction();*/

        /**
         * Update
         */
        /*realm.beginTransaction();
        Student delRet = realm.where(Student.class).equalTo("id", "1513696").findFirst();
        delRet.setAge(22);
        realm.commitTransaction();*/

        RealmResults<Student> results = realm.where(Student.class).findAll();
        for (Student std:results) {
            Log.e(TAG, "id: " + std.getId() + ", name: " + std.getName() + ", age: " + std.getAge());
        }

        realm.close();
    }
}
