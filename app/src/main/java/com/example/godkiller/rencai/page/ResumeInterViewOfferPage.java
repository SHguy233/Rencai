package com.example.godkiller.rencai.page;

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
import com.example.godkiller.rencai.db.ChannelDb2;
import com.example.godkiller.rencai.fragment.HRInterviewChannelFragment;
import com.example.godkiller.rencai.fragment.HROfferChannelFragment;
import com.example.godkiller.rencai.fragment.HRResumeChannelFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/5/7.
 */
public class ResumeInterViewOfferPage  extends FragmentActivity implements OnPageChangeListener{
    private ViewPager viewPager;
    private RadioGroup rgChannel=null;
    private HorizontalScrollView hvChannel;
    private InterviewPageFragmentAdapter adapter=null;
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intertview_resume_page);
        backBtn = (Button) findViewById(R.id.back_button_mrr);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }
    private void initView(){
        rgChannel=(RadioGroup)super.findViewById(R.id.rgChannel2);
        viewPager=(ViewPager)super.findViewById(R.id.vpNewsList2);
        hvChannel=(HorizontalScrollView)super.findViewById(R.id.hvChannel2);
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
        List<Channel> channelList= ChannelDb2.getSelectedChannel();
        for(int i=0;i<channelList.size();i++){
            RadioButton rb=(RadioButton) LayoutInflater.from(this).
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i).getName());
            RadioGroup.LayoutParams params=new
                    RadioGroup.LayoutParams(400,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rgChannel.addView(rb,params);
        }

    }
    private void initViewPager(){
        HRInterviewChannelFragment interviewFragment = new HRInterviewChannelFragment();
        HRResumeChannelFragment resumeFragment = new HRResumeChannelFragment();
        HROfferChannelFragment offerFragment = new HROfferChannelFragment();

        fragmentList.add(resumeFragment);
        fragmentList.add(interviewFragment);
        fragmentList.add(offerFragment);
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
}

