package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/3/7.
 */
public class PersonalInfoPage extends BaseActivity implements View.OnClickListener{
    private EditText nameText;
    private EditText phoneNumEditText;
    private LinearLayout genderLayout;
    private LinearLayout birthLayout;
    private LinearLayout workingLayout;
    private LinearLayout remoteAcceptLayout;
    private LinearLayout phoneLayout;
    private TextView birthView;
    private TextView genderView;
    private TextView workingView;
    private TextView remoteAcceptView;
    private TextView phoneView;
    private Button saveBtn;
    private Button backBtn;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_details = "http://10.0.3.2:63342/htdocs/db/person_info_details.php";
    private static  String url_update = "http://10.0.3.2:63342/htdocs/db/person_info_update.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private String name;
    private String gender;
    private String birth;
    private String workexp;
    private String remote;
    private String phone;
    private JSONObject personObj;

    private int birthDay;
    private int birthMonth;
    private int birthYear;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal_info_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_pi);
        saveBtn = (Button) findViewById(R.id.save_btn_pi);
        nameText = (EditText) findViewById(R.id.name_view);
        genderLayout = (LinearLayout) findViewById(R.id.gender_layout);
        birthLayout = (LinearLayout) findViewById(R.id.birth_layout);
        workingLayout = (LinearLayout) findViewById(R.id.working_exp_layout);
        remoteAcceptLayout = (LinearLayout) findViewById(R.id.accept_remote_layout);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
        birthView = (TextView) findViewById(R.id.birth_view);
        genderView = (TextView) findViewById(R.id.gender_view);
        workingView = (TextView) findViewById(R.id.working_exp_time_view);
        remoteAcceptView = (TextView) findViewById(R.id.accept_remote_view);
        phoneView = (TextView) findViewById(R.id.phone_view);
        phoneNumEditText = new EditText(this);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        genderLayout.setOnClickListener(this);
        birthLayout.setOnClickListener(this);
        workingLayout.setOnClickListener(this);
        remoteAcceptLayout.setOnClickListener(this);
        phoneLayout.setOnClickListener(this);
        initDateLayout();
        new GetPersonTask().execute();

    }

    private void initDateLayout() {
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date date = new Date();
        mycalendar.setTime(date);
        birthDay = mycalendar.get(Calendar.DAY_OF_MONTH);
        birthMonth = mycalendar.get(Calendar.MONTH);
        birthYear = mycalendar.get(Calendar.YEAR);
    }


    private DatePickerDialog.OnDateSetListener birthDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            birthYear = myyear;
            birthMonth = monthOfYear + 1;
            birthDay = dayOfMonth;
            updateBirthDate();
        }
        private void updateBirthDate() {
            birthView.setText(birthYear + "-" + birthMonth + "-" + birthDay);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gender_layout:
                AlertDialog.Builder genderBuilder = new AlertDialog.Builder(PersonalInfoPage.this);
                genderBuilder.setTitle("性别");
                final String[] sex = {"男", "女"};
                genderBuilder.setSingleChoiceItems(sex, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateGender(sex[which]);
                        dialog.dismiss();
                    }
                });
                genderBuilder.show();
                break;
            case R.id.birth_layout:
                DatePickerDialog birthDatePickerDialog = new DatePickerDialog(PersonalInfoPage.this, birthDateListener, birthYear, birthMonth, birthDay);
                birthDatePickerDialog.show();
                break;
            case R.id.working_exp_layout:
                AlertDialog.Builder workBuilder = new AlertDialog.Builder(PersonalInfoPage.this);
                workBuilder.setTitle("工作经验");
                final String[] experience = {"无", "1~3年", "3~5年", "5年以上"};
                workBuilder.setSingleChoiceItems(experience, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateExperience(experience[which]);
                        dialog.dismiss();
                    }
                });
                workBuilder.show();
                break;
            case R.id.accept_remote_layout:
                AlertDialog.Builder remoteBuilder = new AlertDialog.Builder(PersonalInfoPage.this);
                remoteBuilder.setTitle("接受异地工作");
                final String[] accept = {"是", "否"};
                remoteBuilder.setSingleChoiceItems(accept, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateRemote(accept[which]);
                        dialog.dismiss();
                    }
                });
                remoteBuilder.show();
                break;
            case R.id.phone_layout:
                AlertDialog.Builder phoneBuilder = new AlertDialog.Builder(PersonalInfoPage.this)
                        .setTitle("联系电话")
                        .setView(phoneNumEditText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                phoneNum = phoneNumEditText.getText().toString();
                                updatePhone(phoneNum);
                            }
                        });
                phoneBuilder.show();
                break;
            case R.id.back_button_pi:
                PersonalInfoPage.this.finish();
                break;
            case R.id.save_btn_pi:
                name = nameText.getText().toString();
                gender = genderView.getText().toString();
                birth = birthView.getText().toString();
                workexp = workingView.getText().toString();
                remote = remoteAcceptView.getText().toString();
                phone = phoneNumEditText.getText().toString();
                new SavePersonTask().execute();
                break;
            default:
                break;
        }
    }


    private void updateRemote(String accept) {remoteAcceptView.setText(accept); }
    private void updateGender(String gender) {
        genderView.setText(gender);
    }
    private void updateExperience(String experience){
        workingView.setText(experience);
    }
    private  void updatePhone(String phone) { phoneView.setText(phone); }

    class GetPersonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PersonalInfoPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("username", username));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_details, "GET", pairs);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray personAry = jsonObject.getJSONArray("info");
                    personObj = personAry.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                nameText.setText(personObj.getString("name"));
                                genderView.setText(personObj.getString("gender"));
                                birthView.setText(personObj.getString("birth"));
                                workingView.setText(personObj.getString("workexptime"));
                                remoteAcceptView.setText(personObj.getString("remote"));
                                phoneView.setText(personObj.getString("phone"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }

    }

    class SavePersonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PersonalInfoPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("name", name));
            pairs.add(new BasicNameValuePair("gender", gender));
            pairs.add(new BasicNameValuePair("birth", birth));
            pairs.add(new BasicNameValuePair("workexptime", workexp));
            pairs.add(new BasicNameValuePair("remote", remote));
            pairs.add(new BasicNameValuePair("phone", phone));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_update, "POST", pairs);

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(PersonalInfoPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
        }

    }


}
