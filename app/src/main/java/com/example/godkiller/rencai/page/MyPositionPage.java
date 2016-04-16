package com.example.godkiller.rencai.page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPositionPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button addBtn;
    private ListView positionLv;
    private int index;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_position_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_mp);
        addBtn = (Button) findViewById(R.id.add_my_position_btn);
        positionLv = (ListView) findViewById(R.id.my_position_lv);

        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        setAdapter(this);

        positionLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                deleteDialog();
                return false;
            }
        });


    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPositionPage.this);
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = new DatabaseHelper(MyPositionPage.this).getReadableDatabase();
                String company = dataList.get(index).get("company").toString();
                String position = dataList.get(index).get("position").toString();
                String sql =  "select * from positioninfo where username='" + username + "'";
                //查询表中所有符合条件的数据
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    String mCompany = cursor.getString(cursor.getColumnIndex("company"));
                    String mPosition = cursor.getString(cursor.getColumnIndex("position"));
                    //若入学时间和毕业时间均符合，删除该行数据
                    if (company.equals(mCompany) && position.equals(mPosition)) {
                        //cursor.getInt(0)为符合条件的ID
                        db.execSQL("delete from positioninfo where id=" + Integer.toString(cursor.getInt(0)));
                    }
                }
                setAdapter(MyPositionPage.this);
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
        positionAdapter = new SimpleAdapter(context, dataList, R.layout.position_item, new String[]{"position", "salary", "company", "city", "workexp", "degree", "num", "condition"},
                new int[]{R.id.position_item_position, R.id.salary_item_position, R.id.company_item_view, R.id.work_city_item_position, R.id.workexp_item_position, R.id.degree_item_position, R.id.num_item_position, R.id.desc_position});
        positionLv.setAdapter(positionAdapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> positionList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from positioninfo where username='" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            String position = cursor.getString(cursor.getColumnIndex("position"));
            int salary = cursor.getInt(cursor.getColumnIndex("salary"));
            String company = cursor.getString(cursor.getColumnIndex("company"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String workexp = cursor.getString(cursor.getColumnIndex("workexp"));
            String degree = cursor.getString(cursor.getColumnIndex("degree"));
            int num = cursor.getInt(cursor.getColumnIndex("num"));
            String condition = cursor.getString(cursor.getColumnIndex("condition"));
            Map<String, Object> positionMap = new HashMap<String, Object>();
            positionMap.put("position", position);
            positionMap.put("salary", salary);
            positionMap.put("company", company);
            positionMap.put("city", city);
            positionMap.put("workexp", workexp);
            positionMap.put("degree", degree);
            positionMap.put("num", num);
            positionMap.put("condition", condition);
            positionList.add(positionMap);
        }
        return positionList;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_mp:
                finish();
                break;
            case R.id.add_my_position_btn:
                Intent intent = new Intent(MyPositionPage.this, AddPositionPage.class);
                startActivity(intent);
                break;
    }
}



}
