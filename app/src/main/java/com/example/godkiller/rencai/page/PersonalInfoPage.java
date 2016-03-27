package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/3/7.
 */
public class PersonalInfoPage extends BaseActivity implements View.OnClickListener{
    private EditText nameEditText;
    private EditText phoneNumEditText;
    private LinearLayout genderLayout;
    private LinearLayout birthLayout;
    private LinearLayout workingLayout;
    private LinearLayout workingTimeLayout;
    private LinearLayout livingCityLayout;
    private LinearLayout domicilePlaceLayout;
    private LinearLayout phoneLayout;
    private TextView birthView;
    private TextView genderView;
    private TextView workingView;
    private TextView phoneView;

    private int birthDay;
    private int birthMonth;
    private int birthYear;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal_info_page);

        nameEditText = (EditText) findViewById(R.id.name_view);
        genderLayout = (LinearLayout) findViewById(R.id.gender_layout);
        birthLayout = (LinearLayout) findViewById(R.id.birth_layout);
        workingLayout = (LinearLayout) findViewById(R.id.working_exp_layout);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
        birthView = (TextView) findViewById(R.id.birth_view);
        genderView = (TextView) findViewById(R.id.gender_view);
        workingView = (TextView) findViewById(R.id.working_exp_time_view);
        phoneView = (TextView) findViewById(R.id.phone_view);
        phoneNumEditText = new EditText(this);

        genderLayout.setOnClickListener(this);
        birthLayout.setOnClickListener(this);
        workingLayout.setOnClickListener(this);
        phoneLayout.setOnClickListener(this);
        initDateLayout();

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
            default:
                break;
        }
    }
    private void updateGender(String gender) {
        genderView.setText(gender);
    }
    private void updateExperience(String experience){
        workingView.setText(experience);
    }
    private  void updatePhone(String phone) {
        phoneView.setText(phone);
    }

}
