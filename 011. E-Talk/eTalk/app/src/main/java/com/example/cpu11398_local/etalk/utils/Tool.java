package com.example.cpu11398_local.etalk.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.PopupMenu;
import java.lang.reflect.Field;

public class Tool {

    /**
     * Calculate status bar height with given activity to set height of view as statusBar.
     * @param appCompatActivity activity included view as a statusBar.
     * @param viewStatusBar view need to set height.
     */
    public static void setStatusBarHeight(AppCompatActivity appCompatActivity,
                                          View viewStatusBar) {
        int resourceId = appCompatActivity
                .getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            viewStatusBar.getLayoutParams().height
                    = appCompatActivity.getResources().getDimensionPixelSize(resourceId);
        }
    }

    /**
     * Force given {@code PopupMenu} show icon.
     */
    public static void forcePopupMenuShowIcon(PopupMenu popupMenu) {
        try {
            Field mPopup = PopupMenu.class.getDeclaredField("mPopup");
            mPopup.setAccessible(true);
            Object menuHelper = mPopup.get(popupMenu);
            menuHelper
                    .getClass()
                    .getDeclaredMethod("setForceShowIcon", boolean.class)
                    .invoke(menuHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finish activity with result {@code RESULT_OK}.
     */
    public static void finishSuccessfully(Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    /**
     * Finish activity with result {@code RESULT_CANCELED}.
     */
    public static void finishFailed(Activity activity) {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }
}
