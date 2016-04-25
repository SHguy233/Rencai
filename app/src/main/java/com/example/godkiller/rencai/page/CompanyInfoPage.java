package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.CompanyInfo;
import com.example.godkiller.rencai.db.DatabaseHelper;
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
public class CompanyInfoPage extends BaseActivity implements View.OnClickListener{
    private Button editCompanyInfoBtn;
    private ListView companyInfoLv;
    private Button backBtn;
    private int index;
    private SimpleAdapter companyInfoAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/company_info_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_INFO = "info";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_TRADE = "trade";
    private static final String TAG_NATURE = "nature";
    private static final String TAG_SCALE = "scales";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_BUSINESS = "business";



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
        new LoadCompanyInfo().execute();

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


    class LoadCompanyInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CompanyInfoPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insert, "GET", pairs);
            Log.d("company info", jsonObject.toString());

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray companyObj = jsonObject.getJSONArray(TAG_INFO);
                    JSONObject info = companyObj.getJSONObject(0);
                    Map<String, Object> infoMap = new HashMap<String, Object>();
                    infoMap.put("company", info.getString(TAG_COMPANY));
                    infoMap.put("trade", info.getString(TAG_TRADE));
                    infoMap.put("nature", info.getString(TAG_NATURE));
                    infoMap.put("scale", info.getString(TAG_SCALE));
                    infoMap.put("address", info.getString(TAG_ADDRESS));
                    infoMap.put("business", info.getString(TAG_BUSINESS));
                    dataList = new ArrayList<Map<String, Object>>();
                    dataList.add(infoMap);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            companyInfoAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.company_info_item, new String[]{"company", "trade", "nature", "scale", "business","address"},
                    new int[]{R.id.company_name_ci_text, R.id.trade_ci_text, R.id.company_category_ci_text, R.id.scale_ci_text, R.id.business_desc_ci_text,R.id.company_address_ci_text});
            companyInfoLv.setAdapter(companyInfoAdapter);

        }
    }
}
