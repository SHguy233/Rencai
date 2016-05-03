package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfDemand;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.db.PositionInfo;
import com.example.godkiller.rencai.db.PositionInfoService;
import com.example.godkiller.rencai.position.PositionPageOfDemand;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class PositionAddPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button releaseBtn;
    private LinearLayout workPlaceLayout;
    private LinearLayout positionLayout;
    private LinearLayout numLayout;
    private LinearLayout salaryLayout;
    private LinearLayout conditionLayout;
    private LinearLayout degreeLayout;
    private LinearLayout workexpLayout;
    private LinearLayout genderLayout;
    private TextView workPlaceView;
    private TextView positionView;
    private EditText numText;
    private EditText ageText;
    private EditText foreignText;
    private EditText computerText;
    private EditText remoteText;
    private EditText salaryText;
    private TextView conditionView;
    private TextView workexpView;
    private TextView degreeView;
    private TextView genderView;
    private String conDesc;
    private String username;
    private String company;
    private String city;
    private String position;
    private String salary;
    private String num;
    private String workexp;
    private String degree;
    private String condition;
    private String age;
    private String gender;
    private String computer;
    private String foreign;
    private String remote;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/hr_position_add.php";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_position_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_add_position);
        releaseBtn = (Button) findViewById(R.id.release_btn_mp);
        workPlaceLayout = (LinearLayout) findViewById(R.id.working_place_layout_mp);
        positionLayout = (LinearLayout) findViewById(R.id.job_category_layout_mp);
        numLayout = (LinearLayout) findViewById(R.id.demand_num_layout_mp);
        salaryLayout = (LinearLayout) findViewById(R.id.salary_layout_mp);
        conditionLayout = (LinearLayout) findViewById(R.id.condition_layout_mp);
        workexpLayout = (LinearLayout) findViewById(R.id.workexp_layout_mp);
        degreeLayout = (LinearLayout) findViewById(R.id.degree_layout_mp);
        genderLayout = (LinearLayout) findViewById(R.id.gender_layout_mp);
        workPlaceView = (TextView) findViewById(R.id.working_place_view_mp);
        positionView = (TextView) findViewById(R.id.job_category_view_mp);
        numText = (EditText) findViewById(R.id.demand_num_view_mp);
        salaryText = (EditText) findViewById(R.id.salary_view_mp);
        ageText = (EditText) findViewById(R.id.age_text_mp);
        foreignText = (EditText) findViewById(R.id.foreign_text_mp);
        computerText = (EditText) findViewById(R.id.computer_text_mp);
        remoteText = (EditText) findViewById(R.id.remote_text_mp);
        conditionView = (TextView) findViewById(R.id.condition_view_mp);
        degreeView = (TextView) findViewById(R.id.degree_view_mp);
        workexpView = (TextView) findViewById(R.id.workexp_view_mp);
        genderView = (TextView) findViewById(R.id.gender_view_mp);

        backBtn.setOnClickListener(this);
        releaseBtn.setOnClickListener(this);
        workPlaceLayout.setOnClickListener(this);
        positionLayout.setOnClickListener(this);
        numLayout.setOnClickListener(this);
        conditionLayout.setOnClickListener(this);
        salaryLayout.setOnClickListener(this);
        degreeLayout.setOnClickListener(this);
        workexpLayout.setOnClickListener(this);
        genderLayout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_add_position:
                finish();
                break;
            case R.id.release_btn_mp:
                position = positionView.getText().toString();
                num = numText.getText().toString();
                salary = salaryText.getText().toString();
                degree = degreeView.getText().toString();
                workexp = workexpView.getText().toString();
                condition = conditionView.getText().toString();
                age = ageText.getText().toString();
                gender = genderView.getText().toString();
                computer = computerText.getText().toString();
                foreign = foreignText.getText().toString();
                remote = remoteText.getText().toString();
                new SavePositionTask().execute();
                break;
            case R.id.working_place_layout_mp:
                Intent cityIntent = new Intent(PositionAddPage.this, CityListOfDemand.class);
                startActivityForResult(cityIntent, 0);
                break;
            case R.id.job_category_layout_mp:
                Intent positionIntent = new Intent(PositionAddPage.this, PositionPageOfDemand.class);
                startActivityForResult(positionIntent, 0);
                break;
            case R.id.degree_layout_mp:
                AlertDialog.Builder degreeBuilder = new AlertDialog.Builder(PositionAddPage.this);
                degreeBuilder.setTitle("学历/学位");
                final String[] degree = {"初中", "中专", "高中", "大专", "本科", "硕士", "博士"};
                degreeBuilder.setSingleChoiceItems(degree, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDegree(degree[which]);
                        dialog.dismiss();
                    }
                });
                degreeBuilder.show();
                break;
            case R.id.workexp_layout_mp:
                AlertDialog.Builder workBuilder = new AlertDialog.Builder(PositionAddPage.this);
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
            case R.id.condition_layout_mp:
                Intent intent = new Intent(PositionAddPage.this,DemandDescPage.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.gender_layout_mp:
                AlertDialog.Builder genderBuilder = new AlertDialog.Builder(PositionAddPage.this);
                genderBuilder.setTitle("性别");
                final String[] gender = {"男", "女", "不限"};
                genderBuilder.setSingleChoiceItems(gender, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateGender(gender[which]);
                        dialog.dismiss();
                    }
                });
                genderBuilder.show();


        }
    }

    private void updateExperience(String exp) {
        workexpView.setText(exp);
    }

    private void updateDegree(String degree) {
        degreeView.setText(degree);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case CityListOfDemand.CITY_RESULT_OK:
                Bundle cityBundle = data.getExtras();
                String city = cityBundle.getString("city");
                updateWorkPlace(city);
                break;
            case PositionPageOfDemand.POSITION_RESULT_OK:
                Bundle positionBundle = data.getExtras();
                String position = positionBundle.getString("position");
                updatePosition(position);
                break;
            case DemandDescPage.CON_DESC_RESULT_OK:
                Bundle busDescBundle = data.getExtras();
                conDesc = busDescBundle.getString("desc");
                updateConDesc(conDesc);
                break;


        }
    }

    private void updateConDesc(String conDesc) {
        conditionView.setText(conDesc);
    }


    private void updateWorkPlace(String city) { workPlaceView.setText(city);}
    private void updateGender(String gender) { genderView.setText(gender);}
    private void updatePosition(String position) {
        positionView.setText(position);
    }

    class SavePositionTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PositionAddPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("num", num));
            pairs.add(new BasicNameValuePair("position", position));
            pairs.add(new BasicNameValuePair("salary", salary));
            pairs.add(new BasicNameValuePair("degree", degree));
            pairs.add(new BasicNameValuePair("workexp", workexp));
            pairs.add(new BasicNameValuePair("condition", condition));
            pairs.add(new BasicNameValuePair("age", age));
            pairs.add(new BasicNameValuePair("gender", gender));
            pairs.add(new BasicNameValuePair("computer", computer));
            pairs.add(new BasicNameValuePair("foreign", foreign));
            pairs.add(new BasicNameValuePair("remote", remote));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent intent =getIntent();
                    setResult(400, intent);
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("success")) {
                Toast.makeText(PositionAddPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        }

    }




}
