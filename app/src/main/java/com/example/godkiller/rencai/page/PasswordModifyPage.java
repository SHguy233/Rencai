package com.example.godkiller.rencai.page;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.UserService;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class PasswordModifyPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button confirmBtn;
    private EditText oldPwText;
    private EditText newPwText;
    private String oldPassword;
    private String newPassword;
    private String username;
    private String password;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.password_modify_page);

        backBtn = (Button) findViewById(R.id.back_button_modify_pw);
        confirmBtn = (Button) findViewById(R.id.confirm_button);
        oldPwText = (EditText) findViewById(R.id.old_password);
        newPwText = (EditText) findViewById(R.id.new_password);
        backBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_modify_pw:
                finish();
                break;
            case R.id.confirm_button:
                passwordModify();
                break;
            default:
                break;
        }
    }

    private void passwordModify() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        Toast.makeText(PasswordModifyPage.this, username, Toast.LENGTH_SHORT).show();
        password = sharedPreferences.getString("password", "");

        oldPassword = oldPwText.getText().toString();
        newPassword = newPwText.getText().toString();

        if (password.equals(oldPassword)) {
            UserService userService = new UserService(this);
            userService.modifyPw(username, newPassword);
            Toast.makeText(PasswordModifyPage.this, "修改成功！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PasswordModifyPage.this, LoginPage.class));
        } else {
            Toast.makeText(PasswordModifyPage.this, "原密码错误！", Toast.LENGTH_SHORT).show();
        }

    }
}
