package com.example.cpu11398_local.etalk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupMenu;
import com.example.cpu11398_local.etalk.R;
import java.lang.reflect.Field;
import static android.content.Context.INPUT_METHOD_SERVICE;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap getImageWithUri(Context context, Uri uri) {
        Bitmap bitmapAvatar = null;
        try {
            bitmapAvatar = MediaStore
                    .Images
                    .Media
                    .getBitmap(context.getContentResolver(), uri);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmapAvatar == null) return bitmapAvatar;
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(context.getContentResolver().openInputStream(uri));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (exifInterface == null) return bitmapAvatar;
        switch (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return Tool.rotateImage(bitmapAvatar, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return Tool.rotateImage(bitmapAvatar, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return Tool.rotateImage(bitmapAvatar, 270);
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return Tool.flipImage(bitmapAvatar, true, false);
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return Tool.flipImage(bitmapAvatar, false, true);
            default:
                return bitmapAvatar;
        }
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

    /**
     * Rotate given image by given degrees
     * @param bitmap image need to rotate.
     * @return bitmap image after rotate.
     */
    public static Bitmap rotateImage(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Flip given image horizontal or vertical or both.
     * @param bitmap image need to flip.
     * @param horizontal true if need flip horizontal.
     * @param vertical true if need flip vertical.
     * @return bitmap after flip.
     */
    public static Bitmap flipImage(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Hide the soft keyboard.
     * @param activity  activity that need to hide keyboard.
     */
    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard.
     * @param view      object that need to show keyboard.
     * @param activity  activity that need to show keyboard.
     */
    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}