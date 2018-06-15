package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import com.example.cpu11398_local.threadmanager.Utils;

public class MyIntentService extends IntentService {

    private final  String TAG = "IntentService";
    private int info = 0;

    public MyIntentService() {
        super("IntentService");
        Utils.showLog(TAG, Thread.currentThread().getId(), "MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onHandleIntent");
        while (Utils.isAllowLoop) {
            Utils.showLog(TAG, Thread.currentThread().getId(), String.valueOf(++info));
            SystemClock.sleep(Utils.DELAY_TIME);
        }
    }

    @Override
    public void onCreate() {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onStartCommand");
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onDestroy");
        super.onDestroy();
    }
}
