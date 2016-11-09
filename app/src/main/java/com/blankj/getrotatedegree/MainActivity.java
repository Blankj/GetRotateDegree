package com.blankj.getrotatedegree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((RotateImageView)findViewById(R.id.riv_rotate)).setOnRotationChangeListener(new RotateImageView.onRotationChangeListener() {
            @Override
            public void getRotation(int degree) {
                ((TextView) findViewById(R.id.tv_degree)).setText(String.valueOf(degree));
            }
        });
    }
}
