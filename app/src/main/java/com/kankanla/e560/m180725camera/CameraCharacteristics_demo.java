package com.kankanla.e560.m180725camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CameraCharacteristics_demo extends AppCompatActivity {
    private String TAG = "//-----CameraCharacteristics_demo-----//";
    private CameraManager cameraManager = null;
    private CameraCharacteristics cameraCharacteristics;
    private String[] cameraIdList = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_characteristics_demo);
        setTitle(getLocalClassName());
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length == 0) {
                return;
            }
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[0]);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        try {
            T1_COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES();
            T2_CONTROL_AE_AVAILABLE_ANTIBANDING_MODES();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    该相机设备支持的像差校正模式列表。
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void T1_COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES() throws CameraAccessException {
        int[] modes = cameraCharacteristics.get(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES);
        Log.d(TAG, "T1_COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES");
        for (int m : modes) {
            switch (m) {
                case CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_OFF:
                    System.out.println("COLOR_CORRECTION_ABERRATION_MODE_OFF ");
                    break;
                case CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_FAST:
                    System.out.println("COLOR_CORRECTION_ABERRATION_MODE_FAST");
                    break;
                case CameraMetadata.COLOR_CORRECTION_MODE_HIGH_QUALITY:
                    System.out.println("COLOR_CORRECTION_MODE_HIGH_QUALITY");
                    break;
                default:
                    System.out.println("000");
            }
        }
    }

    /*
    此相机设备支持自动曝光抗菌模式列表。
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void T2_CONTROL_AE_AVAILABLE_ANTIBANDING_MODES() {
        int[] mode = cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
        Log.d(TAG, "T2_CONTROL_AE_AVAILABLE_ANTIBANDING_MODES");
        for (int m : mode) {
            System.out.println(m);
            if (m == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_AUTO) ;
        }
    }
}
