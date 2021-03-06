package com.example.godkiller.rencai.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.page.EduBgdPage;
import com.example.godkiller.rencai.page.PersonalInfoPage;
import com.example.godkiller.rencai.page.ProjectExpPage;
import com.example.godkiller.rencai.page.WorkExpPage;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class ResumeFragment extends Fragment implements View.OnClickListener{

    private LinearLayout personalInfoLayout;
    private LinearLayout eduBgdLayout;
    private LinearLayout workExpLayout;
    private LinearLayout projectExpLayout;
    private String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resume_fragment, null);
        personalInfoLayout = (LinearLayout) view.findViewById(R.id.personal_info_layout);
        eduBgdLayout = (LinearLayout) view.findViewById(R.id.edu_bgd_layout);
        workExpLayout = (LinearLayout) view.findViewById(R.id.working_exp_layout);
        projectExpLayout = (LinearLayout) view.findViewById(R.id.project_exp_layout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        personalInfoLayout.setOnClickListener(this);
        eduBgdLayout.setOnClickListener(this);
        workExpLayout.setOnClickListener(this);
        projectExpLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_info_layout:
                jumpIntent(getActivity(), PersonalInfoPage.class);
                break;
            case R.id.edu_bgd_layout:
                jumpIntent(getActivity(), EduBgdPage.class);
                break;
            case R.id.working_exp_layout:
                jumpIntent(getActivity(), WorkExpPage.class);
                break;
            case R.id.project_exp_layout:
                jumpIntent(getActivity(), ProjectExpPage.class);
                break;
            default:
                break;
        }
    }

    public void jumpIntent(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        getActivity().startActivity(intent);
    }
}
