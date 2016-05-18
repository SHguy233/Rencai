package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
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

/**
 * Created by GodKiller on 2016/4/5.
 */
public class WorkExpPage extends BaseActivity implements View.OnClickListener {
    private Button addWorkExpBtn;
    private ListView workExpLv;
    private Button backBtn;
    private SimpleAdapter workExpAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/work_exp_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_ENTRY = "entry";
    private static final String TAG_LEAVE = "leave";
    private static final String TAG_TRADE = "trade";
    private static final String TAG_POSITION = "position";
    private static final String TAG_DESC = "desc";
    private static final String TAG_ID = "id";

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

        workExpLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cid = ((TextView) view.findViewById(R.id.id_no_work_exp)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), WorkExpEditPage.class);
                intent.putExtra("id", cid);
                startActivityForResult(intent, 300);
            }
        });
        new LoadWorkExp().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_work_exp:
                finish();
                break;
            case R.id.add_working_exp_btn:
                Intent intent = new Intent(getApplicationContext(), WorkExpAddPage.class);
                startActivityForResult(intent, 300);
                break;
            default:
                break;
        }
    }

    class LoadWorkExp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(WorkExpPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray workObj = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < workObj.length(); i++) {
                        JSONObject info = workObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString(TAG_ID));
                        infoMap.put("company", info.getString(TAG_COMPANY));
                        infoMap.put("entry", info.getString(TAG_ENTRY));
                        infoMap.put("leave", info.getString(TAG_LEAVE));
                        infoMap.put("trade", info.getString(TAG_TRADE));
                        infoMap.put("position", info.getString(TAG_POSITION));
                        infoMap.put("desc", info.getString(TAG_DESC));
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
            workExpAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.work_exp_item, new String[]{"entry", "leave", "company", "trade", "position", "desc", "id"},
                    new int[]{R.id.entry_item_view, R.id.leave_item_view, R.id.company_item_view, R.id.trade_item_view, R.id.position_item_view, R.id.desc_item_view, R.id.id_no_work_exp});
            workExpLv.setAdapter(workExpAdapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
