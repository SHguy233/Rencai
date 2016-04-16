package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GodKiller on 2016/3/5.
 */
public class WelcomePage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_page);
        final Intent intent = new Intent(this, LoginPage.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                loginEvent();
            }
        };
        timer.schedule(task, 1000 * 2);

    }

    public void loginEvent() {
        Intent loginIntent = null;
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (username != null) {
            if(username.equals("seeker")){
                loginIntent = new Intent(this, SeekerMainPage.class);

            } else if(username.equals("hr")){
                loginIntent = new Intent(this, HRMainPage.class);
            }
        } else {
            loginIntent = new Intent(this, LoginPage.class);
        }
        startActivity(loginIntent);
    }
}
