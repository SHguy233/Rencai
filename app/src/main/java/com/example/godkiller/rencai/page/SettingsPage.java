package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.base.PollingUtils;
import com.example.godkiller.rencai.db.PollingService;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class SettingsPage extends BaseActivity implements View.OnClickListener{
    private Button exitBtn;
    private LinearLayout modifyLayout;
    private Button backBtn;
    private Switch notifySwitch;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_page);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        exitBtn = (Button) findViewById(R.id.exit_button);
        backBtn = (Button) findViewById(R.id.back_button_set);
        notifySwitch = (Switch) findViewById(R.id.switch_notify);
        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PollingUtils.startPollingService(SettingsPage.this, 200, PollingService.class, "com.example.godkiller.rencai.db.PollingService", username);
                    System.out.println("Start polling service...");
                } else  {
                    PollingUtils.stopPollingService(SettingsPage.this, PollingService.class, "com.example.godkiller.rencai.db.PollingService");
                    System.out.println("Stop polling service...");
                }
            }
        });
        modifyLayout = (LinearLayout) findViewById(R.id.pw_modify_layout);
        exitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        modifyLayout.setOnClickListener(this);
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
                Intent intent = new Intent(SettingsPage.this, PasswordModifyPage.class);
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
        Intent intent = new Intent(SettingsPage.this, LoginPage.class);
        startActivity(intent);
    }
}
