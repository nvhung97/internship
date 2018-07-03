package com.example.cpu11398_local.dagger2demo.utils;

import android.util.Log;

public class Utils {

    public static final String TAG = "Dagger2";

    public static void showLog(String positon, String message) {
        Log.e(TAG, positon + "." + message);
    }
}