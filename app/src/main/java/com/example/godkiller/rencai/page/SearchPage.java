package com.example.godkiller.rencai.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/3/7.
 */
public class SearchPage extends BaseActivity implements View.OnClickListener{
    private EditText searchbarText;
    private ImageView cancelView;
    private LinearLayout tradeCategoryLayout;
    private LinearLayout positionCategoryLayout;
    private LinearLayout workingCityLayout;
    private LinearLayout salaryLayout;
    private View positionCategoryView;
    private View workingCityView;
    private View salaryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_page);


        searchbarText = (EditText) findViewById(R.id.seachbar_edittext);
        cancelView = (ImageView) findViewById(R.id.cancel_image);
        tradeCategoryLayout = (LinearLayout) findViewById(R.id.trade_category_layout);
        positionCategoryLayout = (LinearLayout) findViewById(R.id.position_category_layout);
        workingCityLayout = (LinearLayout) findViewById(R.id.working_city_layout);
        salaryLayout = (LinearLayout) findViewById(R.id.salary_layout);

        cancelView.setOnClickListener(this);
        tradeCategoryLayout.setOnClickListener(this);
        positionCategoryLayout.setOnClickListener(this);
        workingCityLayout.setOnClickListener(this);
        salaryLayout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_image:
                jumpIntent(this, MainPage.class);
                break;
            case R.id.trade_category_layout:
                jumpIntent(this, TradeCategoryPage.class);
                break;
            case R.id.position_category_layout:
                jumpIntent(this, PositionCategoryPage.class);
                break;
            case R.id.working_city_layout:
                jumpIntent(this, WorkingCityPage.class);
                break;
            case R.id.salary_layout:
                jumpIntent(this, SalaryPage.class);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                jumpIntent(this, MainPage.class);
                break;
            default:
                break;
        }
        return false;
    }

}
