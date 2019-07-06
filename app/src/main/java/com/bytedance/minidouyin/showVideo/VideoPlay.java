package com.bytedance.minidouyin.showVideo;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.minidouyin.R;


public class VideoPlay extends AppCompatActivity {
    public static final String URI="URI";
    Uri resource;
    public static final String PROGESS="PROGESS";
    private int progess=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        if(savedInstanceState!=null){
            progess = savedInstanceState.getInt(PROGESS,0);
            resource = savedInstanceState.getParcelable(URI);
        }else{
            resource = getIntent().getData();
        }

        VideoPlayFragment fragment = new VideoPlayFragment();
        ((VideoPlayFragment) fragment).setResource(resource);
        ((VideoPlayFragment) fragment).setProgess(progess);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.placeholder,fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PROGESS,progess);
        outState.putParcelable(URI,resource);
    }
}
