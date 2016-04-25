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
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.db.ProjectExp;

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
public class ProjectExpPage extends BaseActivity implements View.OnClickListener{
    private Button addProExpBtn;
    private ListView proExpLv;
    private Button backBtn;
    private SimpleAdapter proExpAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_view = "http://10.0.3.2:63342/htdocs/db/poj_exp_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";
    private static final String TAG_PROJECT = "project";
    private static final String TAG_START = "start";
    private static final String TAG_FINISH = "finish";
    private static final String TAG_DESC = "desc";
    private static final String TAG_ID = "id";

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
        proExpLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cid = ((TextView) view.findViewById(R.id.id_no_poj_exp)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), ProjectExpEditPage.class);
                intent.putExtra("id", cid);
                startActivityForResult(intent, 200);
            }
        });
        new LoadPojExp().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_project_exp:
                finish();
                break;
            case R.id.add_project_exp_btn:
                Intent intent = new Intent(getApplicationContext(), ProjectExpAddPage.class);
                startActivityForResult(intent, 200);
                break;
            default:
                break;
        }
    }

    class LoadPojExp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProjectExpPage.this);
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

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray eduObj = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i<eduObj.length(); i++) {
                        JSONObject info = eduObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString(TAG_ID));
                        infoMap.put("project", info.getString(TAG_PROJECT));
                        infoMap.put("start", info.getString(TAG_START));
                        infoMap.put("finish", info.getString(TAG_FINISH));
                        infoMap.put("desc", info.getString(TAG_DESC));
                        dataList.add(infoMap);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            proExpAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.project_exp_item, new String[]{"start", "finish", "project", "desc","id"},
                    new int[]{R.id.start_item_view, R.id.finish_item_view, R.id.project_item_view,R.id.pro_desc_item_view, R.id.id_no_poj_exp});
            proExpLv.setAdapter(proExpAdapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
