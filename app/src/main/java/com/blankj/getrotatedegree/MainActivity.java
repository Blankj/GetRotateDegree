package com.blankj.getrotatedegree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RotateImageView rivRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rivRotate = (RotateImageView) findViewById(R.id.riv_rotate);
        rivRotate.setOnRotationChangeListener(new RotateImageView.OnRotationChangeListener() {
            @Override
            public void getRotation(int degree) {
                ((TextView) findViewById(R.id.tv_degree)).setText(String.valueOf(degree));
            }
        });

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rivRotate.reset();
                ((TextView) findViewById(R.id.tv_degree)).setText("0");
            }
        });
    }
}
