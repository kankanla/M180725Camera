package com.kankanla.e560.m180725camera;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class RectFandMatrix_demo extends AppCompatActivity {

    private final String TAG = "-aaa-RectFandMatrix-";
    private final String TAG2 = "--bbb--";
    private TextureView textureViewM;
    private Surface surfaceM;
    private SurfaceView surfaceViewM;
    private SurfaceHolder surfaceHolderM;
    private Surface surfaceMM;
    private Button button2222;
    private Size vs;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_fand_matrix_demo);
        setTitle(TAG);
        textureViewM = findViewById(R.id.textureView222);
        surfaceViewM = findViewById(R.id.surfaceView222);

        Log.d(TAG2, TAG);
        button2222 = findViewById(R.id.button222);
        button2222.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "setOnClickListener");
                    ck();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "surfaceTextureListener");
            surfaceM = new Surface(surface);

            Matrix matrix = new Matrix();
            RectF rectf = new RectF();
            rectf.set(0, 0, textureViewM.getWidth(), textureViewM.getHeight());
            float x = rectf.centerX();
            float y = rectf.centerY();
            matrix.setRotate(30, x, y);
            textureViewM.setTransform(matrix);

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
    };


    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @SuppressLint("LongLogTag")
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "SurfaceHolder.Callback");
            surfaceMM = holder.getSurface();
            surfaceViewM.setRotation(90);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        textureViewM.setSurfaceTextureListener(surfaceTextureListener);
        surfaceHolderM = surfaceViewM.getHolder();
        surfaceHolderM.addCallback(callback);
    }

    protected void T1() {
        Matrix matrix = new Matrix();
        RectF rectF = new RectF();
    }


    private CameraDevice.StateCallback CDcallback;
    private CameraCaptureSession cameraCaptureSession;
    private CameraDevice cameraDevice;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void ck() throws CameraAccessException {
        if (!surfaceM.isValid() && !surfaceMM.isValid()) {
            return;
        }

        backHandler();

        CDcallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull final CameraDevice camera) {
                cameraDevice = camera;
                List<Surface> xx = new ArrayList<>();
                xx.add(surfaceM);
                xx.add(surfaceMM);
                try {
                    camera.createCaptureSession(xx, new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {

                            try {
                                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

                                builder.addTarget(surfaceM);
                                builder.addTarget(surfaceMM);

                                session.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                                        Log.d(TAG, "onCaptureStarted");
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                                        super.onCaptureProgressed(session, request, partialResult);
                                        Log.d(TAG, "onCaptureProgressed");
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                                        super.onCaptureCompleted(session, request, result);
                                        Log.d(TAG, "onCaptureCompleted");
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                                        super.onCaptureFailed(session, request, failure);
                                        Log.d(TAG, "onCaptureFailed");
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                                        super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
                                        Log.d(TAG, "onCaptureSequenceCompleted");
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                                        super.onCaptureSequenceAborted(session, sequenceId);
                                        Log.d(TAG, "onCaptureSequenceAborted");
                                    }

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
                                        super.onCaptureBufferLost(session, request, target, frameNumber);
                                        Log.d(TAG, "onCaptureBufferLost");
                                    }
                                }, handler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                        }
                    }, handler);
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
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        cameraManager.openCamera("0", CDcallback, handler);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private HandlerThread handlerThread;
    private Handler handler;

    protected void backHandler() {
        if (handlerThread != null) {
            return;
        }
        handlerThread = new HandlerThread("back");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }
}
