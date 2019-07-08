package com.bytedance.minidouyin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class GateWay extends AppCompatActivity {
    protected LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_way);
        animationView = findViewById(R.id.animation_view);
    }
}
