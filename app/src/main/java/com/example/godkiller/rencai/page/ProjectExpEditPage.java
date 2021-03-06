package com.example.godkiller.rencai.page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class ProjectExpEditPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button saveBtn;
    private Button deleteBtn;
    private EditText projectText;
    private LinearLayout startTimeLayout;
    private LinearLayout finishTimeLayout;
    private LinearLayout proDescLayout;
    private TextView startView;
    private TextView finishView;
    private TextView proDescView;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_details = "http://10.0.3.2:63342/htdocs/db/poj_exp_details.php";
    private static  String url_update = "http://10.0.3.2:63342/htdocs/db/poj_exp_update.php";
    private static  String url_delete = "http://10.0.3.2:63342/htdocs/db/poj_exp_delete.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private String project;
    private String start;
    private String finish;
    private String desc;
    private String id;
    private JSONObject pojObj;



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

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Toast.makeText(ProjectExpEditPage.this, id, Toast.LENGTH_SHORT).show();

        backBtn = (Button) findViewById(R.id.back_button_project_exp_edit);
        saveBtn = (Button) findViewById(R.id.save_btn_pe);
        deleteBtn = (Button) findViewById(R.id.delete_button_poj_edit);
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
        deleteBtn.setOnClickListener(this);
        startTimeLayout.setOnClickListener(this);
        finishTimeLayout.setOnClickListener(this);
        proDescLayout.setOnClickListener(this);
        new GetPojTask().execute();
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
            case R.id.delete_button_poj_edit:
                new DeletePojTask().execute();
                break;
            case R.id.back_button_project_exp_edit:
                finish();
                break;
            case R.id.save_btn_pe:
                project = projectText.getText().toString();
                start = startView.getText().toString();
                finish = finishView.getText().toString();
                desc = proDescView.getText().toString();
                new SavePojTask().execute();
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

    class GetPojTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProjectExpEditPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("id", id));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_details, "GET", pairs);
                JSONArray pojAry = jsonObject.getJSONArray("info");
                pojObj = pojAry.getJSONObject(0);
                Log.d("pojobj", pojObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        projectText.setText(pojObj.getString("project"));
                        startView.setText(pojObj.getString("start"));
                        finishView.setText(pojObj.getString("finish"));
                        proDescView.setText(pojObj.getString("desc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }

    }

    class SavePojTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProjectExpEditPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", id));
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("project", project));
            pairs.add(new BasicNameValuePair("start", start));
            pairs.add(new BasicNameValuePair("finish", finish));
            pairs.add(new BasicNameValuePair("desc", desc));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_update, "POST", pairs);

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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(ProjectExpEditPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
        }

    }

    class DeletePojTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProjectExpEditPage.this);
            dialog.setMessage("deleting...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id",id));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_delete, "POST", pairs);
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(ProjectExpEditPage.this, "删除成功！", Toast.LENGTH_SHORT).show();
        }

    }


}
