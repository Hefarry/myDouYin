package com.bytedance.minidouyin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.bytedance.minidouyin.newtork.FetchFeedThreads;

public class GateWay extends AppCompatActivity {
    protected LottieAnimationView animationView;
    public static final String[] permissions=new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static final int REQUEST_EXTERNAL_CAMERA=100;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what== FetchFeedThreads.FETCH_BACK){
                boolean flg = true;
                for(int i=0;i<permissions.length;i++){
                    if(checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED){
                        flg=false;
                        break;
                    }
                }
                if (!flg) {
                    requestPermissions(permissions,REQUEST_EXTERNAL_CAMERA);
                }else {
                    startActivity(new Intent(GateWay.this, MainActivity.class));
                    finish();
                }
            }
        }
    }
    private Handler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_way);
        animationView = findViewById(R.id.animation_view);
        FetchFeedThreads.getInstance().fetch_request(handler,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_EXTERNAL_CAMERA){
            for(int i=0;i<permissions.length;i++){
                if(checkSelfPermission(permissions[i])!=PackageManager.PERMISSION_GRANTED){
                    finish();
                }
            }
            startActivity(new Intent(GateWay.this, MainActivity.class));
            finish();
        }
    }
}
