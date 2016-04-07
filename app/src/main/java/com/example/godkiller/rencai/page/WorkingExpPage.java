package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class WorkingExpPage extends BaseActivity implements View.OnClickListener{
    private Button addWorkExpBtn;
    private ListView workExpLv;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.working_exp_page);

        addWorkExpBtn = (Button) findViewById(R.id.add_working_exp_btn);
        backBtn = (Button) findViewById(R.id.back_button_work_exp);
        backBtn.setOnClickListener(this);
        addWorkExpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_work_exp:
                finish();
                break;
            case R.id.pw_modify_layout:
                Intent intent = new Intent(WorkingExpPage.this, PasswordModifyPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
