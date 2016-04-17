package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GodKiller on 2016/3/5.
 */
public class WelcomePage extends BaseActivity {
    private String username;
    private String identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_page);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        identity = sharedPreferences.getString("identity", "");
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

        if (username != null && identity != null) {
            if(identity.equals("seeker")){
                loginIntent = new Intent(this, SeekerMainPage.class);

            } else if(identity.equals("hr")){
                loginIntent = new Intent(this, HRMainPage.class);
            }
        } else {
            loginIntent = new Intent(this, LoginPage.class);
        }
        startActivity(loginIntent);
    }
}
