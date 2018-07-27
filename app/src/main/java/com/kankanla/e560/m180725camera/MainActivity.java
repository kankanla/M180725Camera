package com.kankanla.e560.m180725camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.button1:
                ((Button) v).setText("setTorchMode");
                startActivity(new Intent(this, setTorchMode.class));
                break;
            case R.id.button2:
                ((Button) v).setText("CameraCharacteristics_demo");
                startActivity(new Intent(this, CameraCharacteristics_demo.class));
                break;
            case R.id.button3:
                ((Button) v).setText("activity_surface_view_demo");
                startActivity(new Intent(this, SurfaceView_demo.class));
                break;
            case R.id.button4:
                ((Button) v).setText("activity_surface_view_demo2");
                startActivity(new Intent(this, SurfaceView_demo2.class));
                break;
            case R.id.button5:
                ((Button) v).setText("22222");
                break;
            case R.id.button6:
                ((Button) v).setText("22222");
                break;
            case R.id.button7:
                ((Button) v).setText("22222");
                break;
            case R.id.button8:
                ((Button) v).setText("22222");
                break;
            case R.id.button9:
                ((Button) v).setText("22222");
                break;

            default:

        }

    }
}
