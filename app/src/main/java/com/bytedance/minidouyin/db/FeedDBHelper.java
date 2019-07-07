package com.bytedance.minidouyin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import static com.bytedance.minidouyin.db.FeedContract.*;

import com.bytedance.minidouyin.bean.Feed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeedDBHelper extends SQLiteOpenHelper {
    // TODO 定义数据库名、版本；创建数据库
    public static final String DATABASE_NAME = "FeedDB.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "SQL";

    public FeedDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+FeedContract.tableName +" ( "
                + FeedContract.ID + " INTEGER PRIMARY KEY, "
                + FeedContract.student_id + " TEXT, "
                + FeedContract.user_name + " TEXT, "
                + FeedContract.createdAt + " TEXT, "
                + FeedContract.updatedAt + " TEXT, "
                + FeedContract.image_url + " TEXT, "
                + FeedContract.video_url + " TEXT)");
    }

    public void insertOneFeed(Feed item,SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(image_url,item.getImgUrl());
        cv.put(video_url,item.getVideoUrl());
        cv.put(student_id,item.getStudentID());
        cv.put(user_name,item.getName());
        cv.put(createdAt,item.getCreateAt());
        cv.put(updatedAt,item.getUpdatedAt());
        db.replace(tableName,null,cv);
    }

    public static Feed toFeed(Cursor cursor){
        Feed feed = new Feed();
        feed.setCreateAt(cursor.getString(cursor.getColumnIndex(createdAt)));
        feed.setImgUrl(cursor.getString(cursor.getColumnIndex(image_url)));
        feed.setUpdatedAt(cursor.getString(cursor.getColumnIndex(updatedAt)));
        feed.setName(cursor.getString(cursor.getColumnIndex(user_name)));
        feed.setStudentID(cursor.getString(cursor.getColumnIndex(student_id)));
        feed.setVideoUrl(cursor.getString(cursor.getColumnIndex(video_url)));
        return feed;
    }

    public void updateDB(List<Feed> list,Context context){
        FeedDBHelper feedDBHelper = new FeedDBHelper(context);
        SQLiteDatabase db = feedDBHelper.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(new Comparator<Feed>() {
                @Override
                public int compare(Feed o1, Feed o2) {
                    return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
                }
            });
        }
        int cnt =0;
        for(Feed item : list ){
            feedDBHelper.insertOneFeed(item,db);
            cnt++;
            if(cnt>50){
                break;
            }
        }
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i = oldVersion;i<newVersion;i++){
            switch (i){
                default:
            }
        }
    }
}
