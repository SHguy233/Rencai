package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/password_modify.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.password_modify_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");


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
                oldPassword = oldPwText.getText().toString();
                newPassword = newPwText.getText().toString();
                new ModifyTask().execute();
                break;
            default:
                break;
        }
    }

    class ModifyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PasswordModifyPage.this);
            dialog.setMessage("logining...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("oldPassword", oldPassword));
            pairs.add(new BasicNameValuePair("newPassword", newPassword));
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
                Toast.makeText(PasswordModifyPage.this, "原密码错误！",Toast.LENGTH_SHORT).show();
            } else if (s.equals("success")) {
                Toast.makeText(PasswordModifyPage.this, "修改成功！",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
