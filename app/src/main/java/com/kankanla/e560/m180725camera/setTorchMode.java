package com.kankanla.e560.m180725camera;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
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
import android.widget.Toast;

public class setTorchMode extends AppCompatActivity {
    private String TAG = "//-----setTorchMode-----//";
    private Boolean TorchONOFF = false;
    private CameraManager cameraManager;
    private CameraManager.TorchCallback torchCallback;
    private Button button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_torch_mode);
        getLocalClassName();
        setTitle(getLocalClassName());

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
        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[0]);

        //如果没有相机可用推出
        if (cameraIdList.length == 0) {
            return;
        }

        //如果没有闪光灯可用推出
        if (!cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
            Toast.makeText(this, "!FLASH_INFO_AVAILABLE", Toast.LENGTH_SHORT).show();
            return;
        }

        //相机闪光灯手电筒模式变为不可用，禁用或启用的回调。
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
                //得到的闪光灯的状态
                TorchONOFF = enabled;
                button.setText(String.valueOf(enabled));
            }
        };

        //回调方法的工作线程
        HandlerThread handlerThread = new HandlerThread("TorchHandler");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        //注册闪光灯回调，在非主线程中执行
        cameraManager.registerTorchCallback(torchCallback, handler);

        //找到按键，并设置事件
        button = findViewById(R.id.button11);
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


    /*
    当用户从“暂停”状态继续您的 Activity 时，系统会调用 onResume() 方法。
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        /*
        解除回调注册
         */
        cameraManager.unregisterTorchCallback(torchCallback);
    }

    /*
    当系统销毁您的 Activity 时，它会调用您的 Activity 的 onDestroy() 方法。
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        /*
        解除回调注册
         */
        cameraManager.unregisterTorchCallback(torchCallback);
    }
}
