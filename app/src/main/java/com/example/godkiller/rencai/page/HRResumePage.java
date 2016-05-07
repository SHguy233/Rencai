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

public class HRResumePage extends BaseActivity implements View.OnClickListener {
    private Button backBtn;
    private ListView positionLv;
    private String cid;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/hr_position_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_resume_page_position);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_mrp);
        positionLv = (ListView) findViewById(R.id.my_resume_position_lv);
        positionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_position_resume)).getText().toString();
                Intent intent = new Intent(HRResumePage.this, ResumeInterViewOfferPage.class);
                intent.putExtra("id", cid);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(this);

        new LoadHRPositionResume().execute();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_mrp:
                finish();
                break;
        }
    }

    class LoadHRPositionResume extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HRResumePage.this);
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
                        infoMap.put("position", info.getString("position"));
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
            positionAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.resume_position_item, new String[]{"position", "id"},
                    new int[]{R.id.position_item_resume, R.id.id_no_position_resume});
            positionLv.setAdapter(positionAdapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 400) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
