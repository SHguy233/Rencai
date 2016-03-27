package com.example.godkiller.rencai.page;

import android.os.Bundle;
import android.view.Window;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/3/9.
 */
public class PositionCategoryPage extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.position_category_page);
    }
}
