package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/5/2.
 */
public class SeekerConditionPage extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch ageSwitch;
    private Switch genderSwitch;
    private Switch computerSwitch;
    private Switch remoteSwitch;
    private Switch foreignSwitch;
    private Switch degreeSwitch;
    private Switch workSwitch;
    private TextView ageView;
    private TextView genderView;
    private TextView degreeView;
    private TextView foreignView;
    private TextView computerView;
    private TextView remoteView;
    private TextView workView;
    private Button sendBtn;
    private int age;
    private int gender;
    private int computer;
    private int remote;
    private int foreign;
    private int degree;
    private int work;
    private String id;
    private String username;
    private static String url_insert = "http://10.0.3.2:63342/htdocs/db/seeker_resume_send.php";
    private static String url_get = "http://10.0.3.2:63342/htdocs/db/seeker_position_condition.php";
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private JSONObject conObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seeker_condition_page);

        ageView = (TextView) findViewById(R.id.condition_age_desc);
        genderView = (TextView) findViewById(R.id.condition_gender_desc);
        degreeView = (TextView) findViewById(R.id.condition_degree_desc);
        foreignView = (TextView) findViewById(R.id.condition_foreign_desc);
        computerView = (TextView) findViewById(R.id.condition_computer_desc);
        remoteView = (TextView) findViewById(R.id.condition_remote_desc);
        workView = (TextView) findViewById(R.id.condition_workexp_desc);

        ageSwitch = (Switch) findViewById(R.id.age_switch);
        genderSwitch = (Switch) findViewById(R.id.gender_switch);
        computerSwitch = (Switch) findViewById(R.id.computer_switch);
        remoteSwitch = (Switch) findViewById(R.id.remote_switch);
        foreignSwitch = (Switch) findViewById(R.id.foreign_switch);
        degreeSwitch = (Switch) findViewById(R.id.degree_switch);
        workSwitch = (Switch) findViewById(R.id.workexp_switch);
        sendBtn = (Button) findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendResumeTask().execute();
                startActivity(new Intent(SeekerConditionPage.this, SeekerMainPage.class));
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        id = getIntent().getStringExtra("id");
        Toast.makeText(SeekerConditionPage.this, id, Toast.LENGTH_SHORT).show();
        ageSwitch.setOnCheckedChangeListener(this);
        genderSwitch.setOnCheckedChangeListener(this);
        computerSwitch.setOnCheckedChangeListener(this);
        remoteSwitch.setOnCheckedChangeListener(this);
        foreignSwitch.setOnCheckedChangeListener(this);
        degreeSwitch.setOnCheckedChangeListener(this);
        workSwitch.setOnCheckedChangeListener(this);
        new GetPositionCondition().execute();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.age_switch:
                age = setCondition(isChecked);
                break;
            case R.id.gender_switch:
                gender = setCondition(isChecked);
                break;
            case R.id.computer_switch:
                computer = setCondition(isChecked);
                break;
            case R.id.workexp_switch:
                work = setCondition(isChecked);
                break;
            case R.id.foreign_switch:
                foreign = setCondition(isChecked);
                break;
            case R.id.degree_switch:
                degree = setCondition(isChecked);
                break;
            case R.id.remote_switch:
                remote = setCondition(isChecked);
                break;
        }
    }

    private int setCondition(boolean ischecked) {
        if (ischecked) {
            return 1;
        } else {
            return 0;
        }
    }

    class SendResumeTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SeekerConditionPage.this);
            dialog.setMessage("sending...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("id", id));
            pairs.add(new BasicNameValuePair("gender", gender + ""));
            pairs.add(new BasicNameValuePair("age", age + ""));
            pairs.add(new BasicNameValuePair("foreign", foreign + ""));
            pairs.add(new BasicNameValuePair("computer", computer + ""));
            pairs.add(new BasicNameValuePair("workexp", work + ""));
            pairs.add(new BasicNameValuePair("remote", remote + ""));
            pairs.add(new BasicNameValuePair("degree", degree + ""));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("success")) {
                Toast.makeText(SeekerConditionPage.this, "投递成功！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    class GetPositionCondition extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SeekerConditionPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", id));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_get, "GET", pairs);
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray conAry = jsonObject.getJSONArray("info");
                    conObj = conAry.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ageView.setText(conObj.getString("age"));
                                genderView.setText(conObj.getString("gender"));
                                degreeView.setText(conObj.getString("degree"));
                                workView.setText(conObj.getString("workexp"));
                                remoteView.setText(conObj.getString("remote"));
                                foreignView.setText(conObj.getString("foreign"));
                                computerView.setText(conObj.getString("computer"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }
    }
}
