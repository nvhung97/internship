package com.example.cpu11398_local.etalk.presentation.view.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.media.MediaRecorder;
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
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AutoFitTextureView;
import com.example.cpu11398_local.etalk.presentation.custom.ClockView;
import com.example.cpu11398_local.etalk.presentation.view.camera.Utils.MyGLUtils;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.AsciiArtFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.BaseFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.EdgeDetectionFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.LegofiedFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.OriginalFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.PolygonizationFilter;
import com.example.cpu11398_local.etalk.presentation.view.camera.filter.TrianglesMosaicFilter;
import com.example.cpu11398_local.etalk.utils.Tool;
import com.vincent.videocompressor.SamsungCameraTool;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class RecordActivity extends AppCompatActivity {

    /**
     * Animation duration
     */
    private final int ANIMATION_DURATION = 500;

    /**
     * Camera state: Camera not working.
     */
    private final int STATE_NOT_WORK = -1;

    /**
     * Camera state: Showing camera preview.
     */
    private final int STATE_PREVIEW = 0;

    /**
     * Camera state: Picture was taken.
     */
    private final int STATE_VIDEO_RECORDED = 1;

    /**
     * Views on this activity
     */
    private ConstraintLayout    rootView;
    private ConstraintLayout    actionView;
    private ConstraintLayout    filterView;
    private AutoFitTextureView  textureView;
    private ClockView           clockView;
    private Button              btnRecord;
    private Button              btnStopRecord;
    private Button              btnTick;
    private Button              btnSwith;
    private Button              btnCancel;

    /**
     * OpenGL ES 2.0
     */
    private EGL10 egl10;
    private EGLDisplay eglDisplay;
    private EGLContext eglContextPreview;
    private EGLSurface eglSurfacePreview;
    private EGLContext eglContextRecord;
    private EGLSurface eglSurfaceRecord;

    private int previewTextureId;
    private SurfaceTexture previewSurfaceTexture;
    private int recordTextureId;
    private SurfaceTexture recordSurfaceTexture;

    /**
     * Filters and map of filters
     */
    private BaseFilter filter;
    private SparseArray<BaseFilter> filterMap = new SparseArray<>();

    /**
     * Detect Device's orientation.
     */
    private OrientationEventListener orientationEventListener;
    private int mOrientation = 0;

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
     * Used to record video
     */
    private MediaRecorder mMediaRecorder;

    /**
     * Determine allow record video or not
     */
    private boolean isRecordVideoEnable = false;

    /**
     * Render on background
     */
    private Runnable mCameraRender = new Runnable() {
        @Override
        public void run() {
            if (isRecordVideoEnable) {
                setUpMediaRecorder();
                initGL(textureView.getSurfaceTexture(), mMediaRecorder.getSurface());
            } else {
                initGL(textureView.getSurfaceTexture(), null);
            }

            if (filterMap.size() != 0) {
                filterMap.clear();
            }
            boolean isFrontCamera = cameraChoice == CameraCharacteristics.LENS_FACING_FRONT;
            filterMap.append(R.id.record_activity_original_filter, new OriginalFilter(RecordActivity.this, isFrontCamera));
            filterMap.append(R.id.record_activity_legofied_filter, new LegofiedFilter(RecordActivity.this, isFrontCamera));
            filterMap.append(R.id.record_activity_trianglesmosaic_filter, new TrianglesMosaicFilter(RecordActivity.this, isFrontCamera));
            filterMap.append(R.id.record_activity_poligonization_filter, new PolygonizationFilter(RecordActivity.this, isFrontCamera));
            filterMap.append(R.id.record_activity_asciiart_filter, new AsciiArtFilter(RecordActivity.this, isFrontCamera));
            filterMap.append(R.id.record_activity_edgedetection_filter, new EdgeDetectionFilter(RecordActivity.this, isFrontCamera));

            setSelectedFilter(filterId);

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
     * Store current filter
     */
    private int filterId = R.id.record_activity_original_filter;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            startRenderThread();
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
            Tool.finishFailed(RecordActivity.this);
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
    private int mState = STATE_NOT_WORK;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;

    /**
     * Count time when record video
     */
    private Timer timer;
    private long recordingTime;
    private Handler updateTimeHandler = new Handler();

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private Size chooseOptimalSize(Size[] choices, int width, int height) {
        List<Size> bigEnough = new ArrayList<>();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * 3 / 4
                    && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(
                    bigEnough,
                    (lhs, rhs) -> {
                        // We cast here to ensure the multiplications won't overflow
                        return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
                    });
        } else {
            Toast.makeText(this, "Couldn't find any suitable preview size", Toast.LENGTH_SHORT).show();
            return choices[0];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        rootView        = findViewById(R.id.record_activity);
        actionView      = findViewById(R.id.record_activity_action);
        textureView     = findViewById(R.id.record_activity_texture);
        filterView      = findViewById(R.id.record_activity_lyt_filter);
        clockView       = findViewById(R.id.record_activity_time);
        btnRecord       = findViewById(R.id.record_activity_record);
        btnStopRecord   = findViewById(R.id.record_activity_stop_record);
        btnTick         = findViewById(R.id.record_activity_tick);
        btnSwith        = findViewById(R.id.record_activity_switch);
        btnCancel       = findViewById(R.id.record_activity_cancel);

        // Hide filter View
        filterView.post(() -> toggleFilterView(false, 0));

        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "eTalk"
        );
        if (!dir.exists() && !dir.mkdirs()) {
            Tool.finishFailed(this);
        } else {
            mFile = new File(dir, "VID_" + System.currentTimeMillis() + ".mp4");
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

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight(), cameraChoice);
        } else {
            textureView.setSurfaceTextureListener(mSurfaceTextureListener);

            // Set ratio for action layout
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getRealSize(size);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(rootView);
            constraintSet.setDimensionRatio(actionView.getId(), size.x + ":" + (size.y - size.x * 4 / 3));
            constraintSet.applyTo(rootView);
        }

        // Delete file if it existed
        cleanFile();
    }

    @Override
    public void onPause() {
        closeCamera();
        stopRenderThread();
        if (mState == STATE_PREVIEW) {
            if (isRecordVideoEnable) {
                isRecordVideoEnable = false;
                resetMediaRecorder();
                cleanFile();
                btnRecord.setVisibility(View.VISIBLE);
                btnStopRecord.setVisibility(View.GONE);
                btnSwith.setVisibility(View.VISIBLE);
                clockView.setVisibility(View.GONE);
            }
        } else if (mState == STATE_VIDEO_RECORDED) {
            btnRecord.setVisibility(View.VISIBLE);
            btnSwith.setVisibility(View.VISIBLE);
            btnTick.setVisibility(View.GONE);
            clockView.setVisibility(View.GONE);
        }
        mState = STATE_NOT_WORK;
        super.onPause();
    }

    public void onBackPressed(View view) {
        cleanFile();
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
                    throw new RuntimeException("Cannot get available preview/video sizes");
                }

                mPreviewSize = chooseOptimalSize(
                        map.getOutputSizes(SurfaceTexture.class),
                        height,
                        width
                );

                textureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());

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
     * Closes the current {@link CameraCaptureSession}
     */
    private void closePreviewSession() {
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startRenderThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        mBackgroundHandler.post(mCameraRender);
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopRenderThread() {
        if (mBackgroundThread != null) {
            try {
                mCameraOpenCloseLock.acquire();
                mBackgroundThread.interrupt();
                mCameraOpenCloseLock.acquire();
                mCameraOpenCloseLock.release();
                closePreviewSession();
                BaseFilter.release();
                mBackgroundThread.quitSafely();
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            List<Surface> surfaces = new ArrayList<>();

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

            // We configure the size of default buffer to be the size of camera preview we want.
            previewSurfaceTexture.setDefaultBufferSize(textureView.getHeight(), textureView.getWidth());

            // This is the output Surface we need to start preview.
            Surface previewSurface = new Surface(previewSurfaceTexture);

            mPreviewRequestBuilder.addTarget(previewSurface);
            surfaces.add(previewSurface);

            if (isRecordVideoEnable) {
                recordSurfaceTexture.setDefaultBufferSize(textureView.getHeight(), textureView.getWidth());
                Surface recordSurface = new Surface(recordSurfaceTexture);
                mPreviewRequestBuilder.addTarget(recordSurface);
                surfaces.add(recordSurface);
            }

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(
                    surfaces,
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {

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

                                // Start record video if allowed
                                if (isRecordVideoEnable) {
                                    timer = new Timer(true);
                                    recordingTime = 0;
                                    timer.scheduleAtFixedRate(
                                            new TimerTask() {
                                                @Override
                                                public void run() {
                                                    updateTimeHandler.post(()-> clockView.setCountTime(++recordingTime));
                                                }
                                            },
                                            1000,
                                            1000
                                    );
                                    mMediaRecorder.start();
                                }

                                // Render loop
                                long timestamp;
                                while (!Thread.currentThread().isInterrupted()) {
                                    timestamp = System.currentTimeMillis();
                                    eglMakeCurrentPreview();

                                    // Update the camera preview texture
                                    synchronized (this) {
                                        previewSurfaceTexture.updateTexImage();
                                    }

                                    // Draw camera preview
                                    filter.draw(previewTextureId, textureView.getWidth(), textureView.getHeight());

                                    // Flush
                                    GLES20.glFlush();
                                    egl10.eglSwapBuffers(eglDisplay, eglSurfacePreview);

                                    if (isRecordVideoEnable) {
                                        eglMakeCurrentRecord();

                                        // Update the camera preview texture
                                        synchronized (this) {
                                            recordSurfaceTexture.updateTexImage();
                                        }

                                        // Draw camera preview
                                        filter.draw(recordTextureId, textureView.getWidth(), textureView.getHeight());

                                        // Flush
                                        GLES20.glFlush();
                                        egl10.eglSwapBuffers(eglDisplay, eglSurfaceRecord);
                                    }
                                    try {
                                        // try to make fps = 30
                                        timestamp = 1000 / 30 - (System.currentTimeMillis() - timestamp);
                                        if (timestamp > 0) {
                                            Thread.sleep(timestamp);
                                        }
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                                eglMakeCurrentPreview();
                                previewSurfaceTexture.release();
                                GLES20.glDeleteTextures(1, new int[]{previewTextureId}, 0);
                                egl10.eglDestroySurface(eglDisplay, eglSurfacePreview);
                                if (isRecordVideoEnable) {
                                    eglMakeCurrentRecord();
                                    recordSurfaceTexture.release();
                                    GLES20.glDeleteTextures(1, new int[]{recordTextureId}, 0);
                                    egl10.eglDestroySurface(eglDisplay, eglSurfaceRecord);
                                }
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            } finally {
                                mCameraOpenCloseLock.release();
                            }
                        }

                        @Override
                        public void onConfigureFailed( @NonNull CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(RecordActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
     * Execute when user press to finish capture
     * @param view
     */
    public void onRecordCancel(View view) {
        if (mState == STATE_PREVIEW) {
            if (isRecordVideoEnable) {
                resetMediaRecorder();
                stopRenderThread();
                cleanFile();
                btnRecord.setVisibility(View.VISIBLE);
                btnSwith.setVisibility(View.VISIBLE);
                btnStopRecord.setVisibility(View.GONE);
                clockView.setVisibility(View.GONE);
                isRecordVideoEnable = false;
                startRenderThread();
            } else {
                cleanFile();
                orientationEventListener.disable();
                Tool.finishFailed(this);
            }
        } else if (mState == STATE_VIDEO_RECORDED) {
            btnRecord.setVisibility(View.VISIBLE);
            btnSwith.setVisibility(View.VISIBLE);
            btnTick.setVisibility(View.GONE);
            clockView.setVisibility(View.GONE);
            cleanFile();
            startRenderThread();
        }
    }

    /**
     * Execute when user press to capture image
     * @param view
     */
    public void onRecordExecute(View view) {
        stopRenderThread();
        mState = STATE_NOT_WORK;
        isRecordVideoEnable = true;
        btnRecord.setVisibility(View.GONE);
        btnSwith.setVisibility(View.GONE);
        btnStopRecord.setVisibility(View.VISIBLE);
        clockView.setVisibility(View.VISIBLE);
        clockView.setCountTime(0L);
        startRenderThread();
    }

    /**
     * Set up MediaRecoder
     * @throws IOException
     */
    private void setUpMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(mFile.getAbsolutePath());
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(textureView.getWidth(), textureView.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setOrientationHint(mOrientation);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop MediaRecorder
     */
    private void resetMediaRecorder() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
        timer.cancel();
    }

    /**
     * Execute when user press to stop record
     * @param view
     */
    public void onRecordStop(View view) {
        resetMediaRecorder();
        stopRenderThread();
        btnStopRecord.setVisibility(View.GONE);
        btnTick.setVisibility(View.VISIBLE);
        toggleFilterView(false, ANIMATION_DURATION);
        mState = STATE_VIDEO_RECORDED;
        isRecordVideoEnable = false;
    }

    /**
     * Execute when user press to switch camera
     * @param view
     */
    public void onRecordSwitch(View view) {
        if (mState == STATE_NOT_WORK) return;
        mState = STATE_NOT_WORK;
        stopRenderThread();
        closeCamera();
        mCameraId = null;
        if (cameraChoice == CameraCharacteristics.LENS_FACING_BACK) {
            cameraChoice = CameraCharacteristics.LENS_FACING_FRONT;
        } else {
            cameraChoice = CameraCharacteristics.LENS_FACING_BACK;
        }
        openCamera(textureView.getWidth(), textureView.getHeight(), cameraChoice);
    }

    /**
     * Execute when user press to use current recorded video
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRecordTick(View view) {
        mFile = SamsungCameraTool.FixSamsungBug(mFile);
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
        if (view.getId() == R.id.record_activity_texture) {
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
            ((View)btnRecord.getParent())
                    .animate()
                    .translationY(filterView.getHeight() / 2)
                    .setDuration(duration)
                    .start();
            ((View)btnCancel.getParent())
                    .animate()
                    .translationY(filterView.getHeight() / 2)
                    .setDuration(duration)
                    .start();
            ((View)btnSwith.getParent())
                    .animate()
                    .translationY(filterView.getHeight() / 2)
                    .setDuration(duration)
                    .start();
        } else {
            filterView
                    .animate()
                    .translationY(-filterView.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .start();
            ((View)btnRecord.getParent())
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

    /**
     * Init OpenGL with surfaces
     * @param previewTexture
     * @param recordTexture
     */
    private void initGL(Object previewTexture, Object recordTexture) {
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
                EGL10.EGL_RENDERABLE_TYPE, 4,
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

        int[] attrib_list = {0x3098, 2, EGL10.EGL_NONE};

        eglContextPreview = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
        eglSurfacePreview = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, previewTexture, null);

        if (eglSurfacePreview == null || eglSurfacePreview == EGL10.EGL_NO_SURFACE) {
            int error = egl10.eglGetError();
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                Log.e("eTalk", "eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW");
                return;
            }
            throw new RuntimeException("eglCreateWindowSurface failed " +
                    android.opengl.GLUtils.getEGLErrorString(error));
        }

        if (recordTexture != null) {
            eglContextRecord = egl10.eglCreateContext(eglDisplay, eglConfig, eglContextPreview, attrib_list);
            eglSurfaceRecord = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, recordTexture, null);

            if (eglSurfaceRecord == null || eglSurfaceRecord == EGL10.EGL_NO_SURFACE) {
                int error = egl10.eglGetError();
                if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                    Log.e("eTalk", "eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW");
                    return;
                }
                throw new RuntimeException("eglCreateWindowSurface failed " +
                        android.opengl.GLUtils.getEGLErrorString(error));
            }
        }

        eglMakeCurrentPreview();
        previewTextureId = MyGLUtils.genTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        previewSurfaceTexture = new SurfaceTexture(previewTextureId);

        if (recordTexture != null) {
            eglMakeCurrentRecord();
            recordTextureId = MyGLUtils.genTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
            recordSurfaceTexture = new SurfaceTexture(recordTextureId);
        }
    }

    private void eglMakeCurrentPreview() {
        if (!egl10.eglMakeCurrent(eglDisplay, eglSurfacePreview, eglSurfacePreview, eglContextPreview)) {
            throw new RuntimeException("eglMakeCurrent failed " +
                    android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError()));
        }
    }

    private void eglMakeCurrentRecord() {
        if (!egl10.eglMakeCurrent(eglDisplay, eglSurfaceRecord, eglSurfaceRecord, eglContextRecord)) {
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
        //   225\ 180  /135
        //       \   /
        //        \ /
        //    270  o   90
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

    /**
     * Delete unnecessary file
     */
    private void cleanFile() {
        if (mFile.exists()) {
            mFile.delete();
        }
    }
}