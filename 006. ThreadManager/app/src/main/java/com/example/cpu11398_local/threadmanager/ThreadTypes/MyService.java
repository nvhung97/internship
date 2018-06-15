package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.cpu11398_local.threadmanager.Utils;

public class MyService extends Service{

    private final  String TAG = "Service";

    @Override
    public void onCreate() {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.showLog(TAG, Thread.currentThread().getId(), "onStartCommand");
        stopSelf();
        return Service.START_STICKY;
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
