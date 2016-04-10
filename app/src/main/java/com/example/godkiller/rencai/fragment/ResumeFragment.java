package com.example.godkiller.rencai.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.page.EduBgdPage;
import com.example.godkiller.rencai.page.JobIntentionPage;
import com.example.godkiller.rencai.page.PersonalInfoPage;
import com.example.godkiller.rencai.page.ProjectExpPage;
import com.example.godkiller.rencai.page.ResumePreviewPage;
import com.example.godkiller.rencai.page.WorkingExpPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class ResumeFragment extends Fragment implements View.OnClickListener{

    private LinearLayout personalInfoLayout;
    private LinearLayout jobIntentionLayout;
    private LinearLayout eduBgdLayout;
    private LinearLayout workExpLayout;
    private LinearLayout projectExpLayout;
    private LinearLayout resumePreviewLayout;
    private ListView personalInfoLv;
    private SimpleAdapter infoAdapter;
    private List<Map<String, Object>> dataList;
    private String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resume_fragment, null);
        personalInfoLayout = (LinearLayout) view.findViewById(R.id.personal_info_layout);
        jobIntentionLayout = (LinearLayout) view.findViewById(R.id.job_intention_layout);
        eduBgdLayout = (LinearLayout) view.findViewById(R.id.edu_bgd_layout);
        workExpLayout = (LinearLayout) view.findViewById(R.id.working_exp_layout);
        projectExpLayout = (LinearLayout) view.findViewById(R.id.project_exp_layout);
        resumePreviewLayout = (LinearLayout) view.findViewById(R.id.resume_preview_layout);
        personalInfoLv = (ListView) view.findViewById(R.id.personal_info_lv);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        personalInfoLayout.setOnClickListener(this);
        jobIntentionLayout.setOnClickListener(this);
        eduBgdLayout.setOnClickListener(this);
        workExpLayout.setOnClickListener(this);
        projectExpLayout.setOnClickListener(this);
        resumePreviewLayout.setOnClickListener(this);
        setAdapter(getActivity());
        return view;
    }

    private void setAdapter(Context context) {
        dataList = getData();
        infoAdapter = new SimpleAdapter(context, dataList, R.layout.personal_info_item, new String[]{"name","gender","workexptime","phone"},
                new int[]{R.id.name_item_view, R.id.gender_item_view, R.id.workexp_item_view, R.id.phone_item_view});
        personalInfoLv.setAdapter(infoAdapter);

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from personalinfo where username='" + username + "'";
        if (exits("personalinfo"))
        {
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String gender = cursor.getString(cursor.getColumnIndex("gender"));
                String workexp = cursor.getString(cursor.getColumnIndex("workexptime"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));

                Map<String, Object> infoMap = new HashMap<String, Object>();
                infoMap.put("name", name);
                infoMap.put("gender", gender);
                infoMap.put("workexptime", workexp);
                infoMap.put("phone", phone);
                infoList.add(infoMap);

            }
        }
        return infoList;
    }

    public boolean exits(String table){
        SQLiteDatabase db = new DatabaseHelper(getActivity()).getReadableDatabase();
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
            case R.id.personal_info_layout:
                jumpIntent(getActivity(), PersonalInfoPage.class);
                break;
            case R.id.job_intention_layout:
                jumpIntent(getActivity(), JobIntentionPage.class);
                break;
            case R.id.edu_bgd_layout:
                jumpIntent(getActivity(), EduBgdPage.class);
                break;
            case R.id.working_exp_layout:
                jumpIntent(getActivity(), WorkingExpPage.class);
                break;
            case R.id.project_exp_layout:
                jumpIntent(getActivity(), ProjectExpPage.class);
                break;
            case R.id.resume_preview_layout:
                jumpIntent(getActivity(), ResumePreviewPage.class);
                break;
            default:
                break;
        }
    }

    public void jumpIntent(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        getActivity().startActivity(intent);
    }
}
