package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.db.UserService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);

        identity = "seeker";
        final Intent registerIntent = new Intent(this, RegisterPage.class);
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
                account = accountText.getText().toString();
                password = passwordText.getText().toString();
                if (account.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginPage.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                }
                new LoginTask().execute();
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

    class LoginTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginPage.this);
            dialog.setMessage("logining...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", account));
            pairs.add(new BasicNameValuePair("password", password));
            pairs.add(new BasicNameValuePair("identity", identity));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            //Log.d("insert user", jsonObject.toString());
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                String message = jsonObject.getString(TAG_MESSAGE);
                if (success == 1) {
                    Intent intent = null;
                    if (identity.equals("seeker")) {
                        intent = new Intent(getApplicationContext(), SeekerMainPage.class);
                    } else if (identity.equals("hr")) {
                        intent = new Intent(getApplicationContext(),HRMainPage.class);
                    }
                    startActivity(intent);
                    SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", account);
                    editor.putString("identity", identity);
                    editor.commit();
                    finish();
                } else if (success == 0){
                    if (message.equals("failed")){
                        return "failed";
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("failed")) {
                Toast.makeText(LoginPage.this, "用户名或密码错误！",Toast.LENGTH_SHORT).show();
            } else if (s.equals("success")) {
                Toast.makeText(LoginPage.this, "登录成功！",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
