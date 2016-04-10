package com.example.godkiller.rencai.page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.godkiller.rencai.db.WorkExp;
import com.example.godkiller.rencai.db.WorkExpService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class WorkExpEditPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button saveBtn;
    private EditText companyText;
    private EditText tradeText;
    private EditText positionText;
    private LinearLayout entryTimeLayout;
    private LinearLayout leaveTimeLayout;
    private LinearLayout jobDescLayout;
    private TextView entryView;
    private TextView leaveView;
    private TextView jobDescView;



    private int entryDay;
    private int entryMonth;
    private int entryYear;
    private int leaveDay;
    private int leaveMonth;
    private int leaveYear;
    private String jobDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.work_exp_edit_page);

        backBtn = (Button) findViewById(R.id.back_button_work_exp_edit);
        saveBtn = (Button) findViewById(R.id.save_btn_we);
        companyText = (EditText) findViewById(R.id.company_name_view);
        tradeText = (EditText) findViewById(R.id.trade_name_text);
        positionText = (EditText) findViewById(R.id.position_name_text);
        entryTimeLayout = (LinearLayout) findViewById(R.id.entry_time_layout);
        leaveTimeLayout = (LinearLayout) findViewById(R.id.leave_time_layout);
        jobDescLayout = (LinearLayout) findViewById(R.id.job_desc_layout);
        entryView = (TextView) findViewById(R.id.entry_time_view);
        leaveView = (TextView) findViewById(R.id.leave_time_view);
        jobDescView = (TextView) findViewById(R.id.job_desc_view);
        initDateLayout();

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        entryTimeLayout.setOnClickListener(this);
        leaveTimeLayout.setOnClickListener(this);
        jobDescLayout.setOnClickListener(this);
    }
    private void initDateLayout() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = new Date();
        calendar.setTime(date);
        entryDay = calendar.get(Calendar.DAY_OF_MONTH);
        entryMonth = calendar.get(Calendar.MONTH);
        entryYear = calendar.get(Calendar.YEAR);

        leaveDay = calendar.get(Calendar.DAY_OF_MONTH);
        leaveMonth = calendar.get(Calendar.MONTH);
        leaveYear = calendar.get(Calendar.YEAR);
    }

    private DatePickerDialog.OnDateSetListener entryDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            entryDay = dayOfMonth;
            entryMonth = monthOfYear + 1;
            entryYear = year;
            updateEntryDate();
        }

        private void updateEntryDate() {
            entryView.setText(entryYear + "-" + entryMonth + "-" + entryDay);
        }

    };

    private DatePickerDialog.OnDateSetListener leaveDateListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            leaveDay = dayOfMonth;
            leaveMonth = monthOfYear + 1;
            leaveYear = year;
            updateLeaveDate();
        }

        private void updateLeaveDate() {
            leaveView.setText(leaveYear + "-" + leaveMonth + "-" + leaveDay);
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_work_exp_edit:
                finish();
                break;
            case R.id.save_btn_we:
                saveEvent();
                break;
            case R.id.entry_time_layout:
                DatePickerDialog entryDatePickerDialog = new DatePickerDialog(WorkExpEditPage.this, entryDateListener, entryYear, entryMonth, entryDay);
                entryDatePickerDialog.show();
                break;
            case R.id.leave_time_layout:
                DatePickerDialog leaveDatePickerDialog = new DatePickerDialog(WorkExpEditPage.this, leaveDateListener, leaveYear, leaveMonth, leaveDay);
                leaveDatePickerDialog.show();
                break;
            case R.id.job_desc_layout:
                Intent intent = new Intent(WorkExpEditPage.this, JobDescPage.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case JobDescPage.JOB_DESC_RESULT_OK:
                Bundle jobDescBundle = data.getExtras();
                jobDesc = jobDescBundle.getString("desc");
                updateJobDesc(jobDesc);
                break;
            default:
                break;
        }
    }

    private void updateJobDesc(String jobDesc) {
        jobDescView.setText(jobDesc);
    }

    public void saveEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        WorkExp workExp = new WorkExp();
        workExp.setUsername(username);
        workExp.setCompany(companyText.getText().toString());
        workExp.setEntryDate(entryView.getText().toString());
        workExp.setLeaveDate(leaveView.getText().toString());
        workExp.setPosition(positionText.getText().toString());
        workExp.setTrade(tradeText.getText().toString());
        workExp.setJobDesc(jobDesc);
        WorkExpService service = new WorkExpService(this);
        service.save(workExp);

        Toast.makeText(WorkExpEditPage.this, "保存成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(WorkExpEditPage.this, WorkingExpPage.class));
        finish();

    }

}
