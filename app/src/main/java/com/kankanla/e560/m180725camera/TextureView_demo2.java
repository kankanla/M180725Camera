package com.kankanla.e560.m180725camera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;


public class TextureView_demo2 extends AppCompatActivity {
    private final String TAG = "-aaa-Texture_demo2";
    private HandlerThread m_backGurond_Thread;
    private Handler m_backGurond_handler;
    private CameraDevice m_cameraDevice;
    private CameraCaptureSession m_cameraCaptureSession;
    private CameraManager m_cameraManager;
    private TextureView m_textureView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        m_cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        setContentView(R.layout.activity_texture_view_demo2);
    }


    protected void Start_BackGurand() {
        m_backGurond_Thread = new HandlerThread("camera");
        m_backGurond_Thread.start();
        m_backGurond_handler = new Handler(m_backGurond_handler.getLooper());
    }

    protected void Stop_BackGurand() {
        try {
            m_backGurond_Thread.join();
            m_backGurond_Thread.quit();
            m_backGurond_handler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
