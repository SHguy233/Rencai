package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

public class CompanyPositionPage extends BaseActivity implements View.OnClickListener {
    private Button backBtn;
    private ListView positionLv;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private String cid;
    private String company;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/company_position_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.company_position_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        company = getIntent().getStringExtra("company");


        backBtn = (Button) findViewById(R.id.back_button_cp);
        positionLv = (ListView) findViewById(R.id.company_position_lv);
        positionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_position)).getText().toString();
                Intent intent = new Intent(CompanyPositionPage.this, PositionDetailPage.class);
                intent.putExtra("id", cid);
                intent.putExtra("company", company);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(this);

        new LoadCompanyPosition().execute();


    }



    class LoadCompanyPosition extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CompanyPositionPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("company", company));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray posAry = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < posAry.length(); i++) {
                        JSONObject info = posAry.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString("id"));
                        infoMap.put("company", info.getString("company"));
                        infoMap.put("city", info.getString("city"));
                        infoMap.put("position", info.getString("position"));
                        infoMap.put("num", info.getString("num"));
                        infoMap.put("salary", info.getString("salary"));
                        infoMap.put("degree", info.getString("degree"));
                        infoMap.put("workexp", info.getString("workexp"));
                        infoMap.put("condition", info.getString("condition"));
                        dataList.add(infoMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            positionAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.position_item, new String[]{"position", "salary", "company", "city", "workexp", "degree", "num", "condition", "id"},
                    new int[]{R.id.position_item_position, R.id.salary_item_position, R.id.company_item_view, R.id.work_city_item_position, R.id.workexp_item_position, R.id.degree_item_position, R.id.num_item_position, R.id.desc_position, R.id.id_no_position});
            positionLv.setAdapter(positionAdapter);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_cp:
                finish();
                break;
        }
    }

}
