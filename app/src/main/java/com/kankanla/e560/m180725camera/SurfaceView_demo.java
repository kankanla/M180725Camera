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
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class SurfaceView_demo extends AppCompatActivity {
    private String TAG = "-aaa_surface_view_demo-";
    private String TAG2 = "-bbb_surface_view_demo-";

    private SurfaceView m_surfaceView;
    private SurfaceHolder m_surfaceHolder;
    private Button m_button;
    private CameraDevice m_cameraDevice;
    private CaptureRequest m_captureRequest;
    private CameraCaptureSession m_cameraCaptureSession;
    private CameraManager m_cameraManager;
    private String[] m_cameraIDS;
    private CameraCharacteristics m_cameraCharacteristics;
    private StreamConfigurationMap m_streamConfigurationMap;
    private final int m_Front_Camera = 0;
    private final int m_Back_Camera = 1;

    private Size[] m_surfaceHolder_sizes;
    private Size[] m_mediaRecorder_sizes;

    private HandlerThread m_back_handlerThread;
    private Handler m_back_handler;
    private Semaphore m_camera_semaphore = new Semaphore(1);

    private final int m_PermissionsrequestCODE = 333;
    private final String[] m_setSelfPermission = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_surface_view_demo);
        getSupportActionBar().hide();
        if (m_surfaceView == null || m_button == null) {
            set_init();
        }
    }

    protected void set_init() {
        Log.d(TAG, "set_init");
        m_button = findViewById(R.id.button333);
        m_surfaceView = findViewById(R.id.surfaceView);
        m_surfaceHolder = m_surfaceView.getHolder();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        start_back_handle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getSurfe_size() {
        if (m_surfaceHolder == null) {
            return;
        }
        m_surfaceHolder_sizes = m_streamConfigurationMap.getOutputSizes(SurfaceHolder.class);
        m_mediaRecorder_sizes = m_streamConfigurationMap.getOutputSizes(MediaRecorder.class);
    }

    private CameraDevice.StateCallback m_CameraDevice_stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {

        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void m_cameraOPEN(int Camera_ID) {
        Log.d(TAG, "m_cameraOPEN");
        if (!chk_SelfPermission(m_setSelfPermission)) {
            return;
        }

        m_cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            if (!m_camera_semaphore.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            m_cameraIDS = m_cameraManager.getCameraIdList();
            Range<Integer> id = new Range<>(0, m_cameraIDS.length - 1);
            m_cameraCharacteristics = m_cameraManager.getCameraCharacteristics(m_cameraIDS[id.clamp(Camera_ID)]);
            m_streamConfigurationMap = m_cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            getSurfe_size();

            m_cameraManager.openCamera(m_cameraIDS[id.clamp(Camera_ID)], m_CameraDevice_stateCallback, m_back_handler);

        } catch (InterruptedException e) {
            Log.d(TAG2, "tryAcquire");
            e.printStackTrace();
        } catch (CameraAccessException e) {
            Log.d(TAG2, "getCameraIdList");
            e.printStackTrace();
        }
    }

    protected void start_back_handle() {
        Log.d(TAG, "start_back_handle");
        stop_back_handle();
        m_back_handlerThread = new HandlerThread("CAMERA_BACK");
        m_back_handlerThread.start();
        m_back_handler = new Handler(m_back_handlerThread.getLooper());
    }

    protected void stop_back_handle() {
        Log.d(TAG, "stop_back_handle");
        if (m_back_handler != null || m_back_handlerThread != null) {
            m_back_handlerThread.quitSafely();
            m_back_handlerThread = null;
            m_back_handler = null;
        }
    }


    protected boolean chk_SelfPermission(String[] Permissions) {
        Log.d(TAG, "chk_SelfPermission");
        for (String temp : Permissions) {
            if (ActivityCompat.checkSelfPermission(this, temp) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "chk_SelfPermission == false");
                return false;
            }
        }
        Log.d(TAG, "chk_SelfPermission == true");
        return true;
    }

    protected void set_SelfPermission(String[] Permissions) {
        Log.d(TAG, "set_SelfPermission");
        ActivityCompat.requestPermissions(this, Permissions, m_PermissionsrequestCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        switch (requestCode) {
            case m_PermissionsrequestCODE:
                Log.d(TAG, "onRequestPermissionsResult CODE == " + requestCode);
                break;

            default:
                Log.d(TAG, "onRequestPermissionsResult CODE == default");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}