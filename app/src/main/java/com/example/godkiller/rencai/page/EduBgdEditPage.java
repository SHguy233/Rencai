package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.EduBgd;
import com.example.godkiller.rencai.db.EdubgdService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class EduBgdEditPage extends BaseActivity implements View.OnClickListener{
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
        setContentView(R.layout.edu_background_edit_page);

        backBtn = (Button) findViewById(R.id.back_button_edu_bgd_edit);
        saveBtn = (Button) findViewById(R.id.save_btn_eb);
        collegeText = (EditText) findViewById(R.id.college_name_view);
        majorText = (EditText) findViewById(R.id.major_name_text);
        enrollTimeLayout = (LinearLayout) findViewById(R.id.enroll_time_layout);
        graduateTimeLayout = (LinearLayout) findViewById(R.id.graduate_time_layout);
        majorNameLayout = (LinearLayout) findViewById(R.id.major_name_layout);
        degreeLayout = (LinearLayout) findViewById(R.id.degree_layout);
        enrollView = (TextView) findViewById(R.id.enroll_time_view);
        graduateView = (TextView) findViewById(R.id.graduate_time_view);
        degreeView = (TextView) findViewById(R.id.degree_view);
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
            case R.id.back_button_edu_bgd_edit:
                finish();
                break;
            case R.id.save_btn_eb:
                saveEvent();
                break;
            case R.id.enroll_time_layout:
                DatePickerDialog enrollDatePickerDialog = new DatePickerDialog(EduBgdEditPage.this, enrollDateListener, enrollYear, enrollMonth, enrollDay);
                enrollDatePickerDialog.show();
                break;
            case R.id.graduate_time_layout:
                DatePickerDialog graduateDatePickerDialog = new DatePickerDialog(EduBgdEditPage.this, graduateDateListener, graduateYear, graduateMonth, graduateDay);
                graduateDatePickerDialog.show();
                break;
            case R.id.degree_layout:
                AlertDialog.Builder degreeBuilder = new AlertDialog.Builder(EduBgdEditPage.this);
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
    public void saveEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        EduBgd eduBgd = new EduBgd();
        eduBgd.setUsername(username);
        eduBgd.setCollege(collegeText.getText().toString());
        eduBgd.setEnrollDate(enrollView.getText().toString());
        eduBgd.setGraduateDate(graduateView.getText().toString());
        eduBgd.setMajor(majorText.getText().toString());
        eduBgd.setDegree(degreeView.getText().toString());
        EdubgdService edubgdService = new EdubgdService(this);
        edubgdService.save(eduBgd);

        Toast.makeText(EduBgdEditPage.this, "保存成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EduBgdEditPage.this, EduBgdPage.class));
        finish();

    }
    private void updateDegree(String degree) {
        degreeView.setText(degree);
    }

}
