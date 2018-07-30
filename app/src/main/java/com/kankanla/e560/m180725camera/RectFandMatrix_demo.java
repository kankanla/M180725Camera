package com.kankanla.e560.m180725camera;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class RectFandMatrix_demo extends AppCompatActivity {

    private final String TAG = "-aaa-RectFandMatrix_demo-";
    private SurfaceView surfaceViewM;
    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolderM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_fand_matrix_demo);
        setTitle(TAG);
        mSurfaceView = findViewById(R.id.surfaceView222);
    }


    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        @SuppressLint("LongLogTag")
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "Callback");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };


    private SurfaceHolder.Callback2 callback2 = new SurfaceHolder.Callback2() {
        @SuppressLint("LongLogTag")
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {
            Log.d(TAG, "callback2");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        surfaceHolderM = mSurfaceView.getHolder();
        surfaceHolderM.addCallback(callback);
        surfaceHolderM.addCallback(callback2);



    }

    protected void T1() {
        Matrix matrix = new Matrix();
    }

}
