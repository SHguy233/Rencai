package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class CompanyDetailPage extends BaseActivity implements View.OnClickListener{

    private Button backBtn;
    private Button passBtn;
    private long exitTime = 0;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private String cid;
    private static  String url_view = "http://10.0.3.2:63342/htdocs/db/company_info_commit_details.php";
    private static  String url_update = "http://10.0.3.2:63342/htdocs/db/company_info_commit_update.php";
    private JSONObject comObj;
    private TextView tradeView;
    private TextView natureView;
    private TextView addressView;
    private TextView businessView;
    private TextView companyView;
    private TextView scaleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_company_info_page);
        backBtn = (Button) findViewById(R.id.back_button_company_info_admin);
        passBtn = (Button) findViewById(R.id.pass_commit_btn);
        backBtn.setOnClickListener(this);
        passBtn.setOnClickListener(this);
        cid = getIntent().getStringExtra("companyid");
        tradeView = (TextView) findViewById(R.id.admin_trade_text);
        natureView = (TextView) findViewById(R.id.admin_nature_text);
        addressView = (TextView) findViewById(R.id.admin_address_text);
        businessView = (TextView) findViewById(R.id.admin_company_desc_text);
        scaleView = (TextView) findViewById(R.id.admin_scale_text_);
        companyView = (TextView) findViewById(R.id.admin_company_text);
        new GetAdminCompanyDetail().execute();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_company_info_admin:
                finish();
                break;
            case R.id.pass_commit_btn:
                new PassCommitTask().execute();
                break;
        }
    }

    class GetAdminCompanyDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CompanyDetailPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("id", cid));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray comAry = jsonObject.getJSONArray("info");
                    comObj = comAry.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                companyView.setText(comObj.getString("company"));
                                tradeView.setText(comObj.getString("trade"));
                                scaleView.setText(comObj.getString("scales"));
                                addressView.setText(comObj.getString("address"));
                                businessView.setText(comObj.getString("business"));
                                natureView.setText(comObj.getString("nature"));
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }

    }

    class PassCommitTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CompanyDetailPage.this);
            dialog.setMessage("committing...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("id", cid));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_update, "POST", pairs);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    Intent intent =getIntent();
                    setResult(900, intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(CompanyDetailPage.this, "提交成功！", Toast.LENGTH_SHORT).show();
        }

    }

}
