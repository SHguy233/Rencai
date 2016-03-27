package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.page.LoginPage;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class RegisterPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_page);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final Intent intent = new Intent(this, LoginPage.class);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(intent);
        }
        return false;
    }
}
