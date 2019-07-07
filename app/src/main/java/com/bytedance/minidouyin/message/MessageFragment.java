package com.bytedance.minidouyin.message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bytedance.minidouyin.R;
import com.bytedance.minidouyin.bean.Feed;
import com.bytedance.minidouyin.newtork.FetchFeedThreads;
import com.bytedance.minidouyin.showVideo.VideoFragment;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {
    private static final String TAG = "Fragment";
    public ListView messageList;
    private ListViewAdapter adapter;
    private List<Feed>feedList = new ArrayList<>();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what== FetchFeedThreads.FETCH_BACK){
                feedList = FetchFeedThreads.getInstance().getList();
                adapter.setArgs(feedList);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "handleMessage: "+feedList.size());
            }
        }
    }
    private Handler handler = new MessageFragment.MyHandler();

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_message, container, false);
        adapter=new ListViewAdapter(getActivity(),feedList);
        messageList = v.findViewById(R.id.messageList);
        messageList.setAdapter(adapter);
        FetchFeedThreads.getInstance().fetch_request(handler);
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        Log.d(TAG, "onCreateView: ");
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class ListViewAdapter extends BaseAdapter {
        private List<Feed> args;
        private Context context;

        public void setArgs(List<Feed> list){
            args=list;
        }

        @Override
        public void notifyDataSetChanged() {
            Log.d(TAG, "notifyDataSetChanged: "+args.size());
            super.notifyDataSetChanged();
        }

        public ListViewAdapter(Context c, List<Feed> args){
            context=c;
            this.args = args;
        }

        @Override
        public int getCount() {
            return args.size();
        }

        @Override
        public Object getItem(int i) {
            return args.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void renewView(ViewGroup view,Feed msg){
            //ViewGroup iconLayout = (ViewGroup)view.getChildAt(0);
            //CircleImageView img = (CircleImageView)iconLayout.getChildAt(0);
            TextView title = (TextView)view.getChildAt(1);
            title.setText(msg.getName());
            TextView des = (TextView)view.getChildAt(2);
            des.setText(msg.getStudentID());
            TextView time = (TextView)view.getChildAt(3);
            time.setText(msg.getUpdatedAt());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = null;
            Feed msg = args.get(i);
            if(view==null) {
                ViewGroup inf = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.im_list_item,null);
                renewView(inf,msg);
                v=inf;
            }else{
                renewView((ViewGroup)view,msg);
                v = view;
            }
            return v;
        }
    }

}
