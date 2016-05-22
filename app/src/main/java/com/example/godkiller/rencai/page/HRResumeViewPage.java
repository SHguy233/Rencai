package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class HRResumeViewPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button sendBtn;
    private ListView personInfoLv;
    private ListView workInfoLv;
    private ListView eduLv;
    private ListView projectLv;
    private ListView intentionLv;
    private SimpleAdapter infoAdapter;
    private SimpleAdapter eduAdapter;
    private SimpleAdapter workAdapter;
    private SimpleAdapter projectAdapter;
    private SimpleAdapter intentionAdapter;
    private List<Map<String, Object>> personalList;
    private List<Map<String, Object>> eduList;
    private List<Map<String, Object>> workList;
    private List<Map<String, Object>> projectList;
    private List<Map<String, Object>> intentionList;
    private String username;
    private String positionId;
    private String seekerUsername;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/seeker_resume_preview.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hr_resume_view_page);

        personInfoLv = (ListView) findViewById(R.id.personal_info_preview_lv);
        eduLv = (ListView) findViewById(R.id.edu_bgd_preview_lv);
        workInfoLv = (ListView) findViewById(R.id.work_exp_preview_lv);
        projectLv = (ListView) findViewById(R.id.project_exp_preview_lv);
        intentionLv = (ListView) findViewById(R.id.job_intention_preview_lv);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        seekerUsername = getIntent().getStringExtra("seekerUsername");
        positionId = getIntent().getStringExtra("positionId");


        backBtn = (Button) findViewById(R.id.back_button_rv);
        backBtn.setOnClickListener(this);
        new LoadDataTask().execute();
    }

    class LoadDataTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HRResumeViewPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", seekerUsername));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            eduList = new ArrayList<Map<String, Object>>();
            projectList = new ArrayList<Map<String, Object>>();
            personalList = new ArrayList<Map<String, Object>>();
            workList = new ArrayList<Map<String, Object>>();
            intentionList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray eduArray = jsonObject.getJSONArray("eduInfo");
                    for (int i = 0; i < eduArray.length(); i++) {
                        JSONObject eduObj = eduArray.getJSONObject(i);
                        Map<String, Object> eduInfoMap = new HashMap<String, Object>();
                        eduInfoMap.put("id", eduObj.getString("id"));
                        eduInfoMap.put("college", eduObj.getString("college"));
                        eduInfoMap.put("enroll", eduObj.getString("enroll"));
                        eduInfoMap.put("graduate", eduObj.getString("graduate"));
                        eduInfoMap.put("major", eduObj.getString("major"));
                        eduInfoMap.put("degree", eduObj.getString("degree"));
                        eduList.add(eduInfoMap);
                    }
                    JSONArray personArray = jsonObject.getJSONArray("personalInfo");
                    JSONObject personObj = personArray.getJSONObject(0);
                    Map<String, Object> personInfoMap = new HashMap<String, Object>();
                    personInfoMap.put("name", personObj.getString("name"));
                    personInfoMap.put("gender", personObj.getString("gender"));
                    personInfoMap.put("birth", personObj.getString("birth"));
                    personInfoMap.put("workexptime", personObj.getString("workexptime"));
                    personInfoMap.put("remote", personObj.getString("remote"));
                    personInfoMap.put("phone", personObj.getString("phone"));
                    personalList.add(personInfoMap);

                    JSONArray projectArray = jsonObject.getJSONArray("projectInfo");
                    for (int i=0; i<projectArray.length(); i++) {
                        JSONObject pojObj = projectArray.getJSONObject(i);
                        Map<String, Object> pojInfoMap = new HashMap<String, Object>();
                        pojInfoMap.put("id", pojObj.getString("id"));
                        pojInfoMap.put("project", pojObj.getString("project"));
                        pojInfoMap.put("start", pojObj.getString("start"));
                        pojInfoMap.put("finish", pojObj.getString("finish"));
                        pojInfoMap.put("desc", pojObj.getString("desc"));
                        projectList.add(pojInfoMap);
                    }

                    JSONArray workArray = jsonObject.getJSONArray("workInfo");
                    for (int i=0; i<workArray.length(); i++) {
                        JSONObject workObj = workArray.getJSONObject(i);
                        Map<String, Object> workInfoMap = new HashMap<String, Object>();
                        workInfoMap.put("id", workObj.getString("id"));
                        workInfoMap.put("company", workObj.getString("company"));
                        workInfoMap.put("entry", workObj.getString("entry"));
                        workInfoMap.put("leave", workObj.getString("leave"));
                        workInfoMap.put("trade", workObj.getString("trade"));
                        workInfoMap.put("position", workObj.getString("position"));
                        workInfoMap.put("desc", workObj.getString("desc"));
                        workList.add(workInfoMap);
                    }

                    JSONArray intentionArray = jsonObject.getJSONArray("intentionInfo");
                    JSONObject intentionObject = intentionArray.getJSONObject(0);
                    Map<String, Object> intentionInfoMap = new HashMap<String, Object>();
                    intentionInfoMap.put("city", intentionObject.getString("city"));
                    intentionInfoMap.put("trade", intentionObject.getString("trade"));
                    intentionInfoMap.put("position", intentionObject.getString("position"));
                    intentionInfoMap.put("salary", intentionObject.getString("salary"));
                    intentionList.add(intentionInfoMap);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            setEduAdapter(HRResumeViewPage.this);
            setPersonAdapter(HRResumeViewPage.this);
            setWorkAdapter(HRResumeViewPage.this);
            setProjectAdapter(HRResumeViewPage.this);
            setIntentionAdapter(HRResumeViewPage.this);

        }
    }

    private void setPersonAdapter(Context context) {
        infoAdapter = new SimpleAdapter(context, personalList, R.layout.personal_info_pre_item,
                new String[]{"name", "gender", "birth", "workexptime", "remote", "phone"},
                new int[]{R.id.name_view_pre, R.id.gender_view_pre, R.id.birth_view_pre, R.id.workexp_view_pre,R.id.remote_view_pre, R.id.phone_view_pre});
        personInfoLv.setAdapter(infoAdapter);
    }

    private void setWorkAdapter(Context context) {
        workAdapter = new SimpleAdapter(context, workList, R.layout.work_exp_pre_item,
                new String[]{"company", "trade", "position", "entry", "leave","desc"},
                new int[]{R.id.company_view_pre, R.id.trade_view_pre, R.id.position_view_pre, R.id.entry_view_pre,R.id.leave_view_pre,R.id.work_desc_view_pre});
        workInfoLv.setAdapter(workAdapter);
    }

    private void setEduAdapter(Context context) {
        eduAdapter = new SimpleAdapter(context, eduList, R.layout.edu_bgd_pre_item,
                new String[]{"college", "major", "degree", "enroll", "graduate"},
                new int[]{R.id.college_view_pre, R.id.major_view_pre, R.id.degree_view_pre, R.id.enroll_view_pre,R.id.graduate_view_pre});
        eduLv.setAdapter(eduAdapter);
    }

    private void setProjectAdapter(Context context) {
        projectAdapter = new SimpleAdapter(context, projectList, R.layout.project_exp_pre_item,
                new String[]{"project", "start", "finish", "desc"},
                new int[]{R.id.project_view_pre, R.id.start_view_pre, R.id.finish_view_pre, R.id.project_desc_view_pre});
        projectLv.setAdapter(projectAdapter);
    }

    private void setIntentionAdapter(Context context) {
        intentionAdapter = new SimpleAdapter(context, intentionList, R.layout.job_intention_pre_item,
                new String[]{"city", "salary", "position", "trade"},
                new int[]{R.id.workplace_view_pre, R.id.salary_view_pre, R.id.positioncategory_view_pre, R.id.tradecategory_view_pre});
        intentionLv.setAdapter(intentionAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_rv:
                finish();
                break;
            default:
                break;
        }
    }




}
