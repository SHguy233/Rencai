package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/5/6.
 */
public class OfferDetailPage extends BaseActivity {
    private TextView positionView;
    private TextView salaryView;
    private TextView companyView;
    private TextView cityView;
    private TextView expView;
    private TextView degreeView;
    private TextView numView;
    private TextView descView;
    private Button assessBtn;
    private Button backBtn;

    private TextView ccompanyView;
    private TextView ctradeView;
    private TextView cnatureView;
    private TextView cscaleView;
    private TextView caddressView;
    private TextView cdescView;
    private TextView cidView;
    private String company;

    private RatingBar ratingBar;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_positionDetails = "http://10.0.3.2:63342/htdocs/db/seeker_position_details.php";
    private static  String url_companyDetails = "http://10.0.3.2:63342/htdocs/db/seeker_company_details.php";


    private static final String TAG_SUCCESS = "success";
    private String username;
    private String id;
    private JSONObject posObj;
    private JSONObject comObj;
    private Button detailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.interview_detail_page);

        id = getIntent().getStringExtra("id");
        assessBtn = (Button) findViewById(R.id.assess_btn);
        detailBtn = (Button) findViewById(R.id.interview_detail_btn);
        positionView = (TextView) findViewById(R.id.position_item_interview_fra);
        salaryView = (TextView) findViewById(R.id.salary_item_interview_fra);
        companyView = (TextView) findViewById(R.id.company_item_interview_fra);
        cityView = (TextView) findViewById(R.id.work_city_item_interview_fra);
        expView = (TextView) findViewById(R.id.workexp_item_interview_fra);
        degreeView = (TextView) findViewById(R.id.degree_item_interview_fra);
        numView = (TextView) findViewById(R.id.num_item_interview_fra);
        descView = (TextView) findViewById(R.id.desc_interview_fra);

        company = getIntent().getStringExtra("company");
        ccompanyView = (TextView) findViewById(R.id.company_interview_fra);
        ctradeView = (TextView) findViewById(R.id.interview_trade_text_fra);
        cscaleView = (TextView) findViewById(R.id.interview_scale_text_fra);
        caddressView = (TextView) findViewById(R.id.interview_address_text_fra);
        cdescView = (TextView) findViewById(R.id.interview_company_desc_text_fra);
        cnatureView = (TextView) findViewById(R.id.interview_nature_text_fra);
        cidView = (TextView) findViewById(R.id.id_no_company_interview);

        backBtn = (Button) findViewById(R.id.back_button_interview);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        assessBtn = (Button) findViewById(R.id.assess_btn);
        assessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfferDetailPage.this, CompanyAssessmentPage.class);
                intent.putExtra("companyId", cidView.getText().toString());
                startActivity(intent);
            }
        });
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfferDetailPage.this, OfferDetailInfoPage.class);
                intent.putExtra("offerId", getIntent().getStringExtra("offerId"));
                startActivity(intent);
            }
        });
        ratingBar = (RatingBar) findViewById(R.id.ratingbar_interview);
        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.1f);
        new GetSeekerPositionTask().execute();
        new GetSeekerCompanyTask().execute();

    }

    class GetSeekerCompanyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OfferDetailPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                Log.d("company obj", "do in back");
                pairs.add(new BasicNameValuePair("company", company));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_companyDetails, "GET", pairs);
                JSONArray comAry = jsonObject.getJSONArray("info");
                comObj = comAry.getJSONObject(0);
                Log.d("pos obj", comObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ctradeView.setText(comObj.getString("trade"));
                        cscaleView.setText(comObj.getString("scale"));
                        ccompanyView.setText(company);
                        caddressView.setText(comObj.getString("address"));
                        cdescView.setText(comObj.getString("desc"));
                        cnatureView.setText(comObj.getString("nature"));
                        cidView.setText(comObj.getString("id"));
                        String score = comObj.getString("score");
                        float rate = Integer.valueOf(score) / 10;
                        ratingBar.setRating(rate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }

    }

    class GetSeekerPositionTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                Log.d("pos obj", "do in back");
                pairs.add(new BasicNameValuePair("id", id));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_positionDetails, "GET", pairs);
                JSONArray posAry = jsonObject.getJSONArray("info");
                posObj = posAry.getJSONObject(0);
                Log.d("pos obj", posObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        positionView.setText(posObj.getString("position"));
                        salaryView.setText(posObj.getString("salary"));
                        companyView.setText(posObj.getString("company"));
                        cityView.setText(posObj.getString("city"));
                        expView.setText(posObj.getString("workexp"));
                        degreeView.setText(posObj.getString("degree"));
                        numView.setText(posObj.getString("num"));
                        descView.setText(posObj.getString("condition"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }

    }
}
