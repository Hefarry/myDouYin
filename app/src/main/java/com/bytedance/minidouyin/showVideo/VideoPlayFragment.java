package com.bytedance.minidouyin.showVideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.bytedance.minidouyin.R;


public class VideoPlayFragment extends Fragment {
    public static final String TAG = "VideoPlayFragment";
    private Uri resource;
    private int progess=0;

    private SurfaceView surfaceView;

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private CheckBox checkBox;
    private static final int REFRESH_PROCGRESS=1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==REFRESH_PROCGRESS){
//                Log.d(TAG, "handleMessage: ");
                double cur = mediaPlayer.getCurrentPosition();
                double len = mediaPlayer.getDuration();
                double process = cur/len*100.0;
                progess=(int)process;
                seekBar.setProgress((int)process);
                handler.removeMessages(REFRESH_PROCGRESS);
                handler.sendMessageDelayed(Message.obtain(handler,REFRESH_PROCGRESS),
                        1000);
            }
        }
    };
    private DisplayMetrics displayMetrics;
    private int mScreenWidth;
    private int mScreenHeight;

    public VideoPlayFragment() {
    }

    public void setProgess(int p){
        progess = p;
    }

    public int getProgess(){
        return progess;
    }


    public static VideoPlayFragment newInstance() {
        VideoPlayFragment fragment = new VideoPlayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayMetrics = new DisplayMetrics();
        this.getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

    }

    private void fit(){
        float height = mediaPlayer.getVideoHeight();
        float width = mediaPlayer.getVideoWidth();

        if(mScreenWidth<mScreenHeight){
            float scaleY = height/width*mScreenWidth/mScreenHeight;
            surfaceView.setScaleY(scaleY);
        }else{
            float scaleX = width/height*mScreenHeight/mScreenWidth;
            surfaceView.setScaleX(scaleX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_video_play, container, false);
        surfaceView = view.findViewById(R.id.surface);
        mediaPlayer = new MediaPlayer();
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setEnabled(false);
        checkBox = view.findViewById(R.id.loop_checkbox);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: ");
                if(isChecked){
                    seekBar.setEnabled(true);
                    mediaPlayer.pause();
                }else{
                    seekBar.setEnabled(false);
                    mediaPlayer.start();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser){
                    return;
                }
                Log.d(TAG, "onProgressChanged: FromUser");
                double len = mediaPlayer.getDuration();
                double cur = (progress*len/100.0);
                mediaPlayer.seekTo((int)cur);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        try {
            if(resource!=null) {
                mediaPlayer.setDataSource(getActivity(), resource);
            }
            surfaceView.getHolder().addCallback(new PlayerCallBack());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared: start progess="+progess);
                    fit();
                    mediaPlayer.start();
                    checkBox.setChecked(false);
                    double len = mediaPlayer.getDuration();
                    double cur = progess/100.0*len;
                    mediaPlayer.seekTo((int)cur);
                    seekBar.setProgress((int)progess);
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.d(TAG, "onBufferingUpdate: ");
                }
            });
            mediaPlayer.prepare();
            Log.d(TAG, "onCreateView: end prepare");
        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }
        handler.sendMessage(Message.obtain(handler,REFRESH_PROCGRESS));
        return view;
    }



    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        handler.removeMessages(REFRESH_PROCGRESS);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();

    }

    public void setResource(Uri resource) {
        if(resource!=null) {
            this.resource = resource;
        }
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }
}
