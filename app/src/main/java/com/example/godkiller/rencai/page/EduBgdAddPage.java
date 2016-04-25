package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class EduBgdAddPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button saveBtn;
    private EditText collegeText;
    private EditText majorText;
    private LinearLayout enrollTimeLayout;
    private LinearLayout graduateTimeLayout;
    private LinearLayout majorNameLayout;
    private LinearLayout degreeLayout;
    private TextView enrollView;
    private TextView graduateView;
    private TextView degreeView;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/edu_bgd_add.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private String college;
    private String enroll;
    private String graduate;
    private String major;
    private String degree;



    private int enrollDay;
    private int enrollMonth;
    private int enrollYear;
    private int graduateDay;
    private int graduateMonth;
    private int graduateYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edu_background_add_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_ea);
        saveBtn = (Button) findViewById(R.id.save_btn_ea);
        collegeText = (EditText) findViewById(R.id.college_name_view_ea);
        majorText = (EditText) findViewById(R.id.major_name_text_ea);
        enrollTimeLayout = (LinearLayout) findViewById(R.id.enroll_time_layout_ea);
        graduateTimeLayout = (LinearLayout) findViewById(R.id.graduate_time_layout_ea);
        majorNameLayout = (LinearLayout) findViewById(R.id.major_name_layout_ea);
        degreeLayout = (LinearLayout) findViewById(R.id.degree_layout_ea);
        enrollView = (TextView) findViewById(R.id.enroll_time_view_ea);
        graduateView = (TextView) findViewById(R.id.graduate_time_view_ea);
        degreeView = (TextView) findViewById(R.id.degree_view_ea);
        initDateLayout();

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        enrollTimeLayout.setOnClickListener(this);
        graduateTimeLayout.setOnClickListener(this);
        majorNameLayout.setOnClickListener(this);
        degreeLayout.setOnClickListener(this);
    }
    private void initDateLayout() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = new Date();
        calendar.setTime(date);
        enrollDay = calendar.get(Calendar.DAY_OF_MONTH);
        enrollMonth = calendar.get(Calendar.MONTH);
        enrollYear = calendar.get(Calendar.YEAR);

        graduateDay = calendar.get(Calendar.DAY_OF_MONTH);
        graduateMonth = calendar.get(Calendar.MONTH);
        graduateYear = calendar.get(Calendar.YEAR);
    }

    private DatePickerDialog.OnDateSetListener enrollDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            enrollDay = dayOfMonth;
            enrollMonth = monthOfYear + 1;
            enrollYear = year;
            updateEnrollDate();
        }

        private void updateEnrollDate() {
            enrollView.setText(enrollYear + "-" + enrollMonth + "-" + enrollDay);
        }

    };

    private DatePickerDialog.OnDateSetListener graduateDateListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            graduateDay = dayOfMonth;
            graduateMonth = monthOfYear + 1;
            graduateYear = year;
            updateGraduateDate();
        }

        private void updateGraduateDate() {
            graduateView.setText(graduateYear + "-" + graduateMonth + "-" + graduateDay);
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_ea:
                finish();
                break;
            case R.id.save_btn_ea:
                college = collegeText.getText().toString();
                enroll = enrollView.getText().toString();
                graduate = graduateView.getText().toString();
                major = majorText.getText().toString();
                degree = degreeView.getText().toString();
                new EditTask().execute();
                break;
            case R.id.enroll_time_layout_ea:
                DatePickerDialog enrollDatePickerDialog = new DatePickerDialog(EduBgdAddPage.this, enrollDateListener, enrollYear, enrollMonth, enrollDay);
                enrollDatePickerDialog.show();
                break;
            case R.id.graduate_time_layout_ea:
                DatePickerDialog graduateDatePickerDialog = new DatePickerDialog(EduBgdAddPage.this, graduateDateListener, graduateYear, graduateMonth, graduateDay);
                graduateDatePickerDialog.show();
                break;
            case R.id.degree_layout_ea:
                AlertDialog.Builder degreeBuilder = new AlertDialog.Builder(EduBgdAddPage.this);
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

            default:
                break;

        }
    }

    class EditTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EduBgdAddPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("college", college));
            pairs.add(new BasicNameValuePair("enroll", enroll));
            pairs.add(new BasicNameValuePair("graduate", graduate));
            pairs.add(new BasicNameValuePair("major", major));
            pairs.add(new BasicNameValuePair("degree", degree));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent intent =getIntent();
                    setResult(100, intent);
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
                Toast.makeText(EduBgdAddPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void updateDegree(String degree) {
        degreeView.setText(degree);
    }

}
