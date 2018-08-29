package com.example.cpu11398_local.etalk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

    /**
     * Show pop up to allow user to choose to take photo from camera or gallery
     */
    public static Dialog createImageOptionDialog(final Context context,
                                                 final int REQUEST_CAMERA_CODE,
                                                 final int REQUEST_GALLERY_CODE){
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.lyt_image_option, null);
        Button btn_option_camera  = view.findViewById(R.id.profile_activity_image_option_camera);
        Button btn_option_gallery = view.findViewById(R.id.profile_activity_image_option_library);
        Dialog imageOptionDialog  = new Dialog(context);
        imageOptionDialog.setContentView(view);
        imageOptionDialog.setCancelable(true);
        imageOptionDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );

        btn_option_camera.setOnClickListener(view1 -> {
            imageOptionDialog.dismiss();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ((FragmentActivity) context).startActivityForResult(intent, REQUEST_CAMERA_CODE);
        });

        btn_option_gallery.setOnClickListener(view2 -> {
            imageOptionDialog.dismiss();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((FragmentActivity) context).startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    REQUEST_GALLERY_CODE
            );
        });

        return imageOptionDialog;
    }

    /**
     * Reduce size of image.
     * @param image image to upload or download.
     * @return bitmap image after resize.
     */
    public static Bitmap resizeImage(Bitmap image, final int MAX_SIZE) {

        int     width       = image.getWidth();
        int     height      = image.getHeight();
        float   bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            height = MAX_SIZE;
            width  = (int) (height * bitmapRatio);
        } else {
            width  = MAX_SIZE;
            height = (int) (width / bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
