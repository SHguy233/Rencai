package com.example.godkiller.rencai.base;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InterviewPageFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;
	private FragmentManager fm;
	public InterviewPageFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList=fragmentList;
		this.fm=fm;
	}
	@Override
	public Fragment getItem(int idx) {
		return fragmentList.get(idx%fragmentList.size());
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList.size();
	}
	@Override  
	public int getItemPosition(Object object) {  
	   return POSITION_NONE;
	}  
	
}