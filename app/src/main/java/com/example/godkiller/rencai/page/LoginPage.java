package com.example.godkiller.rencai.page;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.ActivityCollector;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.UserService;

public class LoginPage extends BaseActivity {

    private EditText accountText;
    private EditText passwordText;
    private RadioGroup identityGroup;
    private RadioButton seekerBtn;
    private RadioButton hrBtn;
    private RadioButton adminBtn;
    private Button loginButton;
    private TextView registerView;
    private String account;
    private String password;
    private String identity;
    private boolean loginSucc;
    private Intent loginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);

        identity = "seeker";
        final Intent registerIntent = new Intent(this, RegisterPage.class);
        final Intent loginIntent = new Intent(this, SeekerMainPage.class);
        accountText = (EditText) findViewById(R.id.account_login);
        passwordText = (EditText) findViewById(R.id.password_login);
        identityGroup = (RadioGroup) findViewById(R.id.identity_group_login);
        seekerBtn = (RadioButton) findViewById(R.id.seeker_login);
        hrBtn = (RadioButton) findViewById(R.id.hr_login);
        adminBtn = (RadioButton) findViewById(R.id.admin_login);
        loginButton = (Button) findViewById(R.id.login_button);
        registerView = (TextView) findViewById(R.id.register_view);
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerIntent);
            }
        });

        identityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == hrBtn.getId()) {
                    identity = "hr";
                } else if (checkedId == adminBtn.getId()) {
                    identity = "admin";
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginCheck();
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

    private void loginCheck() {
        account = accountText.getText().toString();
        password = passwordText.getText().toString();
        if (account.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginPage.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        }
        UserService userService = new UserService(this);
        loginSucc = userService.login(account, password, identity);
        if (loginSucc) {
            SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", account);
            editor.putString("password", password);
            editor.commit();
            if(identity.equals("seeker")) {
                loginIntent = new Intent(LoginPage.this, SeekerMainPage.class);
            } else if (identity.equals("hr"))
            {
                loginIntent = new Intent(LoginPage.this, HRMainPage.class);
            } else {
                loginIntent = new Intent(LoginPage.this, AdminMainPage.class);
            }
            startActivity(loginIntent);
        } else {
            Toast.makeText(LoginPage.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
        }
    }
}
