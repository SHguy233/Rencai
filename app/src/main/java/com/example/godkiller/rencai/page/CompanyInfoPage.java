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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class CompanyInfoPage extends BaseActivity implements View.OnClickListener{
    private Button editCompanyInfoBtn;
    private ListView companyInfoLv;
    private Button backBtn;
    private int index;
    private SimpleAdapter companyInfoAdapter;
    private List<Map<String, Object>> dataList;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.company_info_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        editCompanyInfoBtn = (Button) findViewById(R.id.edit_company_info_btn);
        backBtn = (Button) findViewById(R.id.back_button_company_info);
        companyInfoLv = (ListView) findViewById(R.id.company_info_lv);
        backBtn.setOnClickListener(this);
        editCompanyInfoBtn.setOnClickListener(this);

        setAdapter(this);

    }

    private void setAdapter(Context context) {
        dataList = getData();
        companyInfoAdapter = new SimpleAdapter(context, dataList, R.layout.company_info_item, new String[]{"company", "trade", "nature", "scale", "business","address"},
                new int[]{R.id.company_name_ci_text, R.id.trade_ci_text, R.id.company_category_ci_text, R.id.scale_ci_text, R.id.business_desc_ci_text,R.id.company_address_ci_text});
        companyInfoLv.setAdapter(companyInfoAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_company_info:
                finish();
                break;
            case R.id.edit_company_info_btn:
                Intent intent = new Intent(CompanyInfoPage.this, CompanyInfoEditPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> companyInfoList = new ArrayList<Map<String, Object>>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql =  "select * from companyinfo where username='" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            String company = cursor.getString(cursor.getColumnIndex("company"));
            String trade = cursor.getString(cursor.getColumnIndex("trade"));
            String nature = cursor.getString(cursor.getColumnIndex("nature"));
            String scale = cursor.getString(cursor.getColumnIndex("scale"));
            String business =cursor.getString(cursor.getColumnIndex("business"));
            String address =cursor.getString(cursor.getColumnIndex("address"));
            Map<String, Object> companyInfoMap = new HashMap<String, Object>();
            companyInfoMap.put("company", company);
            companyInfoMap.put("trade", trade);
            companyInfoMap.put("nature", nature);
            companyInfoMap.put("scale", scale);
            companyInfoMap.put("business", business);
            companyInfoMap.put("address", address);
            companyInfoList.add(companyInfoMap);
        }
        return companyInfoList;
    }
}
