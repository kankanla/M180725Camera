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
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class SurfaceView_demo extends AppCompatActivity {
    private String TAG = "activity_surface_view_demo";
    private SurfaceView surfaceView = null;
    private Surface msurface = null;
    private SurfaceHolder surfaceHolder = null;
    private CameraManager cameraManager = null;
    private StreamConfigurationMap streamConfigurationMap = null;
    private CameraCharacteristics cameraCharacteristics = null;
    private HandlerThread handlerThread = null;
    private Handler Background_handler = null;
    private CameraDevice mcamera = null;
    private String[] cameraIdList;

    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkpermission();
        setContentView(R.layout.activity_surface_view_demo);
        setTitle(getLocalClassName());
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        try {
            T1();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackground_handler();
    }

    protected void checkpermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, 22);
            return;
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void T1() throws CameraAccessException {
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        cameraIdList = cameraManager.getCameraIdList();
        cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[0]);
        streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        startBackground_handler();
        surfaceHolder.addCallback(surfaceHolder_callback);
    }

    private SurfaceHolder.Callback surfaceHolder_callback = new SurfaceHolder.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("MissingPermission")
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceHolder = holder;
            msurface = surfaceHolder.getSurface();
            try {
                cameraManager.openCamera(cameraIdList[0], cameraDevice_StateCallback, Background_handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };
    private CameraDevice.StateCallback cameraDevice_StateCallback = new CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mcamera = camera;
            ArrayList<Surface> surfaces = new ArrayList<>();
            surfaces.add(msurface);
            try {
                Size[] sizes = streamConfigurationMap.getOutputSizes(SurfaceHolder.class);
                Size temp = sizes[sizes.length - (sizes.length - 1)];
                camera.createCaptureSession(surfaces, cameraCaptureSession_stateCallback, Background_handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    private CameraCaptureSession.StateCallback cameraCaptureSession_stateCallback = new CameraCaptureSession.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            try {
                CaptureRequest.Builder builder = mcamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                builder.addTarget(msurface);

                session.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);

                    }
                }, Background_handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };


    private void startBackground_handler() {
        handlerThread = new HandlerThread("Background_handler");
        handlerThread.start();
        Background_handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
    }

    private void stopBackground_handler() {
        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            Background_handler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}