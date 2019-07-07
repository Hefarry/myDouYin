package com.bytedance.minidouyin.newtork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.bytedance.minidouyin.bean.Feed;
import com.bytedance.minidouyin.bean.FeedResponse;
import com.bytedance.minidouyin.db.FeedDBHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;
import retrofit2.Retrofit;

public class FetchFeedThreads {
    private static FetchFeedThreads instance = new FetchFeedThreads();
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    public static int FETCH_BACK=100;
    private List<Feed> list = new ArrayList<>();
    public static final String baseUrl="http://test.androidcamp.bytedance.com/";
    public static final String TAG = "FETCHFEED";

    private FetchFeedThreads(){
        super();
    }

    public static FetchFeedThreads getInstance(){
        return instance;
    }

    public void fetch_request(final Handler handler){
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = RetrofitManager.get(baseUrl);
                try {
                    final Response<FeedResponse> response = retrofit.create(IMiniDouyinService.class)
                            .getFeedResponse().execute();
                    Log.d(TAG, "run: end Fetch Feed");
                    if (response.body().isSuccess()) {
                        list = response.body().getFeeds();
                        handler.sendMessage(Message.obtain(handler,FETCH_BACK));
                        Log.d(TAG, "run: Fetch Feed Succeed");
                    } else {
                        Log.d(TAG, "run: Fetch Feed Failed");
                    }
                }catch (Exception e){
                    Log.d(TAG, "run: "+e.getMessage());
                }
            }
        });
    }

    public List<Feed> getList() {
        return list;
    }
}
