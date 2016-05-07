package com.example.godkiller.rencai.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.Channel;
import com.example.godkiller.rencai.base.InterviewPageFragmentAdapter;
import com.example.godkiller.rencai.db.ChannelDb;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class InterviewFragment extends Fragment implements OnPageChangeListener {
    private String username;
    private View view = null;
    private RadioGroup rgChannel = null;
    private ViewPager viewpager;
    private HorizontalScrollView hvChannel = null;
    private List<Fragment> msgsChannelList = new ArrayList<Fragment>();
    private InterviewPageFragmentAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.message_fragment, null);
            rgChannel = (RadioGroup) view.findViewById(R.id.rgChannel);
            viewpager = (ViewPager) view.findViewById(R.id.vpList);
            hvChannel = (HorizontalScrollView) view.findViewById(R.id.hvChannel);
            rgChannel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    viewpager.setCurrentItem(checkedId);

                }
            });
            initTab(inflater);
            initViewPager();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

       return view;
    }

    private void initViewPager() {
        InterviewChannelFragment fragment = new InterviewChannelFragment();
        OfferChannelFragment fragment2 = new OfferChannelFragment();
        msgsChannelList.add(fragment);
        msgsChannelList.add(fragment2);
        adapter = new InterviewPageFragmentAdapter(super.getActivity().getSupportFragmentManager(), msgsChannelList);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(this);
    }

    private void initTab(LayoutInflater inflater) {
        List<Channel> channelList = ChannelDb.getSelectedChannel();
        for (int i = 0; i < channelList.size(); i++) {
            RadioButton rb = (RadioButton) inflater.
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i).getName());
            RadioGroup.LayoutParams params = new
                    RadioGroup.LayoutParams(600,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rgChannel.addView(rb, params);
        }
        rgChannel.check(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setTab(int idx) {
        RadioButton rb = (RadioButton) rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        super.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hvChannel.smoothScrollTo(len, 0);
    }

}
