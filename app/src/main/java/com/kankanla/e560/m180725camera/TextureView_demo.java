package com.kankanla.e560.m180725camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
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
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class TextureView_demo extends AppCompatActivity {
    private final String TAG = "-aaa-TextureView_demo-";
    private TextureView textureViewM;
    private Button buttonaM, buttonbM;
    private int requestCodeM = 22;
    private HandlerThread handlerThreadM;
    private Handler backhandlerM;
    private Semaphore semaphoreM = new Semaphore(1);
    private String[] req_permission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private CameraDevice cameraDeviceM;
    private CaptureRequest.Builder builderM;
    private CameraManager cameraManagerM;
    private CameraCaptureSession cameraCaptureSessionM;
    private StreamConfigurationMap streamConfigurationMapM;
    private Surface surfaceM;
    private SurfaceTexture surfaceTextureM;
    private CameraCharacteristics cameraCharacteristicsM;
    private String[] getCameraIdListM;

    protected void find_view() {
        setContentView(R.layout.activity_texture_view_demo);
        textureViewM = findViewById(R.id.textureView);
        buttonaM = findViewById(R.id.button14);
        buttonbM = findViewById(R.id.button15);

        buttonbM.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Toast.makeText(TextureView_demo.this, "TAG", Toast.LENGTH_SHORT).show();
                if (builderM.get(CaptureRequest.FLASH_MODE) == CaptureRequest.FLASH_MODE_OFF) {
                    builderM.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                } else {
                    builderM.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                }

                builderM.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
                req(builderM);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void req(CaptureRequest.Builder builder) {
        try {
            cameraCaptureSessionM.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }


            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view_demo);
        find_view();
    }

    protected boolean A_checkSelfPermission(String[] permission) {
        for (String temp : permission) {
            if (ActivityCompat.checkSelfPermission(this, temp) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    protected void A_requestPermissions(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, requestCodeM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult -- " + requestCode);
    }

    protected void open_camera() {
        if (!A_checkSelfPermission(req_permission)) {
            A_requestPermissions(req_permission);
            return;
        }

        find_view();
        Start_backHandler();
        try {
            semaphoreM.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textureViewM.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @SuppressLint("MissingPermission")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                surfaceTextureM = surface;
                cameraManagerM = (CameraManager) getSystemService(CAMERA_SERVICE);
                try {
                    getCameraIdListM = cameraManagerM.getCameraIdList();
                    cameraCharacteristicsM = cameraManagerM.getCameraCharacteristics(getCameraIdListM[0]);
                    streamConfigurationMapM = cameraCharacteristicsM.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

                Size[] x = streamConfigurationMapM.getOutputSizes(SurfaceTexture.class);

                surfaceTextureM.setDefaultBufferSize(x[0].getWidth(), x[0].getHeight());
                surfaceM = new Surface(surfaceTextureM);

                try {
                    cameraManagerM.openCamera(getCameraIdListM[0], new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull final CameraDevice camera) {
                            cameraDeviceM = camera;
                            try {
                                camera.createCaptureSession(Arrays.asList(surfaceM), new CameraCaptureSession.StateCallback() {
                                    @Override
                                    public void onConfigured(@NonNull CameraCaptureSession session) {
                                        cameraCaptureSessionM = session;
                                        try {
                                            builderM = cameraDeviceM.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                            builderM.addTarget(surfaceM);
                                            req(builderM);
                                        } catch (CameraAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                                    }
                                }, backhandlerM);
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
                    }, backhandlerM);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

    }

    private void Start_backHandler() {
        handlerThreadM = new HandlerThread("TextureView_demo");
        handlerThreadM.start();
        backhandlerM = new Handler(handlerThreadM.getLooper());
    }

    private void stop_backHandler() {
        if (handlerThreadM.isAlive()) {
            handlerThreadM.quitSafely();
            try {
                handlerThreadM.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handlerThreadM = null;
            backhandlerM = null;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        open_camera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        stop_backHandler();
        semaphoreM.release();
        cameraDeviceM.close();
        cameraDeviceM = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
