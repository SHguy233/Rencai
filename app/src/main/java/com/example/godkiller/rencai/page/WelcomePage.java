package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.page.LoginPage;

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
             startActivity(intent);
            }
        };
        timer.schedule(task, 1000 * 2);

    }
}
