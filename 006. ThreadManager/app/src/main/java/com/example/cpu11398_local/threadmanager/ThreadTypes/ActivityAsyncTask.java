package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.cpu11398_local.threadmanager.Utils;
import com.example.cpu11398_local.threadmanager.R;

public class ActivityAsyncTask extends AppCompatActivity {

    private final  String TAG = "AsyncTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        new MyAsyncTask().execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        private int info = 0;

        public MyAsyncTask(){
            Toast.makeText(ActivityAsyncTask.this, "Created new AsyncTask", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            Utils.showLog(TAG, Thread.currentThread().getId(), "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Utils.showLog(TAG, Thread.currentThread().getId(), "doInBackground");
            while (Utils.isAllowLoop) {
                Utils.showLog(TAG, Thread.currentThread().getId(), String.valueOf(++info));
                SystemClock.sleep(Utils.DELAY_TIME);
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Utils.showLog(TAG, Thread.currentThread().getId(), "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Utils.showLog(TAG, Thread.currentThread().getId(), "onPostExecute");
        }

        @Override
        protected void onCancelled(Void aVoid) {
            Utils.showLog(TAG, Thread.currentThread().getId(), "onCancelled");
        }
    }
}
