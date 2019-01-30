package com.example.cpu11398_local.etalk.presentation.view.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AutoFitTextureView;
import com.example.cpu11398_local.etalk.presentation.view.camera.Utils.MyGLUtils;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.AsciiArtFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.BaseFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.EdgeDetectionFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.LegofiedFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.OriginalFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.PolygonizationFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.TrianglesMosaicFilter;
import com.example.cpu11398_local.etalk.utils.Tool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class CaptureActivity extends AppCompatActivity {

    private final int ANIMATION_DURATION = 500;
    private final int EGL_OPENGL_ES2_BIT = 4;
    private final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

    /**
     * OpenGL ES 2.0
     */
    private EGL10 egl10;
    private EGLDisplay eglDisplay;
    private EGLContext eglContext;
    private EGLSurface eglSurface;

    private BaseFilter filter;
    private SurfaceTexture cameraSurfaceTexture;

    private SparseArray<BaseFilter> filterMap = new SparseArray<>();

    private int cameraTextureId;

    /**
     * Detect Device's orientation.
     */
    private OrientationEventListener orientationEventListener;
    private int mOrientation = 0;

    /**
     * Views on this activity
     */
    private ConstraintLayout    rootView;
    private ConstraintLayout    actionView;
    private ConstraintLayout    filterView;
    private AutoFitTextureView  textureView;
    private LinearLayout        resolutionView;
    private Button btnCapture;
    private Button btnTick;
    private Button btnSwith;
    private Button btnCancel;
    private Button btnResolution1;
    private Button btnResolution2;
    private Button btnResolution3;

    /**
     * Camera state: unknown.
     */
    private static final int STATE_UNKNOWN = -1;

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 1;

    /**
     * Camera state: Camera not working.
     */
    private static final int STATE_NOT_WORK = 2;

    /**
     * Define current camera
     */
    private int cameraChoice = CameraCharacteristics.LENS_FACING_BACK;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height, cameraChoice);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    /**
     * Render on background
     */
    private Runnable mCameraRender = new Runnable() {
        @Override
        public void run() {
            initGL(textureView.getSurfaceTexture());

            if (filterMap.size() != 0) {
                filterMap.clear();
            }
            filterMap.append(R.id.capture_activity_original_filter, new OriginalFilter(CaptureActivity.this));
            filterMap.append(R.id.capture_activity_legofied_filter, new LegofiedFilter(CaptureActivity.this));
            filterMap.append(R.id.capture_activity_trianglesmosaic_filter, new TrianglesMosaicFilter(CaptureActivity.this));
            filterMap.append(R.id.capture_activity_poligonization_filter, new PolygonizationFilter(CaptureActivity.this));
            filterMap.append(R.id.capture_activity_asciiart_filter, new AsciiArtFilter(CaptureActivity.this));
            filterMap.append(R.id.capture_activity_edgedetection_filter, new EdgeDetectionFilter(CaptureActivity.this));

            setSelectedFilter(filterId);

            cameraTextureId = MyGLUtils.genTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
            cameraSurfaceTexture = new SurfaceTexture(cameraTextureId);

            createCameraPreviewSession();
        }
    };

    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * The optionResolutionCamera {@link android.util.Size} user can choose.
     */
    private Size[] optionResolutionCamera;

    /**
     * Current optionResolutionCamera size
     */
    private int numChoice = 0;

    /**
     * Store current filter
     */
    private int filterId = R.id.capture_activity_original_filter;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            mBackgroundHandler.post(mCameraRender);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Tool.finishFailed(CaptureActivity.this);
        }

    };

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * This is the output file for our picture.
     */
    private File mFile;

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;

    /**
     * The current state of camera state for taking pictures.
     */
    private int mState = STATE_UNKNOWN;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;

    /**
     * Get some size from {@code choices} such as largest sizes has ratio 16:9, 4:3, 1:1.
     * @param cameraChoices the list of sizes that the camera supports for the intended output class
     */
    private void chooseOptionSize(Size[] cameraChoices) {
        optionResolutionCamera = new Size[3];
        btnResolution1.setEnabled(false);
        btnResolution2.setEnabled(false);
        btnResolution3.setEnabled(false);
        for (Size choice : cameraChoices) {
            if (optionResolutionCamera[0] == null) {
                if (choice.getWidth() * 9 / 16 == choice.getHeight()) {
                    optionResolutionCamera[0] = choice;
                    btnResolution1.setEnabled(true);
                    btnResolution1.setText("16:9");
                }
            }
            if (optionResolutionCamera[1] == null) {
                if (choice.getWidth() * 3 / 4 == choice.getHeight()) {
                    optionResolutionCamera[1] = choice;
                    btnResolution2.setEnabled(true);
                    btnResolution2.setText("4:3");
                }
            }
            if (optionResolutionCamera[2] == null) {
                if (choice.getWidth() * 1 / 1 == choice.getHeight()) {
                    optionResolutionCamera[2] = choice;
                    btnResolution3.setEnabled(true);
                    btnResolution3.setText("1:1");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        rootView        = findViewById(R.id.capture_activity);
        actionView      = findViewById(R.id.capture_activity_action);
        textureView     = findViewById(R.id.capture_activity_texture);
        filterView      = findViewById(R.id.capture_activity_lyt_filter);
        resolutionView  = findViewById(R.id.capture_activity_resolution);
        btnCapture      = findViewById(R.id.capture_activity_capture);
        btnTick         = findViewById(R.id.capture_activity_tick);
        btnSwith        = findViewById(R.id.capture_activity_switch);
        btnCancel       = findViewById(R.id.capture_activity_cancel);
        btnResolution1  = findViewById(R.id.capture_activity_resolution_1);
        btnResolution2  = findViewById(R.id.capture_activity_resolution_2);
        btnResolution3  = findViewById(R.id.capture_activity_resolution_3);

        // Hide filter View
        filterView.post(() -> toggleFilterView(false, 0));

        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "eTalk"
        );
        if (!dir.exists() && !dir.mkdirs()) {
            Tool.finishFailed(this);
        } else {
            mFile = new File(dir, "IMG_" + System.currentTimeMillis() + ".jpg");
        }

        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                rotateView(getImageOrientation(orientation));
            }
        };
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight(), cameraChoice);
        } else {
            textureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }

        // Set ratio for action layout
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        constraintSet.setDimensionRatio(actionView.getId(), size.x + ":" + (size.y - size.x * 4 / 3));
        constraintSet.applyTo(rootView);

        // Delete file if it existed
        if (mFile.exists()) {
            mFile.delete();
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        if (mState == STATE_PICTURE_TAKEN) {
            btnCapture.setVisibility(View.VISIBLE);
            btnSwith.setVisibility(View.VISIBLE);
            btnTick.setVisibility(View.GONE);
            resolutionView.setVisibility(View.VISIBLE);
        }
        mState = STATE_NOT_WORK;
        super.onPause();
    }

    public void onBackPressed(View view) {
        if (mFile.exists()) {
            mFile.delete();
        }
        orientationEventListener.disable();
        Tool.finishFailed(this);
    }

    @Override
    protected void onDestroy() {
        orientationEventListener.disable();
        super.onDestroy();
    }

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void setUpCameraOutputs(int width, int height, int cameraChoice) {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == null || facing != cameraChoice) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                );
                if (map == null) {
                    continue;
                }

                chooseOptionSize(map.getOutputSizes(SurfaceTexture.class));
                switch (numChoice) {
                    case 0:
                        onChooseResolution1(null);
                        break;
                    case 1:
                        onChooseResolution2(null);
                        break;
                    case 2:
                        onChooseResolution3(null);
                        break;
                }

                // Check if the flash is supported.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the camera specified by {@link CaptureActivity#mCameraId}.
     */
    @SuppressLint("MissingPermission")
    private void openCamera(int width, int height, int cameraChoice) {
        setUpCameraOutputs(width, height, cameraChoice);
        configureTransform(width, height);
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
                mCameraId = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.interrupt();
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BaseFilter.release();
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            // We configure the size of default buffer to be the size of camera preview we want.
            cameraSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(cameraSurfaceTexture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(
                    Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            mState = STATE_PREVIEW;

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                                );
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(
                                        mPreviewRequest,
                                        null,
                                        mBackgroundHandler);

                                // Render loop
                                while (!Thread.currentThread().isInterrupted()) {
                                    if (mState == STATE_PREVIEW) {
                                        if (textureView.getWidth() >= 0 && textureView.getHeight() >= 0)
                                            GLES20.glViewport(0, 0, textureView.getWidth(), textureView.getHeight());

                                        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

                                        // Update the camera preview texture
                                        synchronized (this) {
                                            cameraSurfaceTexture.updateTexImage();
                                        }

                                        // Draw camera preview
                                        filter.draw(cameraTextureId, textureView.getWidth(), textureView.getHeight());

                                        // Flush
                                        GLES20.glFlush();
                                        egl10.eglSwapBuffers(eglDisplay, eglSurface);
                                    }
                                    try {
                                        Thread.sleep(1000 / 30);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                                cameraSurfaceTexture.release();
                                GLES20.glDeleteTextures(1, new int[]{cameraTextureId}, 0);
                                egl10.eglDestroySurface(eglDisplay, eglSurface);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed( @NonNull CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(CaptureActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    },
                    null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == textureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    /**
     * Set flash to auto mode
     * @param requestBuilder
     */
    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            );
        }
    }

    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Bitmap mBitmap;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        private final int mOrientation;

        ImageSaver(Bitmap bitmap, File file, int orientation) {
            mBitmap      = bitmap;
            mFile        = file;
            mOrientation = orientation;
        }

        @Override
        public void run() {
            try {
                OutputStream outputStream = new FileOutputStream(mFile);
                Tool.rotateImage(mBitmap, mOrientation)
                        .compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Execute when user press to choose resolution 1
     * @param view
     */
    public void onChooseResolution1(View view) {
        if (mState == STATE_NOT_WORK) return;
        mState = STATE_NOT_WORK;
        numChoice = 0;
        mPreviewSize = optionResolutionCamera[numChoice];
        if (mCameraId != null) {
            mBackgroundThread.interrupt();
            BaseFilter.release();
            clearSurface(textureView.getSurfaceTexture());
            textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mBackgroundHandler.post(mCameraRender);
        } else {
            textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        constraintSet.connect(textureView.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM);
        constraintSet.applyTo(rootView);

        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    /**
     * Execute when user press to choose resolution 2
     * @param view
     */
    public void onChooseResolution2(View view) {
        if (mState == STATE_NOT_WORK) return;
        mState = STATE_NOT_WORK;
        numChoice = 1;
        mPreviewSize = optionResolutionCamera[numChoice];
        if (mCameraId != null) {
            mBackgroundThread.interrupt();
            BaseFilter.release();
            clearSurface(textureView.getSurfaceTexture());
            textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mBackgroundHandler.post(mCameraRender);
        } else {
            textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        constraintSet.connect(textureView.getId(), ConstraintSet.BOTTOM, actionView.getId(), ConstraintSet.TOP);
        constraintSet.applyTo(rootView);

        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    /**
     * Execute when user press to choose resolution 3
     * @param view
     */
    public void onChooseResolution3(View view) {
        if (mState == STATE_NOT_WORK) return;
        mState = STATE_NOT_WORK;
        numChoice = 2;
        mPreviewSize = optionResolutionCamera[numChoice];
        if (mCameraId != null) {
            mBackgroundThread.interrupt();
            BaseFilter.release();
            clearSurface(textureView.getSurfaceTexture());
            textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mBackgroundHandler.post(mCameraRender);
        } else {
            textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        constraintSet.connect(textureView.getId(), ConstraintSet.BOTTOM, actionView.getId(), ConstraintSet.TOP);
        constraintSet.applyTo(rootView);

        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
    }

    /**
     * Clear the given surface Texture by attaching a GL context and clearing the surface.
     * @param texture a valid SurfaceTexture
     */
    private void clearSurface(SurfaceTexture texture) {
        if(texture == null){
            return;
        }

        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(display, null);

        int[] attribList = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE,
                EGL10.EGL_WINDOW_BIT,
                EGL10.EGL_NONE, 0, // placeholder for recordable [@-3]
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        egl.eglChooseConfig(display, attribList, configs, configs.length, numConfigs);
        EGLConfig config = configs[0];
        EGLContext context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, new int[]{
                12440, 2,
                EGL10.EGL_NONE
        });
        EGLSurface eglSurface = egl.eglCreateWindowSurface(display, config, texture,
                new int[]{
                        EGL10.EGL_NONE
                });

        egl.eglMakeCurrent(display, eglSurface, eglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        egl.eglSwapBuffers(display, eglSurface);
        egl.eglDestroySurface(display, eglSurface);
        egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroyContext(display, context);
        egl.eglTerminate(display);
    }

    /**
     * Execute when user press to finish capture
     * @param view
     */
    public void onCaptureCancel(View view) {
        if (mState == STATE_PICTURE_TAKEN) {
            btnCapture.setVisibility(View.VISIBLE);
            btnSwith.setVisibility(View.VISIBLE);
            btnTick.setVisibility(View.GONE);
            resolutionView.setVisibility(View.VISIBLE);
            mBackgroundHandler.post(mCameraRender);
        } else {
            if (mFile.exists()) {
                mFile.delete();
            }
            orientationEventListener.disable();
            Tool.finishFailed(this);
        }
    }

    /**
     * Execute when user press to capture image
     * @param view
     */
    public void onCaptureExecute(View view) {
        mBackgroundThread.interrupt();
        BaseFilter.release();
        mBackgroundHandler.post(new ImageSaver(
                textureView.getBitmap(),
                mFile,
                mOrientation
        ));
        btnCapture.setVisibility(View.GONE);
        btnSwith.setVisibility(View.GONE);
        btnTick.setVisibility(View.VISIBLE);
        toggleFilterView(false, ANIMATION_DURATION);
        resolutionView.setVisibility(View.GONE);
        mState = STATE_PICTURE_TAKEN;
    }

    /**
     * Execute when user press to switch camera
     * @param view
     */
    public void onCaptureSwitch(View view) {
        if (mState == STATE_NOT_WORK) return;
        mState = STATE_NOT_WORK;
        mBackgroundThread.interrupt();
        closeCamera();
        BaseFilter.release();
        mCameraId = null;
        if (cameraChoice == CameraCharacteristics.LENS_FACING_BACK) {
            cameraChoice = CameraCharacteristics.LENS_FACING_FRONT;
        } else {
            cameraChoice = CameraCharacteristics.LENS_FACING_BACK;
        }
        openCamera(textureView.getWidth(), textureView.getHeight(), cameraChoice);
    }

    /**
     * Execute when user press to use current captured image
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCaptureTick(View view) {
        Intent data = new Intent();
        data.setData(Uri.fromFile(mFile));
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Execute when user press to show/hide filter options
     * @param view
     */
    public void onShowFilter(View view) {
        if (view.getId() == R.id.capture_activity_texture) {
            toggleFilterView(false, ANIMATION_DURATION);
        } else {
            toggleFilterView(filterView.getTranslationY() != 0.0f, ANIMATION_DURATION);
        }
    }

    private void toggleFilterView(boolean show, int duration) {
        if (show) {
            filterView
                    .animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
            ((View)btnCapture.getParent())
                    .animate()
                    .translationY((filterView.getHeight() - btnResolution1.getHeight()) / 2)
                    .setDuration(duration)
                    .start();
            ((View)btnCancel.getParent())
                    .animate()
                    .translationY((filterView.getHeight() - btnResolution1.getHeight()) / 2)
                    .setDuration(duration)
                    .start();
            ((View)btnSwith.getParent())
                    .animate()
                    .translationY((filterView.getHeight() - btnResolution1.getHeight()) / 2)
                    .setDuration(duration)
                    .start();
            resolutionView
                    .animate()
                    .translationY(resolutionView.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            resolutionView.setVisibility(View.GONE);
                            resolutionView.animate().setListener(null);
                        }
                    })
                    .start();
        } else {
            filterView
                    .animate()
                    .translationY(-filterView.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .start();
            ((View)btnCapture.getParent())
                    .animate()
                    .translationY(0)
                    .setDuration(duration)
                    .start();
            ((View)btnCancel.getParent())
                    .animate()
                    .translationY(0)
                    .setDuration(duration)
                    .start();
            ((View)btnSwith.getParent())
                    .animate()
                    .translationY(0)
                    .setDuration(duration)
                    .start();
            resolutionView.setVisibility(View.VISIBLE);
            resolutionView
                    .animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
        }
    }

    /**
     * Execute when user select one filter
     * @param view
     */
    public void onFilterSelected(View view) {
        setSelectedFilter(view.getId());
    }

    public void setSelectedFilter(int id) {
        filterId = id;
        filter = filterMap.get(id);
        if (filter != null)
            filter.onAttach();
    }

    private void initGL(SurfaceTexture texture) {
        egl10 = (EGL10) EGLContext.getEGL();

        eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed " +
                    android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError()));
        }

        int[] version = new int[2];
        if (!egl10.eglInitialize(eglDisplay, version)) {
            throw new RuntimeException("eglInitialize failed " +
                    android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError()));
        }

        int[] configsCount = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        int[] configSpec = {
                EGL10.EGL_RENDERABLE_TYPE,
                EGL_OPENGL_ES2_BIT,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 0,
                EGL10.EGL_STENCIL_SIZE, 0,
                EGL10.EGL_NONE
        };

        EGLConfig eglConfig = null;
        if (!egl10.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)) {
            throw new IllegalArgumentException("eglChooseConfig failed " +
                    android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError()));
        } else if (configsCount[0] > 0) {
            eglConfig = configs[0];
        }
        if (eglConfig == null) {
            throw new RuntimeException("eglConfig not initialized");
        }

        int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
        eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null);

        if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
            int error = egl10.eglGetError();
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                Log.e("eTalk", "eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW");
                return;
            }
            throw new RuntimeException("eglCreateWindowSurface failed " +
                    android.opengl.GLUtils.getEGLErrorString(error));
        }

        if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw new RuntimeException("eglMakeCurrent failed " +
                    android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError()));
        }
    }

    /**
     * Get orientation to rotate image base on device orientation
     * @param deviceOrientation
     * @return
     */
    private int getImageOrientation(int deviceOrientation) {
        //   225\ 180 /135
        //       \   /
        //        \ /
        //    270  o  90
        //        / \
        //       /   \
        //   315/  0  \45
        return (deviceOrientation + 45) % 360 / 90 * 90;
    }

    /**
     * Rotate some view
     * @param orientation
     */
    private void rotateView(int orientation) {
        if (orientation != mOrientation) {
            int degrees;
            if (mOrientation == 0 && orientation == 270) {
                degrees = 90;
            } else if (mOrientation == 270 && orientation == 0) {
                degrees = -360;
            } else {
                degrees = -orientation;
            }
            mOrientation = orientation;
            ((View)btnCancel.getParent())
                    .animate()
                    .rotation(degrees)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ((View)btnCancel.getParent()).setRotation(-mOrientation);
                        }
                    })
                    .start();
            ((View)btnSwith.getParent())
                    .animate()
                    .rotation(degrees)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ((View)btnSwith.getParent()).setRotation(-mOrientation);
                        }
                    })
                    .start();
        }
    }
}