package com.bytedance.minidouyin.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.minidouyin.R;

import java.util.List;


/**
 * 大作业:实现一个抖音消息页面,所需资源已放在res下面
 */
public class MessageActivityDemo extends AppCompatActivity {
    private static final String TAG = "EXE3";
    public ListView messageList;
    public ViewGroup fances;
    public ViewGroup zan;
    public ViewGroup me;
    public ViewGroup comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        messageList = findViewById(R.id.messageList);
        List<Message> data =null;
        try {
            //data = PullParser.getMessage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageList.setAdapter(new ListViewAdapter(this,data));
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClick1(i);
            }
        });
        fances = findViewById(R.id.fances);
        zan = findViewById(R.id.zan);
        me = findViewById(R.id.callMy);
        comment = findViewById(R.id.comment);
        setOnClickForVG(fances);
        setOnClickForVG(zan);
        setOnClickForVG(me);
        setOnClickForVG(comment);
    }

    public void setOnClickForVG(ViewGroup viewGroup){
        TextView v2 = (TextView)viewGroup.getChildAt(1);
        final String msg = v2.getText().toString();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickWithString(msg);
            }
        };
        viewGroup.setOnClickListener(listener);
//        View v1 = viewGroup.getChildAt(0);
//
//        v1.setOnClickListener(listener);
//        v2.setOnClickListener(listener);
//        viewGroup.setOnClickListener(listener);
    }

    public void onClick1(int cnt){
//        Intent intent = new Intent(this,Main2Activity.class);
//        intent.putExtra("IND",cnt);
//        startActivity(intent);
    }

    public void onClickWithString(String s){
//        Intent intent = new Intent(this,Main3Activity.class);
//        intent.putExtra("MSG",s);
//        startActivity(intent);
    }

    public void onClick2(View view){
//        ViewGroup viewGroup = (ViewGroup)view.getParent();
//        TextView textView = (TextView)viewGroup.getChildAt(1);
//        Intent intent = new Intent(this,Main3Activity.class);
//        intent.putExtra("MSG",textView.getText());
//        startActivity(intent);
    }

    public static class ListViewAdapter extends BaseAdapter{
        private static final String TAG = ListViewAdapter.class.getName();
        private List<Message> args;
        private Context context;

        public ListViewAdapter(Context c,List<Message> args){
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

        public void renewView(ViewGroup view,Message msg){
            //ViewGroup iconLayout = (ViewGroup)view.getChildAt(0);
            //CircleImageView img = (CircleImageView)iconLayout.getChildAt(0);
            TextView title = (TextView)view.getChildAt(1);
            title.setText(msg.getTitle());
            TextView des = (TextView)view.getChildAt(2);
            des.setText(msg.getDescription());
            TextView time = (TextView)view.getChildAt(3);
            time.setText(msg.getTime());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = null;
            Message msg = args.get(i);
            if(view==null) {
                ViewGroup inf = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.im_list_item,null);
                renewView(inf,msg);
                v=inf;
            }else{
                renewView((ViewGroup)view,msg);
                v = view;
            }
            Log.d(TAG, "getView() called"+ v.toString());
            return v;
        }
    }

}
