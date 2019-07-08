package com.bytedance.minidouyin.takeVideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.minidouyin.R;
import com.bytedance.minidouyin.bean.Feed;
import com.bytedance.minidouyin.db.FeedContract;
import com.bytedance.minidouyin.db.FeedDBHelper;
import com.bytedance.minidouyin.showVideo.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

public class MyHomeActivity extends AppCompatActivity {
    public static final String LOGINNAME = "LOGINNAME";
    public static final String LOGINID = "LOGINID";

    TextView myname;
    TextView myid;
    String name;
    String id;
    RecyclerView mRv;
    private List<Feed> feedList = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);
        final Intent intent = getIntent();
        name = intent.getStringExtra(LOGINNAME);
        id = intent.getStringExtra(LOGINID);
        myname=findViewById(R.id.myname);
        myid=findViewById(R.id.myid);
        myid.setText(id);
        myname.setText(name);

        mRv = findViewById(R.id.myrecycleview);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @NonNull @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent showVideoIntent = new Intent(MyHomeActivity.this, VideoPlayActivity.class);
                        showVideoIntent.setData(Uri.parse(feedList.get(i).getVideoUrl()));
                        startActivity(showVideoIntent);
                    }
                });
                return new MyViewHolder(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ImageView iv = (ImageView) viewHolder.itemView;
                iv.setPadding(10,30,10,10);
                String url = feedList.get(i).getImgUrl();
                Glide.with(iv.getContext()).load(url).into(iv);
            }

            @Override public int getItemCount() {
                return feedList.size();
            }
        };
        mRv.setAdapter(adapter);
        getFeeds();
        adapter.notifyDataSetChanged();
    }

    public void getFeeds(){
        FeedDBHelper helper = new FeedDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+FeedContract.tableName,null);
        while(cursor.moveToNext()){
            Feed f = FeedDBHelper.toFeed(cursor);
            if(f.getName().compareTo(name)==0&&f.getStudentID().compareTo(id)==0)
                feedList.add(f);
        }
        helper.close();
    }
}
