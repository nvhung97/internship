package com.example.cpu11398_local.etalk.presentation.view.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
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
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AutoFitTextureView;
import com.example.cpu11398_local.etalk.utils.Tool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CaptureActivity extends AppCompatActivity {

    private final int ANIMATION_DURATION = 500;

    /**
     * Views on this activity
     */
    private ConstraintLayout    rootView;
    private ConstraintLayout    actionView;
    private ConstraintLayout    filterView;
    private AutoFitTextureView  textureView;
    private Button btnCapture;
    private Button btnTick;
    private Button btnSwith;
    private Button btnCancel;
    private Button btnResolution1;
    private Button btnResolution2;
    private Button btnResolution3;

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

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
     * The optionResolutionImage {@link android.util.Size} user can choose.
     */
    private Size[] optionResolutionImage;

    /**
     * Current optionResolutionCamera size
     */
    private int numChoice = 0;

    /**
     * Store current state of filter view
     */
    private boolean isFilterOn = false;

    /**
     * Store current filter
     */
    private int filterId = 0;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
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
     * An {@link ImageReader} that handles still image capture.
     */
    private ImageReader mImageReader;

    /**
     * This is the output file for our picture.
     */
    private File mFile;

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }
    };

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
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;

    /**
     * Where the current camera device supports auto focus
     */
    private boolean mAutoFocusSupported;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    /**
     * Get some size from {@code choices} such as largest sizes has ratio 16:9, 4:3, 1:1.
     * @param cameraChoices the list of sizes that the camera supports for the intended output class
     */
    private void chooseOptionSize(Size[] cameraChoices, Size[] imageChoices) {
        optionResolutionCamera = new Size[3];
        optionResolutionImage = new Size[3];
        for (Size choice : cameraChoices) {
            if (optionResolutionCamera[0] == null) {
                if (choice.getWidth() * 9 / 16 == choice.getHeight()) {
                    optionResolutionCamera[0] = choice;
                    optionResolutionImage[0] = new Size(16, 9);
                }
            }
            if (optionResolutionCamera[1] == null) {
                if (choice.getWidth() * 3 / 4 == choice.getHeight()) {
                    optionResolutionCamera[1] = choice;
                    optionResolutionImage[1] = new Size(4, 3);
                }
            }
            if (optionResolutionCamera[2] == null) {
                if (choice.getWidth() * 1 / 1 == choice.getHeight()) {
                    optionResolutionCamera[2] = choice;
                    optionResolutionImage[2] = new Size(1, 1);
                }
            }
        }

        if (optionResolutionImage[0] != null && optionResolutionCamera[0] != null) {
            btnResolution1.setEnabled(true);
            btnResolution1.setText(optionResolutionImage[0].getWidth() + ":" + optionResolutionImage[0].getHeight());
        } else {
            btnResolution1.setEnabled(false);
        }
        if (optionResolutionImage[1] != null && optionResolutionCamera[1] != null) {
            btnResolution1.setEnabled(true);
            btnResolution2.setText(optionResolutionImage[1].getWidth() + ":" + optionResolutionImage[1].getHeight());
        } else {
            btnResolution2.setEnabled(false);
        }
        if (optionResolutionImage[2] != null && optionResolutionCamera[2] != null) {
            btnResolution1.setEnabled(true);
            btnResolution3.setText(optionResolutionImage[2].getWidth() + ":" + optionResolutionImage[2].getHeight());
        } else {
            btnResolution3.setEnabled(false);
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
        Tool.setRotationAnimation(this);

        if (savedInstanceState != null) {
            numChoice = savedInstanceState.getInt("num_choice");
            cameraChoice = savedInstanceState.getInt("camera_choice");
            isFilterOn = savedInstanceState.getBoolean("is_filter_on");
            filterId = savedInstanceState.getInt("filter_id");
        }

        rootView        = findViewById(R.id.capture_activity);
        actionView      = findViewById(R.id.capture_activity_action);
        textureView     = findViewById(R.id.capture_activity_texture);
        filterView      = findViewById(R.id.capture_activity_lyt_filter);
        btnCapture      = findViewById(R.id.capture_activity_capture);
        btnTick         = findViewById(R.id.capture_activity_tick);
        btnSwith        = findViewById(R.id.capture_activity_switch);
        btnCancel       = findViewById(R.id.capture_activity_cancel);
        btnResolution1  = findViewById(R.id.capture_activity_resolution_1);
        btnResolution2  = findViewById(R.id.capture_activity_resolution_2);
        btnResolution3  = findViewById(R.id.capture_activity_resolution_3);

        // Hide filter View
        filterView.post(() -> toggleFilterView(isFilterOn, 0));

        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "eTalk"
        );
        if (!dir.exists() && !dir.mkdirs()) {
            Tool.finishFailed(this);
        } else {
            mFile = new File(dir, "IMG_" + System.currentTimeMillis() + ".jpg");
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            constraintSet.setDimensionRatio(actionView.getId(), size.x + ":" + (size.y - size.x * 4 / 3));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintSet.setDimensionRatio(actionView.getId(), (size.x - size.y * 4 / 3) + ":" + size.y);
        }
        constraintSet.applyTo(rootView);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("num_choice", numChoice);
        savedInstanceState.putInt("camera_choice", cameraChoice);
        savedInstanceState.putBoolean("is_filter_on", isFilterOn);
        savedInstanceState.putInt("filter_id", filterId);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        if (mState == STATE_PICTURE_TAKEN) {
            btnCapture.setVisibility(View.VISIBLE);
            btnSwith.setVisibility(View.VISIBLE);
            btnTick.setVisibility(View.GONE);
            btnResolution1.setVisibility(View.VISIBLE);
            btnResolution2.setVisibility(View.VISIBLE);
            btnResolution3.setVisibility(View.VISIBLE);
        }
        super.onPause();
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

                chooseOptionSize(
                        map.getOutputSizes(SurfaceTexture.class),
                        map.getOutputSizes(ImageFormat.JPEG)
                );

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

                // Check if the auto focus is supported
                int[] afAvailableModes = characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);

                if (afAvailableModes.length == 0 || (afAvailableModes.length == 1
                        && afAvailableModes[0] == CameraMetadata.CONTROL_AF_MODE_OFF)) {
                    mAutoFocusSupported = false;
                } else {
                    mAutoFocusSupported = true;
                }

                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
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
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
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
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Prepare image reader
            mImageReader = ImageReader.newInstance(
                    optionResolutionImage[numChoice].getWidth(),
                    optionResolutionImage[numChoice].getHeight(),
                    ImageFormat.JPEG,
                    2 /*maxImages*/);
            mImageReader.setOnImageAvailableListener(
                    mOnImageAvailableListener,
                    mBackgroundHandler
            );

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(
                    Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

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
                                        mCaptureCallback,
                                        mBackgroundHandler)
                                ;
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
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START
            );
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(
                    mPreviewRequestBuilder.build(),
                    mCaptureCallback,
                    mBackgroundHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START
            );
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(
                    mPreviewRequestBuilder.build(),
                    mCaptureCallback,
                    mBackgroundHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            if (null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            );
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    mState = STATE_PICTURE_TAKEN;
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
            );
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(
                    mPreviewRequestBuilder.build(),
                    mCaptureCallback,
                    mBackgroundHandler
            );
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(
                    mPreviewRequest,
                    mCaptureCallback,
                    mBackgroundHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Execute when user press to choose resolution 1
     * @param view
     */
    public void onChooseResolution1(View view) {
        numChoice = 0;
        mPreviewSize = optionResolutionCamera[numChoice];
        textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        if (mCameraId != null) {
            createCameraPreviewSession();
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            constraintSet.connect(textureView.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintSet.connect(textureView.getId(), ConstraintSet.RIGHT, rootView.getId(), ConstraintSet.RIGHT);
        }
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
        numChoice = 1;
        mPreviewSize = optionResolutionCamera[numChoice];
        textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        if (mCameraId != null) {
            createCameraPreviewSession();
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            constraintSet.connect(textureView.getId(), ConstraintSet.BOTTOM, actionView.getId(), ConstraintSet.TOP);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintSet.connect(textureView.getId(), ConstraintSet.RIGHT, actionView.getId(), ConstraintSet.LEFT);
        }
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
        numChoice = 2;
        mPreviewSize = optionResolutionCamera[numChoice];
        textureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        if (mCameraId != null) {
            createCameraPreviewSession();
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            constraintSet.connect(textureView.getId(), ConstraintSet.BOTTOM, actionView.getId(), ConstraintSet.TOP);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintSet.connect(textureView.getId(), ConstraintSet.RIGHT, actionView.getId(), ConstraintSet.LEFT);
        }
        constraintSet.applyTo(rootView);

        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
    }

    /**
     * Execute when user press to finish capture
     * @param view
     */
    public void onCaptureCancel(View view) {
        if (mState == STATE_PICTURE_TAKEN) {
            unlockFocus();
            btnCapture.setVisibility(View.VISIBLE);
            btnSwith.setVisibility(View.VISIBLE);
            btnTick.setVisibility(View.GONE);
            btnResolution1.setVisibility(View.VISIBLE);
            btnResolution2.setVisibility(View.VISIBLE);
            btnResolution3.setVisibility(View.VISIBLE);
        } else {
            if (mFile.exists()) {
                mFile.delete();
            }
            Tool.finishFailed(this);
        }
    }

    /**
     * Execute when user press to capture image
     * @param view
     */
    public void onCaptureExecute(View view) {
        if (mAutoFocusSupported) {
            lockFocus();
        } else {
            captureStillPicture();
        }
        btnCapture.setVisibility(View.GONE);
        btnSwith.setVisibility(View.GONE);
        btnTick.setVisibility(View.VISIBLE);
        btnResolution1.setVisibility(View.GONE);
        btnResolution2.setVisibility(View.GONE);
        btnResolution3.setVisibility(View.GONE);
    }

    /**
     * Execute when user press to switch camera
     * @param view
     */
    public void onCaptureSwitch(View view) {
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
        toggleFilterView(isFilterOn = !isFilterOn, ANIMATION_DURATION);
    }

    private void toggleFilterView(boolean show, int duration) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            toggleFilterViewPortrait(show, duration);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toggleFilterViewLandscape(show, duration);
        }
    }

    private void toggleFilterViewPortrait(boolean show, int duration) {
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
            btnResolution1
                    .animate()
                    .translationY(btnResolution1.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnResolution1.setVisibility(View.GONE);
                            btnResolution1.animate().setListener(null);
                        }
                    })
                    .start();
            btnResolution2
                    .animate()
                    .translationY(btnResolution2.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnResolution2.setVisibility(View.GONE);
                            btnResolution2.animate().setListener(null);
                        }
                    })
                    .start();
            btnResolution3
                    .animate()
                    .translationY(btnResolution3.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnResolution3.setVisibility(View.GONE);
                            btnResolution3.animate().setListener(null);
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
            btnResolution1.setVisibility(View.VISIBLE);
            btnResolution2.setVisibility(View.VISIBLE);
            btnResolution3.setVisibility(View.VISIBLE);
            btnResolution1
                    .animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
            btnResolution2
                    .animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
            btnResolution3
                    .animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
        }
    }

    private void toggleFilterViewLandscape(boolean show, int duration) {
        if (show) {
            filterView
                    .animate()
                    .translationX(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
            ((View)btnCapture.getParent())
                    .animate()
                    .translationX((filterView.getWidth() - btnResolution1.getHeight()) / 2)
                    .setDuration(duration)
                    .start();
            ((View)btnCancel.getParent())
                    .animate()
                    .translationX((filterView.getWidth() - btnResolution1.getHeight()) / 2)
                    .setDuration(duration)
                    .start();
            ((View)btnSwith.getParent())
                    .animate()
                    .translationX((filterView.getWidth() - btnResolution1.getHeight()) / 2)
                    .setDuration(duration)
                    .start();
            btnResolution1
                    .animate()
                    .translationX(btnResolution1.getTranslationX() + btnResolution1.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnResolution1.setVisibility(View.GONE);
                            btnResolution1.animate().setListener(null);
                        }
                    })
                    .start();
            btnResolution2
                    .animate()
                    .translationX(btnResolution2.getTranslationX() + btnResolution2.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnResolution2.setVisibility(View.GONE);
                            btnResolution2.animate().setListener(null);
                        }
                    })
                    .start();
            btnResolution3
                    .animate()
                    .translationX(btnResolution3.getTranslationX() + btnResolution3.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnResolution3.setVisibility(View.GONE);
                            btnResolution3.animate().setListener(null);
                        }
                    })
                    .start();
        } else {
            filterView
                    .animate()
                    .translationX(-filterView.getWidth())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .start();
            ((View)btnCapture.getParent())
                    .animate()
                    .translationX(0)
                    .setDuration(duration)
                    .start();
            ((View)btnCancel.getParent())
                    .animate()
                    .translationX(0)
                    .setDuration(duration)
                    .start();
            ((View)btnSwith.getParent())
                    .animate()
                    .translationX(0)
                    .setDuration(duration)
                    .start();
            btnResolution1.setVisibility(View.VISIBLE);
            btnResolution2.setVisibility(View.VISIBLE);
            btnResolution3.setVisibility(View.VISIBLE);
            btnResolution1
                    .animate()
                    .translationX(btnResolution1.getTranslationX() - btnResolution1.getHeight())
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
            btnResolution2
                    .animate()
                    .translationX(btnResolution2.getTranslationX() - btnResolution2.getHeight())
                    .alpha(1.0f)
                    .setDuration(duration)
                    .start();
            btnResolution3
                    .animate()
                    .translationX(btnResolution3.getTranslationX() - btnResolution3.getHeight())
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
        filterId = view.getId();
    }
}