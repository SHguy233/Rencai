package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.EduBgd;
import com.example.godkiller.rencai.db.EdubgdService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class EduBgdPage extends BaseActivity implements View.OnClickListener{
    private Button addEduBgdBtn;
    private ListView eduBgdLv;
    private Button backBtn;
    private List<EduBgd> eduBgdList;
    private int index;
    private SimpleAdapter eduAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edu_background_page);
//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//        String sql =  "select * from edubgd";
//        Cursor cursor = db.rawQuery(sql, null);
//        cursor.moveToNext();
//        String college = cursor.getString(cursor.getColumnIndex("enroll"));
//        cursor.close();
//        db.close();
        //Toast.makeText(EduBgdPage.this, college, Toast.LENGTH_SHORT).show();
        addEduBgdBtn = (Button) findViewById(R.id.add_edu_bgd_btn);
        backBtn = (Button) findViewById(R.id.back_button_edu_bgd);
        eduBgdLv = (ListView) findViewById(R.id.edu_bgd_lv);
        backBtn.setOnClickListener(this);
        addEduBgdBtn.setOnClickListener(this);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("enroll", "2012");
        map.put("graduate", "2013");
        map.put("college", "NJUST");
        map.put("major", "SE");
        map.put("degree", "bachelor");
        mapList.add(map);
        setAdapter(this);


        eduBgdLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position + 1;
                deleteDialog();
                return false;
            }
        });
    }
    private void setAdapter(Context context) {
        eduAdapter = new SimpleAdapter(context, getData(), R.layout.edu_bgd_item, new String[]{"enroll", "graduate", "college", "degree", "major"},
                new int[]{R.id.enroll_item_view, R.id.graduate_item_view, R.id.college_item_view, R.id.degree_item_view, R.id.major_item_view});
        eduBgdLv.setAdapter(eduAdapter);
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EduBgdPage.this);
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = new DatabaseHelper(EduBgdPage.this).getReadableDatabase();
                String[] id = {String.valueOf(3)};
                db.delete("edubgd", "id=?", id);
                setAdapter(EduBgdPage.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_edu_bgd:
                finish();
                break;
            case R.id.add_edu_bgd_btn:
                Intent intent = new Intent(EduBgdPage.this, EduBgdEditPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> eduList = new ArrayList<Map<String, Object>>();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from edubgd where username='" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            String college = cursor.getString(cursor.getColumnIndex("college"));
            String enroll = cursor.getString(cursor.getColumnIndex("enroll"));
            String graduate = cursor.getString(cursor.getColumnIndex("graduate"));
            String major = cursor.getString(cursor.getColumnIndex("major"));
            String degree =cursor.getString(cursor.getColumnIndex("degree"));
            Map<String, Object> eduMap = new HashMap<String, Object>();
            eduMap.put("enroll", enroll);
            eduMap.put("graduate", graduate);
            eduMap.put("college", college);
            eduMap.put("major", major);
            eduMap.put("degree", degree);
            eduList.add(eduMap);
        }
        return eduList;
    }
}
