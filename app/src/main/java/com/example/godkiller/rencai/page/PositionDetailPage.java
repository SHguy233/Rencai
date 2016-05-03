package com.example.godkiller.rencai.page;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.ActivityCollector;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.fragment.CompanyDetailFragment;
import com.example.godkiller.rencai.fragment.InfoFragment;
import com.example.godkiller.rencai.fragment.InterviewFragment;
import com.example.godkiller.rencai.fragment.PositionDetailFragment;
import com.example.godkiller.rencai.fragment.PositionFragment;
import com.example.godkiller.rencai.fragment.ResumeFragment;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class PositionDetailPage extends BaseActivity implements View.OnClickListener{

    private Button positionBtn;
    private Button companyBtn;
    private Button backBtn;

    private PositionDetailFragment positionDetailFragment;
    private CompanyDetailFragment companyDetailFragment;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.position_detail_page);

        initView();
    }


    /**
     * 初始化组件
     */
    private void initView(){
        positionBtn = (Button) findViewById(R.id.position_detail_btn);
        companyBtn = (Button) findViewById(R.id.company_detail_btn);
        backBtn = (Button) findViewById(R.id.back_button_position_detail);
        positionBtn.setOnClickListener(this);
        companyBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        setDefaultFragment();


    }

    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        positionDetailFragment = new PositionDetailFragment();
        transaction.replace(R.id.detail_frame, positionDetailFragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.position_detail_btn:
                if (positionDetailFragment == null) {
                    positionDetailFragment = new PositionDetailFragment();
                }
                transaction.replace(R.id.detail_frame, positionDetailFragment);
                break;
            case R.id.company_detail_btn:
                if (companyDetailFragment == null) {
                    companyDetailFragment = new CompanyDetailFragment();
                }
                transaction.replace(R.id.detail_frame, companyDetailFragment);
                break;
            case R.id.back_button_position_detail:
                finish();
                break;
        }
        transaction.commit();
    }
}
