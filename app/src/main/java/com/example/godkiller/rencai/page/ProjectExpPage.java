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
public class ProjectExpPage extends BaseActivity implements View.OnClickListener{
    private Button addEduBgdBtn;
    private ListView eduBgdLv;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edu_background_page);

        addEduBgdBtn = (Button) findViewById(R.id.add_edu_bgd_btn);
        backBtn = (Button) findViewById(R.id.back_button_edu_bgd);
        backBtn.setOnClickListener(this);
        addEduBgdBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_button:
                exitEvent();
                break;
            case R.id.back_button_set:
                finish();
                break;
            case R.id.pw_modify_layout:
                Intent intent = new Intent(ProjectExpPage.this, PasswordModifyPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    public void exitEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        Intent intent = new Intent(ProjectExpPage.this, LoginPage.class);
        startActivity(intent);
    }
}
