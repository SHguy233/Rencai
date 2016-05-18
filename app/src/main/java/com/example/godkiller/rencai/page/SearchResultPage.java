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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SearchResultPage extends BaseActivity implements View.OnClickListener {
    private RadioGroup orderGroup;
    private RadioButton attentionBtn;
    private RadioButton popularityBtn;
    private RadioButton numBtn;
    private RadioButton assessmentBtn;
    private RadioButton defaultBtn;
    private Button backBtn;
    private ListView positionLv;
    private String cid;
    private String company;
    private String salary;
    private String trade;
    private String position;
    private String keywords;
    private String city;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private String order;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/search_position_view.php";
    private static String url_order = "http://10.0.3.2:63342/htdocs/db/search_position_order.php";
    private static String url_add = "http://10.0.3.2:63342/htdocs/db/seeker_position_attention.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_result_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        salary = getIntent().getStringExtra("salary");
        city = getIntent().getStringExtra("city");
        position = getIntent().getStringExtra("position");
        trade = getIntent().getStringExtra("trade");
        keywords = getIntent().getStringExtra("keywords");


        backBtn = (Button) findViewById(R.id.back_button_search_result);
        positionLv = (ListView) findViewById(R.id.position_lv_seeker_result);
        positionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_position_seeker)).getText().toString();
                company = ((TextView) view.findViewById(R.id.company_item_position_seeker)).getText().toString();
                new ResultAddAttentionTask().execute();
                Intent intent = new Intent(SearchResultPage.this, PositionDetailPage.class);
                intent.putExtra("id", cid);
                intent.putExtra("company", company);
                startActivity(intent);
            }
        });

        orderGroup = (RadioGroup) findViewById(R.id.order_group_result);
        attentionBtn = (RadioButton) findViewById(R.id.attention_btn_result);
        popularityBtn = (RadioButton) findViewById(R.id.popularity_btn_result);
        numBtn = (RadioButton) findViewById(R.id.demand_num_btn_result);
        defaultBtn = (RadioButton) findViewById(R.id.default_btn_result);
        assessmentBtn = (RadioButton) findViewById(R.id.assessment_btn_result);
        final int attId = attentionBtn.getId();
        final int popId = popularityBtn.getId();
        final int numId = numBtn.getId();
        final int assId = assessmentBtn.getId();
        final int deaId = defaultBtn.getId();

        orderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == attId) {
                    order = "attention";
                    new ResultOrderByAttention().execute();
                } else if (checkedId == popId) {
                    order = "popularity";
                    new ResultOrderByAttention().execute();
                } else if (checkedId == numId) {
                    order = "num";
                    new ResultOrderByAttention().execute();
                } else if (checkedId == assId) {
                    order = "assessment";
                    new ResultOrderByAttention().execute();
                } else if (checkedId == deaId) {
                    new LoadResultPosition().execute();
                }

            }
        });

        backBtn.setOnClickListener(this);

        new LoadResultPosition().execute();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_search_result:
                finish();
                break;
        }
    }

    class ResultOrderByAttention extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchResultPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("trade", trade));
            pairs.add(new BasicNameValuePair("position", position));
            pairs.add(new BasicNameValuePair("salary", salary));
            pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("keywords", keywords));
            pairs.add(new BasicNameValuePair("order", order));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_order, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray posObj = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < posObj.length(); i++) {
                        JSONObject info = posObj.getJSONObject(i);
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
            positionAdapter = new SimpleAdapter(SearchResultPage.this, dataList, R.layout.position_item_seeker, new String[]{"position", "salary", "company", "city", "degree", "num", "id"},
                    new int[]{R.id.position_item_position_seeker, R.id.salary_item_position_seeker, R.id.company_item_position_seeker, R.id.work_city_item_position_seeker, R.id.degree_item_position_seeker, R.id.num_item_position_seeker, R.id.id_no_position_seeker});
            positionLv.setAdapter(positionAdapter);
        }
    }

    class ResultAddAttentionTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchResultPage.this);
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


    class LoadResultPosition extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchResultPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            Log.d("trade", trade);
            pairs.add(new BasicNameValuePair("trade", trade));
            pairs.add(new BasicNameValuePair("position", position));
            pairs.add(new BasicNameValuePair("salary", salary));
            pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("keywords", keywords));
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
            positionAdapter = new SimpleAdapter(SearchResultPage.this, dataList, R.layout.position_item_seeker, new String[]{"position", "salary", "company", "city", "degree", "num", "id"},
                    new int[]{R.id.position_item_position_seeker, R.id.salary_item_position_seeker, R.id.company_item_position_seeker, R.id.work_city_item_position_seeker, R.id.degree_item_position_seeker, R.id.num_item_position_seeker, R.id.id_no_position_seeker});
            positionLv.setAdapter(positionAdapter);

        }
    }

}
