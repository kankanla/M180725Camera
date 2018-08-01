package com.kankanla.e560.m180725camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class View_demo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demo);

    }

    class myView extends View {
        public myView(Context context) {
            super(context);
        }

        @Override
        public Bitmap getDrawingCache() {
            return super.getDrawingCache();
        }
    }
}
