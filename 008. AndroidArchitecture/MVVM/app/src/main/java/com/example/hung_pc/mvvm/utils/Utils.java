package com.example.hung_pc.mvvm.utils;

import android.util.Log;

/**
 * Created by Hung-pc on 6/28/2018.
 */

public class Utils {

    public static final String TAG = "MVVM";

    public static void showLog(String positon, String message) {
        Log.e(TAG, positon + "." + message);
    }
}