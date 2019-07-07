package com.bytedance.minidouyin.showVideo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.minidouyin.R;
import com.bytedance.minidouyin.bean.Feed;
import com.bytedance.minidouyin.newtork.FetchFeedThreads;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "Fragment";
    // TODO: Rename and change types of parameters
    private String mParam1;

//    private OnFragmentInteractionListener mListener;
    private ViewPager viewPager;
    protected List<Feed> feedList=new ArrayList<>();
    private FragmentPagerAdapter adapter;


    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what== FetchFeedThreads.FETCH_BACK){
                feedList = FetchFeedThreads.getInstance().getList();
                adapter.notifyDataSetChanged();
                Log.d(TAG, "handleMessage: "+feedList.size());
            }
        }
    }
    private Handler handler = new MyHandler();

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment videoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        FetchFeedThreads.getInstance().fetch_request(handler);
        viewPager = view.findViewById(R.id.videoViewPager);
        adapter = new FragmentPagerAdapter(getFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                VideoPlayFragment fragment = VideoPlayFragment.newInstance();
                fragment.setResource(Uri.parse(feedList.get(position).getVideoUrl()));
                return fragment;
            }

            @Override
            public int getCount() {
                return feedList.size();
            }
        };
        viewPager.setAdapter(adapter);
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
