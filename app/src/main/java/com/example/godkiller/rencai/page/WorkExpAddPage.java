package com.example.godkiller.rencai.page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class WorkExpAddPage extends BaseActivity implements View.OnClickListener{
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
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/work_exp_add.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private String company;
    private String entry;
    private String leave;
    private String trade;
    private String position;
    private String desc;



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
        setContentView(R.layout.work_exp_add_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_work_exp_add);
        saveBtn = (Button) findViewById(R.id.save_btn_wa);
        companyText = (EditText) findViewById(R.id.company_name_view_wa);
        tradeText = (EditText) findViewById(R.id.trade_name_text_wa);
        positionText = (EditText) findViewById(R.id.position_name_text_wa);
        entryTimeLayout = (LinearLayout) findViewById(R.id.entry_time_layout_wa);
        leaveTimeLayout = (LinearLayout) findViewById(R.id.leave_time_layout_wa);
        jobDescLayout = (LinearLayout) findViewById(R.id.job_desc_layout_wa);
        entryView = (TextView) findViewById(R.id.entry_time_view_wa);
        leaveView = (TextView) findViewById(R.id.leave_time_view_wa);
        jobDescView = (TextView) findViewById(R.id.job_desc_view_wa);
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
            case R.id.back_button_work_exp_add:
                finish();
                break;
            case R.id.save_btn_wa:
                company = companyText.getText().toString();
                entry = entryView.getText().toString();
                leave = leaveView.getText().toString();
                trade = tradeText.getText().toString();
                position = positionText.getText().toString();
                desc = jobDescView.getText().toString();
                new EditWorkTask().execute();
                break;
            case R.id.entry_time_layout_wa:
                DatePickerDialog entryDatePickerDialog = new DatePickerDialog(WorkExpAddPage.this, entryDateListener, entryYear, entryMonth, entryDay);
                entryDatePickerDialog.show();
                break;
            case R.id.leave_time_layout_wa:
                DatePickerDialog leaveDatePickerDialog = new DatePickerDialog(WorkExpAddPage.this, leaveDateListener, leaveYear, leaveMonth, leaveDay);
                leaveDatePickerDialog.show();
                break;
            case R.id.job_desc_layout_wa:
                Intent intent = new Intent(WorkExpAddPage.this, JobDescPage.class);
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

    class EditWorkTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(WorkExpAddPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("company", company));
            pairs.add(new BasicNameValuePair("entry", entry));
            pairs.add(new BasicNameValuePair("leave", leave));
            pairs.add(new BasicNameValuePair("trade", trade));
            pairs.add(new BasicNameValuePair("position", position));
            pairs.add(new BasicNameValuePair("desc", desc));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent intent =getIntent();
                    setResult(300, intent);
                    finish();
                    return "success";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("success")) {
                Toast.makeText(WorkExpAddPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

