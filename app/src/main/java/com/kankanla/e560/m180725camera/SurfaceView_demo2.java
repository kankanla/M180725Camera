package com.kankanla.e560.m180725camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.PriorityQueue;

public class SurfaceView_demo2 extends AppCompatActivity {
    private CameraCharacteristics cameraCharacteristics;
    private CameraManager McameraManager;
    private CameraDevice McameraDevice;
    private StreamConfigurationMap MstreamConfigurationMap;
    private SurfaceView MsurfaceView;
    private SurfaceHolder MsurfaceHolder;
    private Surface Msurface;
    private CameraCaptureSession McameraCaptureSession;
    private CaptureRequest.Builder Mbuilder;
    private String[] req_permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_demo2);
    }

    protected boolean chk_permission() {
        for (String temp : req_permissions) {
            if (ActivityCompat.checkSelfPermission(this, temp) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }




}
