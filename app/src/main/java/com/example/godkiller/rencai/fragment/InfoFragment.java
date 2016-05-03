package com.example.godkiller.rencai.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.page.CollectionPage;
import com.example.godkiller.rencai.page.FollowPage;
import com.example.godkiller.rencai.page.SendRecordPage;
import com.example.godkiller.rencai.page.SettingsPage;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class InfoFragment extends Fragment implements View.OnClickListener{
    private LinearLayout settingsLayout;
    private LinearLayout collectionLayout;
    private LinearLayout recordLayout;
    private LinearLayout followLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, null);
        settingsLayout = (LinearLayout) view.findViewById(R.id.setttings_layout);
        collectionLayout = (LinearLayout) view.findViewById(R.id.collection_layout);
        recordLayout = (LinearLayout) view.findViewById(R.id.send_record_layout);
        followLayout = (LinearLayout) view.findViewById(R.id.follow_layout);
        settingsLayout.setOnClickListener(this);
        collectionLayout.setOnClickListener(this);
        recordLayout.setOnClickListener(this);
        followLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setttings_layout:
                jumpIntent(getActivity(), SettingsPage.class);
                break;
            case R.id.collection_layout:
                jumpIntent(getActivity(), CollectionPage.class);
                break;
            case R.id.send_record_layout:
                jumpIntent(getActivity(), SendRecordPage.class);
                break;
            case R.id.follow_layout:
                jumpIntent(getActivity(), FollowPage.class);
                break;
        }
    }

    public void jumpIntent(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        getActivity().startActivity(intent);
    }
}
