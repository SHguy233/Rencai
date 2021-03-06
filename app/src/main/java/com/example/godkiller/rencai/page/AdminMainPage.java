package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.ActivityCollector;
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
 * Created by GodKiller on 2016/3/6.
 */
public class AdminMainPage extends BaseActivity {

    private long exitTime = 0;
    private ListView companyLv;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private String username;
    private String cid;
    private static  String url_view = "http://10.0.3.2:63342/htdocs/db/company_info_commit_view.php";
    private SimpleAdapter companyAdapter;
    private List<Map<String, Object>> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_main_page);
        companyLv = (ListView) findViewById(R.id.admin_company_lv);
        companyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView)findViewById(R.id.id_no_admin_company)).getText().toString();
                Intent intent = new Intent(AdminMainPage.this, CompanyDetailPage.class);
                intent.putExtra("companyid", cid);
                startActivityForResult(intent, 900);
            }
        });
        new GetAdminCompanyTask().execute();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    class GetAdminCompanyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AdminMainPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
                dataList = new ArrayList<Map<String, Object>>();
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray comObj = jsonObject.getJSONArray("info");
                    for (int i = 0; i < comObj.length(); i++) {
                        JSONObject info = comObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString("id"));
                        infoMap.put("company", info.getString("company"));
                        infoMap.put("username", info.getString("username"));
                        dataList.add(infoMap);
                    }
                    result = "success";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("success")) {
                companyAdapter = new SimpleAdapter(AdminMainPage.this, dataList, R.layout.admin_company_item, new String[]{"id", "company", "username"},
                        new int[]{R.id.id_no_admin_company, R.id.admin_company_view, R.id.admin_username_view});
                companyLv.setAdapter(companyAdapter);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 900) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
