package com.kankanla.e560.m180725camera;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class setTorchMode extends AppCompatActivity {
    private String TAG = "setTorchMode";
    private Boolean TorchONOFF = false;
    private CameraManager cameraManager;
    private CameraManager.TorchCallback torchCallback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_torch_mode);
        setTitle(this.getClass().getCanonicalName());
        try {
            t1();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void t1() throws CameraAccessException {
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        final String[] cameraIdList = cameraManager.getCameraIdList();

        torchCallback = new CameraManager.TorchCallback() {
            @Override
            public void onTorchModeUnavailable(@NonNull String cameraId) {
                Log.e(TAG, "onTorchModeUnavailable");
                super.onTorchModeUnavailable(cameraId);
            }

            @Override
            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                Log.e(TAG, "onTorchModeChanged");
                super.onTorchModeChanged(cameraId, enabled);
                TorchONOFF = enabled;
            }
        };

        HandlerThread handlerThread = new HandlerThread("TorchHandler");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        cameraManager.registerTorchCallback(torchCallback, handler);

        Button button = findViewById(R.id.button11);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (TorchONOFF) {
                        cameraManager.setTorchMode(cameraIdList[0], false);
                    } else {
                        cameraManager.setTorchMode(cameraIdList[0], true);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        cameraManager.unregisterTorchCallback(torchCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        cameraManager.unregisterTorchCallback(torchCallback);
    }
}
