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
public class ProjectExpAddPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button saveBtn;
    private EditText projectText;
    private LinearLayout startTimeLayout;
    private LinearLayout finishTimeLayout;
    private LinearLayout proDescLayout;
    private TextView startView;
    private TextView finishView;
    private TextView proDescView;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/poj_exp_add.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private String project;
    private String start;
    private String finish;
    private String desc;



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
        setContentView(R.layout.project_exp_add_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_project_add);
        saveBtn = (Button) findViewById(R.id.save_btn_pa);
        projectText = (EditText) findViewById(R.id.project_name_view_pa);
        startTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout_pa);
        finishTimeLayout = (LinearLayout) findViewById(R.id.finish_time_layout_pa);
        proDescLayout = (LinearLayout) findViewById(R.id.project_desc_layout_pa);
        startView = (TextView) findViewById(R.id.start_time_view_pa);
        finishView = (TextView) findViewById(R.id.finish_time_view_pa);
        proDescView = (TextView) findViewById(R.id.project_desc_view_pa);
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
            case R.id.back_button_project_add:
                finish();
                break;
            case R.id.save_btn_pa:
                project = projectText.getText().toString();
                start = startView.getText().toString();
                finish = finishView.getText().toString();
                desc = proDescView.getText().toString();
                new EditPojTask().execute();
                break;
            case R.id.start_time_layout_pa:
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(ProjectExpAddPage.this, startDateListener, startYear, startMonth, startDay);
                startDatePickerDialog.show();
                break;
            case R.id.finish_time_layout_pa:
                DatePickerDialog finishDatePickerDialog = new DatePickerDialog(ProjectExpAddPage.this, finishDateListener, finishYear, finishMonth, finishDay);
                finishDatePickerDialog.show();
                break;
            case R.id.project_desc_layout_pa:
                Intent intent = new Intent(ProjectExpAddPage.this, ProjectDescPage.class);
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

    class EditPojTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProjectExpAddPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("project", project));
            pairs.add(new BasicNameValuePair("start", start));
            pairs.add(new BasicNameValuePair("finish", finish));
            pairs.add(new BasicNameValuePair("desc", desc));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "POST", pairs);
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent intent =getIntent();
                    setResult(200, intent);
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
                Toast.makeText(ProjectExpAddPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
