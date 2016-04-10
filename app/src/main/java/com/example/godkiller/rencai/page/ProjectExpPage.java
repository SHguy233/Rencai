package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.ProjectExp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class ProjectExpPage extends BaseActivity implements View.OnClickListener{
    private Button addProExpBtn;
    private ListView proExpLv;
    private Button backBtn;
    private int index;
    private SimpleAdapter proExpAdapter;
    private List<Map<String, Object>> dataList;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.project_exp_page);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        proExpLv = (ListView) findViewById(R.id.project_exp_lv);
        addProExpBtn = (Button) findViewById(R.id.add_project_exp_btn);
        backBtn = (Button) findViewById(R.id.back_button_project_exp);
        backBtn.setOnClickListener(this);
        addProExpBtn.setOnClickListener(this);

        setAdapter(this);
        proExpLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                deleteDialog();
                return false;
            }
        });
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectExpPage.this);
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = new DatabaseHelper(ProjectExpPage.this).getReadableDatabase();
                String start = dataList.get(index).get("start").toString();
                String finish = dataList.get(index).get("finish").toString();
                String sql = "select * from projectexp where username='" + username + "'";
                //查询表中所有符合条件的数据
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    String mStart = cursor.getString(cursor.getColumnIndex("start"));
                    String mFinish = cursor.getString(cursor.getColumnIndex("finish"));
                    //若入学时间和毕业时间均符合，删除该行数据
                    if (start.equals(mStart) && finish.equals(mFinish)) {
                        //cursor.getInt(0)为符合条件的ID
                        db.execSQL("delete from projectexp where id=" + Integer.toString(cursor.getInt(0)));
                    }
                }
                setAdapter(ProjectExpPage.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void setAdapter(Context context) {
        dataList = getData();
        proExpAdapter = new SimpleAdapter(context, dataList, R.layout.project_exp_item, new String[]{"start", "finish", "project", "desc"},
                new int[]{R.id.start_item_view, R.id.finish_item_view, R.id.project_item_view,R.id.pro_desc_item_view});
        proExpLv.setAdapter(proExpAdapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> expList = new ArrayList<Map<String, Object>>();
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
                String desc = cursor.getString(cursor.getColumnIndex("desc"));

                Map<String, Object> expMap = new HashMap<String, Object>();
                expMap.put("start", start);
                expMap.put("finish", finish);
                expMap.put("project", project);
                expMap.put("desc", desc);
                expList.add(expMap);
            }
        }
        return expList;
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
            case R.id.back_button_project_exp:
                finish();
                break;
            case R.id.add_project_exp_btn:
                Intent intent = new Intent(ProjectExpPage.this, ProjectExpEditPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
