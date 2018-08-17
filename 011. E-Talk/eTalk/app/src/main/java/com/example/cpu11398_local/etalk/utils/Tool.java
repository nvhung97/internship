package com.example.cpu11398_local.etalk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import com.example.cpu11398_local.etalk.R;
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

    /**
     * Create new Dialog to inform user that application is processing request.
     * @param context context need to show dialog.
     * @return a dialog.
     */
    public static Dialog createProcessingDialog(Context context) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.lyt_loading, null);
        AnimationDrawable animation = (AnimationDrawable)view
                .findViewById(R.id.loading_icon)
                .getBackground();
        Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        animation.start();
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
        return dialog;
    }
}
