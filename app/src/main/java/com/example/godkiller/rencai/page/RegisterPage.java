package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.User;
import com.example.godkiller.rencai.db.UserService;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class RegisterPage extends BaseActivity implements View.OnClickListener{
    private EditText usernameText;
    private EditText passwordText;
    private RadioGroup identityGroup;
    private Button saveBtn;
    private RadioButton hrBtn;
    private RadioButton seekerBtn;
    private String identity;
    private String username;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_page);

        usernameText = (EditText) findViewById(R.id.account_reg);
        passwordText = (EditText) findViewById(R.id.password_reg);
        saveBtn = (Button) findViewById(R.id.save_button_reg);
        hrBtn = (RadioButton) findViewById(R.id.hr_reg);
        seekerBtn = (RadioButton) findViewById(R.id.jobhunter_reg);
        identityGroup = (RadioGroup) findViewById(R.id.identity_group_reg);
        identityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == hrBtn.getId()) {
                    identity = "hr";
                } else if(checkedId == seekerBtn.getId()){
                    identity = "seeker";
                }
            }
        });


        saveBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        registerAccount();

    }

    public void registerAccount() {
        username = usernameText.getText().toString();
        password = passwordText.getText().toString();
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql =  "select * from user where username='" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 0){
            UserService userService = new UserService(this);
            User user = new User();
            user.setIdentity(identity);
            user.setPassword(password);
            user.setUsername(username);
            userService.register(user);
            Toast.makeText(RegisterPage.this, "注册成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterPage.this, "用户名已存在！", Toast.LENGTH_SHORT).show();
        }

    }
}
