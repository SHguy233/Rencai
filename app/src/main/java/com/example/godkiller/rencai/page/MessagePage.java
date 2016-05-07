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
 * Created by GodKiller on 2016/5/7.
 */
public class MessagePage extends BaseActivity{
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/follow_position_view.php";
    private static String url_add = "http://10.0.3.2:63342/htdocs/db/seeker_position_attention.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";
    private Button backBtn;
    private ListView positionLv;
    private String cid;
    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        backBtn = (Button) findViewById(R.id.back_button_message);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        positionLv = (ListView) findViewById(R.id.position_lv_message);
        positionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_position_seeker)).getText().toString();
                company = ((TextView) view.findViewById(R.id.company_item_position_seeker)).getText().toString();
                new MessageAddAttentionTask().execute();
                Intent intent = new Intent(MessagePage.this, PositionDetailPage.class);
                intent.putExtra("id", cid);
                intent.putExtra("company", company);
                startActivity(intent);
            }
        });

        new LoadMessagePosition().execute();
    }

    class MessageAddAttentionTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MessagePage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", cid));
            Log.d("id", cid);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_add, "POST", pairs);
            Log.d("ADD ATTENTION", jsonObject.toString());
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }
    }

    class LoadMessagePosition extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MessagePage.this);
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
            positionAdapter = new SimpleAdapter(MessagePage.this, dataList, R.layout.position_item_seeker, new String[]{"position", "salary", "company", "city", "degree", "num", "id"},
                    new int[]{R.id.position_item_position_seeker, R.id.salary_item_position_seeker, R.id.company_item_position_seeker, R.id.work_city_item_position_seeker, R.id.degree_item_position_seeker, R.id.num_item_position_seeker, R.id.id_no_position_seeker});
            positionLv.setAdapter(positionAdapter);

        }
    }


}
