package com.example.cpu11398_local.realmdemo;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Realm";

    ImageView   img;
    Realm       realm;
    long        logTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        Realm.init(this);
        /**
         * Delete realm in case have some change
         */
       /*Realm.deleteRealm(
                new RealmConfiguration
                        .Builder()
                        .schemaVersion(BuildConfig.VERSION_CODE)
                        .build()
        );*/
        realm = Realm.getDefaultInstance();

        /**
         * insert
         */
        /*realm.beginTransaction();
            logTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; ++i) {
                Student student = realm.createObject(Student.class, String.valueOf(i));
                student.setName("Nguyễn Văn A");
                student.setAge(i);
                if ((i + 1) % 1000 == 0) {
                    Log.e(TAG, "1000-record " + ((i + 1) / 1000) + "th");
                }
            }
            Log.e(TAG, "Save cost: " + (System.currentTimeMillis() - logTime));
        realm.commitTransaction();*/

        /**
         * delete
         */
        /*realm.beginTransaction();
            RealmResults<Student> delRet = realm.where(Student.class).findAll();
            delRet.deleteAllFromRealm();
        realm.commitTransaction();*/

        /**
         * Update
         */
        /*realm.beginTransaction();
        Student updateRet = realm.where(Student.class).equalTo("id", "1513696").findFirst();
        updateRet.setAge(22);
        realm.commitTransaction();*/

        //realm.close();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Read
                 */
                logTime = System.currentTimeMillis();
                RealmResults<Student> results = realm.where(Student.class).findAll();
                Log.e(TAG, "Number of records: "+ results.size());
                Log.e(TAG, "Load cost: " + (System.currentTimeMillis() - logTime));
                /*for (Student std:results) {
                    Log.e(TAG, "id: " + std.getId() + ", name: " + std.getName() + ", age: " + std.getAge());
                }*/
            }
        });

        /**
         * More example for query
         */
        /*realm.beginTransaction();
            Dog dog = realm.createObject(Dog.class, "Jack");
            Person person = realm.createObject(Person.class, "John");
            person.setDog(dog);
        realm.commitTransaction();
        Log.e(
            TAG,
            realm.where(Person.class).equalTo("dog.name", "Jack").findFirst().getName()
        );*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimationDrawable animation = (AnimationDrawable) img.getBackground();
        animation.start();
    }
}
