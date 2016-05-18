package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

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
        username = usernameText.getText().toString();
        password = passwordText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterPage.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        }
        new RegisterTask().execute();

    }

    class RegisterTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterPage.this);
            dialog.setMessage("logining...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            pairs.add(new BasicNameValuePair("identity", identity));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            //Log.d("insert user", jsonObject.toString());
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                String message = jsonObject.getString(TAG_MESSAGE);
                if (success == 1) {
                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(intent);
                    finish();
                } else if (success == 0){
                    if (message.equals("existed")){
                        return "existed";
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
            if (s.equals("existed")) {
                Toast.makeText(RegisterPage.this, "用户名已存在！",Toast.LENGTH_SHORT).show();
            } else if (s.equals("success")) {
                Toast.makeText(RegisterPage.this, "注册成功！",Toast.LENGTH_SHORT).show();
            }
        }

    }

}
