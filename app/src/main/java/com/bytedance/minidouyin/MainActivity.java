package com.bytedance.minidouyin;

import android.os.Bundle;

import com.bytedance.minidouyin.message.MessageFragment;
import com.bytedance.minidouyin.showVideo.VideoFragment;
import com.bytedance.minidouyin.showVideo.VideoPlayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int state=-1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(state!=0){
                        VideoFragment videoPlayFragment = VideoFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(
                                R.id.palceholder,videoPlayFragment).commit();
                        state=0;
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if(state!=1){
                        MessageFragment videoPlayFragment = MessageFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(
                                R.id.palceholder,videoPlayFragment).commit();
                        state=1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //navView.setSelectedItemId(R.id.navigation_dashboard);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MessageFragment videoPlayFragment = MessageFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.palceholder,videoPlayFragment).commit();
        state=1;
    }

}
