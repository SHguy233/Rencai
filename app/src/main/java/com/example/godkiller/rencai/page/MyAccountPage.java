package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class MyAccountPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button exitBtn;
    private LinearLayout modifyLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_info_page);

        modifyLayout = (LinearLayout) findViewById(R.id.pw_modify_layout_hr);
        modifyLayout.setOnClickListener(this);
        backBtn = (Button) findViewById(R.id.back_button_ai);
        exitBtn = (Button) findViewById(R.id.exit_button_ai);
        backBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_ai:
                finish();
                break;
            case R.id.exit_button_ai:
                exitEvent();
                break;
            case R.id.pw_modify_layout_hr:
                Intent intent = new Intent(MyAccountPage.this, PasswordModifyPage.class);
                startActivity(intent);
            default:
                break;
        }
    }

    public void exitEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        Intent intent = new Intent(MyAccountPage.this, LoginPage.class);
        startActivity(intent);
    }

}
