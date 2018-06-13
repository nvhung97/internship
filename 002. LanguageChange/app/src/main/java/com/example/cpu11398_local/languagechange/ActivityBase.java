package com.example.cpu11398_local.languagechange;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hung-pc on 6/2/2018.
 */

public class ActivityBase extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocateManager.setLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ActivityInfo activityInfo = getPackageManager()
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            if (activityInfo.labelRes != 0) {
                setTitle(activityInfo.labelRes);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!LocateManager
                .getLocale(this)
                .getLanguage()
                .equalsIgnoreCase(LocateManager.getLanguage(this))) {
            recreate();
        }
    }
}
