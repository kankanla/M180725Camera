package com.kankanla.e560.m180725camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
onCreate
onStart
onResume
onPause
onStop
onDestroy
 */


public class SurfaceView_demo2 extends AppCompatActivity {
    private final String TAG = "-aaa-SurfaceView_demo-";
    private String[] APP_Permission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private final int mRequestCode = 22;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private SurfaceView mSurfaceView;
    private Surface mSurface;
    private Button mButtonA, mButtonB;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private StreamConfigurationMap mStreamConfigurationMap;
    private String[] mGetCameraIdList;
    protected SurfaceHolder mSurfaceHolder;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_surface_view_demo2);
        setTitle(TAG);
    }

    protected void set_view() {
        setContentView(R.layout.activity_surface_view_demo2);
        mButtonA = findViewById(R.id.button);
        mButtonB = findViewById(R.id.button13);
        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SurfaceView_demo2.this, "xxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startBackgroundThread();
        Log.d(TAG, this.isFinishing() + "--------------------------rrrrrrrrrr--------------");
        set_view();
        openCamera(new Size(12, 12));
    }


    private CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(@NonNull final CameraDevice camera) {
            Log.d(TAG, "mCameraStateCallback");
            mCameraDevice = camera;
            try {
                mCameraDevice.createCaptureSession(Arrays.asList(mSurface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        mCameraCaptureSession = session;
                        try {
                            CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                            builder.addTarget(mSurface);
                            Log.d(TAG, mSurface.isValid() + "-----------------------------------------");
                            mCameraCaptureSession.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                                @Override
                                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                                }
                            }, mBackgroundHandler);

                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d(TAG, "onDisconnected");
            Log.d(TAG, "mCameraDevice.close()");
            mCameraDevice.close();
            mCameraCaptureSession.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void openCamera(Size sizes) {
        Log.d(TAG, "openCamera");
        try {
            if (!A_checkSelfPermission(APP_Permission)) {
                A_requestPermissions(APP_Permission);
                return;
            }

            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            mCameraManager = (CameraManager) getApplicationContext().getSystemService(CAMERA_SERVICE);
            mSurfaceView = findViewById(R.id.surfaceView2);

            mGetCameraIdList = mCameraManager.getCameraIdList();
            CameraCharacteristics mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mGetCameraIdList[0]);
            mStreamConfigurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size[] sizes1 = mStreamConfigurationMap.getOutputSizes(SurfaceHolder.class);

            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mSurfaceHolder = holder;
                    mSurface = mSurfaceHolder.getSurface();
                    try {
                        mCameraManager.openCamera(mGetCameraIdList[0], mCameraStateCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    mSurfaceHolder = holder;
                    mSurface = mSurfaceHolder.getSurface();
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mSurfaceHolder = holder;
                }
            });


        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mCameraOpenCloseLock.release();
        mCameraOpenCloseLock.isFair();
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
        }
        mCameraManager = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mCameraOpenCloseLock.release();
        mCameraManager = null;
    }

    private void startBackgroundThread() {
        Log.d(TAG, "startBackgroundThread");
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        Log.d(TAG, "stopBackgroundThread");
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void A_requestPermissions(String[] permissions) {
        Log.d(TAG, "A_requestPermissions");
        ActivityCompat.requestPermissions(this, permissions, mRequestCode);
    }

    protected boolean A_checkSelfPermission(String[] permission) {
        Log.d(TAG, "A_checkSelfPermission");
        for (String t : permission) {
            if (ActivityCompat.checkSelfPermission(this, t) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}

