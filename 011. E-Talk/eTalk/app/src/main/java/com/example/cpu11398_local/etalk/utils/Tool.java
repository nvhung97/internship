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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class Tool {

    /**
     * {@code imageCaptureUri} used to hold {@code Uri} of image from intent camera.
     */
    public static Uri imageCaptureUri = null;

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
     * Disable rotate animation of given {@code activity}.
     * @param activity need to disable rotate animation.
     */
    public static void setRotationAnimation(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE;
        window.setAttributes(winParams);
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
     * Create new {@code Dialog} to notify something to user.
     */
    public static Dialog createNotificationDialog(Context context,
                                                  String title,
                                                  String content,
                                                  String positive,
                                                  Callable<Void> positiveFunc,
                                                  String negative,
                                                  Callable<Void> negativeFunc) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.lyt_notification, null)
        );
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
        TextView txt_title    = dialog.findViewById(R.id.notification_title);
        TextView txt_content  = dialog.findViewById(R.id.notification_content);
        Button   btn_positive = dialog.findViewById(R.id.notification_positive);
        Button   btn_negative = dialog.findViewById(R.id.notification_negative);

        txt_title.setText(title);
        txt_content.setText(content);
        btn_positive.setText(positive);
        btn_negative.setText(negative);

        btn_positive.setOnClickListener(v -> {
            dialog.dismiss();
            try {
                if (positiveFunc != null) {
                    positiveFunc.call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        btn_negative.setOnClickListener(v -> {
            dialog.dismiss();
            try {
                if (negativeFunc != null) {
                    negativeFunc.call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
     * Show pop up to allow user to choose to take photo or video from camera or gallery
     */
    public static Dialog createMediaOptionDialog(final Context context,
                                                 final int REQUEST_TAKE_PHOTO_CODE,
                                                 final int REQUEST_RECORD_CODE,
                                                 final int REQUEST_CHOOSE_PHOTOS_CODE,
                                                 final int REQUEST_CHOOSE_VIDEOS_CODE) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.lyt_media_option, null);
        Button btn_take_photo       = view.findViewById(R.id.chat_activity_media_option_take_photo);
        Button btn_record           = view.findViewById(R.id.chat_activity_media_option_record);
        Button btn_choose_photos    = view.findViewById(R.id.chat_activity_media_option_choose_photos);
        Button btn_choose_videos    = view.findViewById(R.id.chat_activity_media_option_choose_videos);
        Dialog mediaOptionDialog  = new Dialog(context);
        mediaOptionDialog.setContentView(view);
        mediaOptionDialog.setCancelable(true);
        mediaOptionDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );

        btn_take_photo.setOnClickListener(view1 -> {
            mediaOptionDialog.dismiss();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                File dir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "Camera"
                );
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = File.createTempFile(
                        "IMG_",
                        ".jpg",
                        dir
                );
                imageCaptureUri = FileProvider.getUriForFile(context, "com.example.cpu11398_local.etalk.provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((FragmentActivity) context).startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
        });

        btn_record.setOnClickListener(view1 -> {
            mediaOptionDialog.dismiss();
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            ((FragmentActivity) context).startActivityForResult(intent, REQUEST_RECORD_CODE);
        });

        btn_choose_photos.setOnClickListener(view2 -> {
            mediaOptionDialog.dismiss();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((FragmentActivity) context).startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    REQUEST_CHOOSE_PHOTOS_CODE
            );
        });

        btn_choose_videos.setOnClickListener(view2 -> {
            mediaOptionDialog.dismiss();
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((FragmentActivity) context).startActivityForResult(
                    Intent.createChooser(intent, "Select Video"),
                    REQUEST_CHOOSE_VIDEOS_CODE
            );
        });

        return mediaOptionDialog;
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

        int width  = image.getWidth();
        int height = image.getHeight();

        if (width > height) {
            return Bitmap.createScaledBitmap(
                    image,
                    MAX_SIZE,
                    MAX_SIZE * height / width,
                    true
            );
        } else {
            return Bitmap.createScaledBitmap(
                    image,
                    MAX_SIZE * width / height,
                    MAX_SIZE,
                    true
            );
        }
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
