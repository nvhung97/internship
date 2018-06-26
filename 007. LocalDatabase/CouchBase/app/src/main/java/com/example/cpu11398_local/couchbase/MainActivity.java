package com.example.cpu11398_local.couchbase;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

public class MainActivity extends AppCompatActivity {

    final String TAG = "CouchBase";

    Database    db;
    ImageView   img;
    long        logTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        /**
         * Create database
         */
        try {
            DatabaseConfiguration config = new DatabaseConfiguration(getApplicationContext());
            db = new Database("StudentDB", config);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        /**
         * Insert
         */
        /*try {
            logTime = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                MutableDocument mutableDocument = new MutableDocument(String.valueOf(i));
                mutableDocument.setString("id", String.valueOf(i));
                mutableDocument.setString("name", "Nguyễn Văn A");
                mutableDocument.setInt("age", i);
                db.save(mutableDocument);
                if ((i + 1) % 1000 == 0) {
                    Log.e(TAG, "1000-record " + ((i + 1) / 1000) + "th");
                }
            }
            Log.e(TAG, "Save cost: " + (System.currentTimeMillis() - logTime));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }*/

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Load
                 */
                try {
                    logTime = System.currentTimeMillis();
                    ResultSet resultSet = QueryBuilder
                            .select(SelectResult.all())
                            .from(DataSource.database(db))
                            .execute();
                    Log.e(TAG, "Load cost: " + (System.currentTimeMillis() - logTime));
                     for (Result result:resultSet) {
                         Log.e(TAG, result.getDictionary("StudentDB").getString("id"));
                         Log.e(TAG, result.getDictionary("StudentDB").getString("name"));
                         Log.e(TAG, result.getDictionary("StudentDB").getInt("age") + "");
                         break;
                     }
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimationDrawable animation = (AnimationDrawable) img.getBackground();
        animation.start();
    }
}
