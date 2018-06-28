package com.example.cpu11398_local.mvp.utils;

import android.util.Log;

public class Utils {

    public static final String TAG = "MVP";

    public static void showLog(String positon, String message) {
        Log.e(TAG, positon + "." + message);
    }
}