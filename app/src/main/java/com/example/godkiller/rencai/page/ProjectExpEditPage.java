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
import com.example.godkiller.rencai.db.ProjectExp;
import com.example.godkiller.rencai.db.ProjectExpService;
import com.example.godkiller.rencai.db.WorkExp;
import com.example.godkiller.rencai.db.WorkExpService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class ProjectExpEditPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button saveBtn;
    private EditText projectText;
    private LinearLayout startTimeLayout;
    private LinearLayout finishTimeLayout;
    private LinearLayout proDescLayout;
    private TextView startView;
    private TextView finishView;
    private TextView proDescView;



    private int startDay;
    private int startMonth;
    private int startYear;
    private int finishDay;
    private int finishMonth;
    private int finishYear;
    private String proDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.project_exp_edit_page);

        backBtn = (Button) findViewById(R.id.back_button_project_exp_edit);
        saveBtn = (Button) findViewById(R.id.save_btn_pe);
        projectText = (EditText) findViewById(R.id.project_name_view);
        startTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout);
        finishTimeLayout = (LinearLayout) findViewById(R.id.finish_time_layout);
        proDescLayout = (LinearLayout) findViewById(R.id.project_desc_layout);
        startView = (TextView) findViewById(R.id.start_time_view);
        finishView = (TextView) findViewById(R.id.finish_time_view);
        proDescView = (TextView) findViewById(R.id.project_desc_view);
        initDateLayout();

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        startTimeLayout.setOnClickListener(this);
        finishTimeLayout.setOnClickListener(this);
        proDescLayout.setOnClickListener(this);
    }
    private void initDateLayout() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = new Date();
        calendar.setTime(date);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startMonth = calendar.get(Calendar.MONTH);
        startYear = calendar.get(Calendar.YEAR);

        finishDay = calendar.get(Calendar.DAY_OF_MONTH);
        finishMonth = calendar.get(Calendar.MONTH);
        finishYear = calendar.get(Calendar.YEAR);
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            startDay = dayOfMonth;
            startMonth = monthOfYear + 1;
            startYear = year;
            updateStartDate();
        }

        private void updateStartDate() {
            startView.setText(startYear + "-" + startMonth + "-" + startDay);
        }

    };

    private DatePickerDialog.OnDateSetListener finishDateListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            finishDay = dayOfMonth;
            finishMonth = monthOfYear + 1;
            finishYear = year;
            updateFinishDate();
        }

        private void updateFinishDate() {
            finishView.setText(finishYear + "-" + finishMonth + "-" + finishDay);
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_project_exp_edit:
                finish();
                break;
            case R.id.save_btn_pe:
                saveEvent();
                break;
            case R.id.start_time_layout:
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(ProjectExpEditPage.this, startDateListener, startYear, startMonth, startDay);
                startDatePickerDialog.show();
                break;
            case R.id.finish_time_layout:
                DatePickerDialog finishDatePickerDialog = new DatePickerDialog(ProjectExpEditPage.this, finishDateListener, finishYear, finishMonth, finishDay);
                finishDatePickerDialog.show();
                break;
            case R.id.project_desc_layout:
                Intent intent = new Intent(ProjectExpEditPage.this, ProjectDescPage.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ProjectDescPage.PRO_DESC_RESULT_OK:
                Bundle proDescBundle = data.getExtras();
                proDesc = proDescBundle.getString("desc");
                updateProDesc(proDesc);
                break;
            default:
                break;
        }
    }

    private void updateProDesc(String proDesc) {
        proDescView.setText(proDesc);
    }

    public void saveEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        ProjectExp proExp = new ProjectExp();
        proExp.setUsername(username);
        proExp.setProject(projectText.getText().toString());
        proExp.setStartDate(startView.getText().toString());
        proExp.setFinishDate(finishView.getText().toString());
        proExp.setPrjectDesc(proDesc);
        ProjectExpService service = new ProjectExpService(this);
        service.save(proExp);

        Toast.makeText(ProjectExpEditPage.this, "保存成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProjectExpEditPage.this, ProjectExpPage.class));
        finish();

    }

}
