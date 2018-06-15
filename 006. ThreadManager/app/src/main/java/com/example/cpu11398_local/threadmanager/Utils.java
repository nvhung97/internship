package com.example.cpu11398_local.threadmanager;

import android.util.Log;

public class Utils {

    private static final String MAIN_TAG    = "ThreadManager";
    public  static final int    DELAY_TIME  = 1000;
    public  static boolean      isAllowLoop = false;

    public static void showLog(String subTag, long threadID, String info) {
        Log.e(MAIN_TAG, subTag + "(Thread Id: " + threadID + ", info: " + info +")");
    }
}
