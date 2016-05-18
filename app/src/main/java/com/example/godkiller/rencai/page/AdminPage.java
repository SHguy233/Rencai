package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.Channel;
import com.example.godkiller.rencai.base.InterviewPageFragmentAdapter;
import com.example.godkiller.rencai.db.ChannelDb3;
import com.example.godkiller.rencai.fragment.AdminAfterChannelFragment;
import com.example.godkiller.rencai.fragment.AdminBeforeChannelFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/5/7.
 */
public class AdminPage extends FragmentActivity implements OnPageChangeListener{
    private ViewPager viewPager;
    private RadioGroup rgChannel=null;
    private HorizontalScrollView hvChannel;
    private InterviewPageFragmentAdapter adapter=null;
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_check_page);
        initView();
    }
    private void initView(){
        rgChannel=(RadioGroup)super.findViewById(R.id.rgChannel3);
        viewPager=(ViewPager)super.findViewById(R.id.vpNewsList3);
        hvChannel=(HorizontalScrollView)super.findViewById(R.id.hvChannel3);
        rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        viewPager.setCurrentItem(checkedId);
                    }
                });
        viewPager.setOnPageChangeListener(this);
        initTab();//动态产生RadioButton
        initViewPager();
        rgChannel.check(0);
    }
    private void initTab(){
        List<Channel> channelList= ChannelDb3.getSelectedChannel();
        for(int i=0;i<channelList.size();i++){
            RadioButton rb=(RadioButton) LayoutInflater.from(this).
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i).getName());
            RadioGroup.LayoutParams params=new
                    RadioGroup.LayoutParams(600,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rgChannel.addView(rb,params);
        }

    }
    private void initViewPager(){
        AdminAfterChannelFragment fragment2 = new AdminAfterChannelFragment();
        AdminBeforeChannelFragment fragment1 = new AdminBeforeChannelFragment();

        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        adapter=new InterviewPageFragmentAdapter(super.getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        //viewPager.setOffscreenPageLimit(0);
    }

    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     * @param idx
     */
    private void setTab(int idx){
        RadioButton rb=(RadioButton)rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left=rb.getLeft();
        int width=rb.getMeasuredWidth();
        DisplayMetrics metrics=new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int len=left+width/2-screenWidth/2;
        hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
    }
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        setTab(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 900) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}

