package com.example.godkiller.rencai.page;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class ResumePreviewPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
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
    private List<Map<String, Object>> infoList;
    private List<Map<String, Object>> eduList;
    private List<Map<String, Object>> workList;
    private List<Map<String, Object>> projectList;
    private List<Map<String, Object>> intentionList;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.resume_preview_page);

        personInfoLv = (ListView) findViewById(R.id.personal_info_preview_lv);
        eduLv = (ListView) findViewById(R.id.edu_bgd_preview_lv);
        workInfoLv = (ListView) findViewById(R.id.work_exp_preview_lv);
        projectLv = (ListView) findViewById(R.id.project_exp_preview_lv);
        intentionLv = (ListView) findViewById(R.id.job_intention_preview_lv);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_rp);
        backBtn.setOnClickListener(this);
        setInfoAdapter(this);
        setIntentionAdapter(this);
        setEduAdapter(this);
        setWorkAdapter(this);
        setProjectAdapter(this);
    }

    private void setInfoAdapter(Context context) {
        infoList = getInfoData();
        infoAdapter = new SimpleAdapter(context, infoList, R.layout.personal_info_pre_item,
                new String[]{"name", "gender", "birth", "workexptime", "acceptRemote", "phone"},
                new int[]{R.id.name_view_pre, R.id.gender_view_pre, R.id.birth_view_pre, R.id.workexp_view_pre,R.id.remote_view_pre, R.id.phone_view_pre});
        personInfoLv.setAdapter(infoAdapter);
    }

    private void setWorkAdapter(Context context) {
        workList = getWorkData();
        workAdapter = new SimpleAdapter(context, workList, R.layout.work_exp_pre_item,
                new String[]{"company", "trade", "position", "entry", "leave","desc"},
                new int[]{R.id.company_view_pre, R.id.trade_view_pre, R.id.position_view_pre, R.id.entry_view_pre,R.id.leave_view_pre,R.id.work_desc_view_pre});
        workInfoLv.setAdapter(workAdapter);
    }

    private void setEduAdapter(Context context) {
        eduList = getEduData();
        eduAdapter = new SimpleAdapter(context, eduList, R.layout.edu_bgd_pre_item,
                new String[]{"college", "major", "degree", "enroll", "graduate"},
                new int[]{R.id.college_view_pre, R.id.major_view_pre, R.id.degree_view_pre, R.id.enroll_view_pre,R.id.graduate_view_pre});
        eduLv.setAdapter(eduAdapter);
    }

    private void setProjectAdapter(Context context) {
        projectList = getProjectData();
        projectAdapter = new SimpleAdapter(context, projectList, R.layout.project_exp_pre_item,
                new String[]{"project", "start", "finish", "desc"},
                new int[]{R.id.project_view_pre, R.id.start_view_pre, R.id.finish_view_pre, R.id.project_desc_view_pre});
        projectLv.setAdapter(projectAdapter);
    }

    private void setIntentionAdapter(Context context) {
        intentionList = getIntentionData();
        intentionAdapter = new SimpleAdapter(context, intentionList, R.layout.job_intention_pre_item,
                new String[]{"workplace", "salary", "positioncategory", "tradecategory"},
                new int[]{R.id.workplace_view_pre, R.id.salary_view_pre, R.id.positioncategory_view_pre, R.id.tradecategory_view_pre});
        intentionLv.setAdapter(intentionAdapter);
    }

    private List<Map<String, Object>> getInfoData() {
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from personalinfo where username='" + username + "'";
        if (exits("personalinfo"))
        {
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String gender = cursor.getString(cursor.getColumnIndex("gender"));
                String birth = cursor.getString(cursor.getColumnIndex("birth"));
                String remote = cursor.getString(cursor.getColumnIndex("acceptRemote"));
                String workexp = cursor.getString(cursor.getColumnIndex("workexptime"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));

                Map<String, Object> infoMap = new HashMap<String, Object>();
                infoMap.put("name", name);
                infoMap.put("gender", gender);
                infoMap.put("birth", birth);
                infoMap.put("workexptime", workexp);
                infoMap.put("acceptRemote", remote);
                infoMap.put("phone", phone);
                infoList.add(infoMap);

            }
        }
        return infoList;

    }

    private List<Map<String, Object>> getEduData() {
        List<Map<String, Object>> eduList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from edubgd where username='" + username + "'";
        if (exits("edubgd"))
        {
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String college = cursor.getString(cursor.getColumnIndex("college"));
                String major = cursor.getString(cursor.getColumnIndex("major"));
                String degree = cursor.getString(cursor.getColumnIndex("degree"));
                String enroll = cursor.getString(cursor.getColumnIndex("enroll"));
                String graduate = cursor.getString(cursor.getColumnIndex("graduate"));

                Map<String, Object> eduMap = new HashMap<String, Object>();
                eduMap.put("college", college);
                eduMap.put("major", major);
                eduMap.put("degree", degree);
                eduMap.put("enroll", enroll);
                eduMap.put("graduate", graduate);
                eduList.add(eduMap);

            }
        }
        return eduList;

    }

    private List<Map<String, Object>> getProjectData() {
        List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from projectexp where username='" + username + "'";
        if (exits("projectexp"))
        {
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String project = cursor.getString(cursor.getColumnIndex("project"));
                String start = cursor.getString(cursor.getColumnIndex("start"));
                String finish = cursor.getString(cursor.getColumnIndex("finish"));
                String projectDesc = cursor.getString(cursor.getColumnIndex("desc"));

                Map<String, Object> projectMap = new HashMap<String, Object>();
                projectMap.put("project", project);
                projectMap.put("start", start);
                projectMap.put("finish", finish);
                projectMap.put("desc", projectDesc);
                projectList.add(projectMap);

            }
        }
        return projectList;

    }

    private List<Map<String, Object>> getIntentionData() {
        List<Map<String, Object>> intentionList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from jobintention where username='" + username + "'";
        if (exits("jobintention"))
        {
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String workplace = cursor.getString(cursor.getColumnIndex("workplace"));
                String salary = cursor.getString(cursor.getColumnIndex("salary"));
                String position = cursor.getString(cursor.getColumnIndex("positioncategory"));
                String trade = cursor.getString(cursor.getColumnIndex("tradecategory"));

                Map<String, Object> intentionMap = new HashMap<String, Object>();
                intentionMap.put("workplace", workplace);
                intentionMap.put("salary", salary);
                intentionMap.put("positioncategory", position);
                intentionMap.put("tradecategory", trade);
                intentionList.add(intentionMap);

            }
        }
        return intentionList;

    }

    private List<Map<String, Object>> getWorkData() {
        List<Map<String, Object>> workList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from workexp where username='" + username + "'";
        if (exits("workexp"))
        {
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String company = cursor.getString(cursor.getColumnIndex("company"));
                String trade = cursor.getString(cursor.getColumnIndex("trade"));
                String position = cursor.getString(cursor.getColumnIndex("position"));
                String entry = cursor.getString(cursor.getColumnIndex("entry"));
                String leave = cursor.getString(cursor.getColumnIndex("leave"));
                String workDesc = cursor.getString(cursor.getColumnIndex("desc"));

                Map<String, Object> workMap = new HashMap<String, Object>();
                workMap.put("company", company);
                workMap.put("trade", trade);
                workMap.put("position", position);
                workMap.put("entry", entry);
                workMap.put("leave", leave);
                workMap.put("desc", workDesc);
                workList.add(workMap);

            }
        }
        return workList;

    }


    public boolean exits(String table){
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        boolean exits = false;
        String sql = "select * from sqlite_master where name="+"'"+table+"'";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount()!=0){
            exits = true;
        }
        return exits;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_rp:
                finish();
                break;
            default:
                break;
        }
    }



}
