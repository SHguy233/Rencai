package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode;
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
import com.example.godkiller.rencai.db.WorkExp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class WorkingExpPage extends BaseActivity implements View.OnClickListener{
    private Button addWorkExpBtn;
    private ListView workExpLv;
    private Button backBtn;
    private int index;
    private SimpleAdapter workExpAdapter;
    private List<Map<String, Object>> dataList;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.working_exp_page);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        workExpLv = (ListView) findViewById(R.id.working_exp_lv);
        addWorkExpBtn = (Button) findViewById(R.id.add_working_exp_btn);
        backBtn = (Button) findViewById(R.id.back_button_work_exp);
        backBtn.setOnClickListener(this);
        addWorkExpBtn.setOnClickListener(this);

        setAdapter(this);
        workExpLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                deleteDialog();
                return false;
            }
        });
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkingExpPage.this);
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = new DatabaseHelper(WorkingExpPage.this).getReadableDatabase();
                String entry = dataList.get(index).get("entry").toString();
                String leave = dataList.get(index).get("leave").toString();
                String sql = "select * from workexp where username='" + username + "'";
                //查询表中所有符合条件的数据
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    String mEntry = cursor.getString(cursor.getColumnIndex("entry"));
                    String mLeave = cursor.getString(cursor.getColumnIndex("leave"));
                    //若入学时间和毕业时间均符合，删除该行数据
                    if (entry.equals(mEntry) && leave.equals(mLeave)) {
                        //cursor.getInt(0)为符合条件的ID
                        db.execSQL("delete from workexp where id=" + Integer.toString(cursor.getInt(0)));
                    }
                }
                setAdapter(WorkingExpPage.this);
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
        workExpAdapter = new SimpleAdapter(context, dataList, R.layout.work_exp_item, new String[]{"entry", "leave", "company", "trade", "position", "desc"},
                new int[]{R.id.entry_item_view, R.id.leave_item_view, R.id.company_item_view,R.id.trade_item_view, R.id.position_item_view, R.id.desc_item_view});
        workExpLv.setAdapter(workExpAdapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> expList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from workexp where username='" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            String company = cursor.getString(cursor.getColumnIndex("company"));
            String entry = cursor.getString(cursor.getColumnIndex("entry"));
            String leave = cursor.getString(cursor.getColumnIndex("leave"));
            String trade = cursor.getString(cursor.getColumnIndex("trade"));
            String position =cursor.getString(cursor.getColumnIndex("position"));
            String desc = cursor.getString(cursor.getColumnIndex("desc"));
            Map<String, Object> expMap = new HashMap<String, Object>();
            expMap.put("entry", entry);
            expMap.put("leave", leave);
            expMap.put("company", company);
            expMap.put("trade", trade);
            expMap.put("position", position);
            expMap.put("desc", desc);
            expList.add(expMap);
        }
        return expList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_work_exp:
                finish();
                break;
            case R.id.add_working_exp_btn:
                Intent intent = new Intent(WorkingExpPage.this, WorkExpEditPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
