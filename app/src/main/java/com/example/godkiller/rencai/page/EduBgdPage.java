package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class EduBgdPage extends BaseActivity implements View.OnClickListener{
    private Button addEduBgdBtn;
    private ListView eduBgdLv;
    private Button backBtn;
    private SimpleAdapter eduAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_insert = "http://10.0.3.2:63342/htdocs/db/edu_bgd_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";
    private static final String TAG_COLLEGE = "college";
    private static final String TAG_ENROLL = "enroll";
    private static final String TAG_GRADUATE = "graduate";
    private static final String TAG_MAJOR = "major";
    private static final String TAG_DEGREE = "degree";
    private static final String TAG_ID = "id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edu_background_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        addEduBgdBtn = (Button) findViewById(R.id.add_edu_bgd_btn);
        backBtn = (Button) findViewById(R.id.back_button_edu_bgd);
        eduBgdLv = (ListView) findViewById(R.id.edu_bgd_lv);
        backBtn.setOnClickListener(this);
        addEduBgdBtn.setOnClickListener(this);
        eduBgdLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cid = ((TextView)view.findViewById(R.id.id_no_edu_bgd)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), EduBgdEditPage.class);
                intent.putExtra("id", cid);
                startActivityForResult(intent, 100);
            }
        });
        new LoadEduBgd().execute();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_edu_bgd:
                finish();
                break;
            case R.id.add_edu_bgd_btn:
                Intent intent = new Intent(getApplicationContext(), EduBgdAddPage.class);
                startActivityForResult(intent, 100);
                break;
            default:
                break;
        }
    }

    class LoadEduBgd extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EduBgdPage.this);
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
            dataList = new ArrayList<Map<String, Object>>();

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray eduObj = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i<eduObj.length(); i++) {
                        JSONObject info = eduObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString(TAG_ID));
                        infoMap.put("college", info.getString(TAG_COLLEGE));
                        infoMap.put("enroll", info.getString(TAG_ENROLL));
                        infoMap.put("graduate", info.getString(TAG_GRADUATE));
                        infoMap.put("major", info.getString(TAG_MAJOR));
                        infoMap.put("degree", info.getString(TAG_DEGREE));
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
            eduAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.edu_bgd_item, new String[]{"enroll", "graduate", "college", "degree", "major","id"},
                    new int[]{R.id.enroll_item_view, R.id.graduate_item_view, R.id.college_item_view, R.id.degree_item_view, R.id.major_item_view, R.id.id_no_edu_bgd});
            eduBgdLv.setAdapter(eduAdapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
