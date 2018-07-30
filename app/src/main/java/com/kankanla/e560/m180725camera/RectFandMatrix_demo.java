package com.kankanla.e560.m180725camera;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;


public class RectFandMatrix_demo extends AppCompatActivity {

    private final String TAG = "-aaa-RectFandMatrix_demo-";
    private TextureView textureViewM;
    private Surface surfaceM;
    private SurfaceView surfaceViewM;
    private SurfaceHolder surfaceHolderM;
    private Surface surfaceMM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_fand_matrix_demo);
        setTitle(TAG);
        textureViewM = findViewById(R.id.textureView222);
        surfaceViewM = findViewById(R.id.surfaceView222);
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "surfaceTextureListener");

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
        textureViewM.setSurfaceTextureListener(surfaceTextureListener);
        surfaceHolderM = surfaceViewM.getHolder();
        surfaceHolderM.addCallback(callback);
    }

    protected void T1() {
        Matrix matrix = new Matrix();
    }

}
