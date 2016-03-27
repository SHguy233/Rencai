package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.ActivityCollector;
import com.example.godkiller.rencai.base.BaseActivity;

public class LoginPage extends BaseActivity {

    private EditText accountText;
    private EditText passwordText;
    private RadioGroup requesterGroup;
    private Button loginButton;
    private TextView registerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);

        final Intent registerIntent = new Intent(this, RegisterPage.class);
        final Intent loginIntent = new Intent(this, MainPage.class);
        accountText = (EditText) findViewById(R.id.account_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        requesterGroup = (RadioGroup) findViewById(R.id.requester_group);
        loginButton = (Button) findViewById(R.id.login_button);
        registerView = (TextView) findViewById(R.id.register_view);
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerIntent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(loginIntent);
            }
        });
        };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                ActivityCollector.finishAll();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
