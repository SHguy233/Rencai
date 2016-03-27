package com.example.godkiller.rencai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.page.SearchPage;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class PositionFragment extends Fragment{
    private Button msgBtn;
    private ImageView searchbarView;
    private Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_fragment, null);
        final Intent searchIntent = new Intent(getActivity(), SearchPage.class);
        msgBtn = (Button) view.findViewById(R.id.msg_btn);
        searchbarView = (ImageView) view.findViewById(R.id.searchbar_view);
        searchbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().startActivity(searchIntent);
            }
        });

        return view;

    }
}
