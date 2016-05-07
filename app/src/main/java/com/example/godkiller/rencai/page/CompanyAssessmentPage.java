package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

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
public class CompanyAssessmentPage extends BaseActivity implements RatingBar.OnRatingBarChangeListener,View.OnClickListener{
    private Button backBtn;
    private Button commitBtn;
    private RatingBar ratingBarStrength;
    private RatingBar ratingBarPower;
    private RatingBar ratingBarRegualr;
    private RatingBar ratingBarTotal;
    private float strengthScore;
    private float powerScore;
    private float regularScore;
    private float totalScore;
    private float sum;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_score = "http://10.0.3.2:63342/htdocs/db/seeker_company_assessment.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private String companyId;
    private JSONObject comObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.star_assessment_page);
        companyId = getIntent().getStringExtra("companyId");

        backBtn = (Button) findViewById(R.id.back_button_ca);
        commitBtn = (Button) findViewById(R.id.commit_button);
        backBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        ratingBarStrength = (RatingBar) findViewById(R.id.ratingbar_company_strength);
        ratingBarPower = (RatingBar) findViewById(R.id.ratingbar_company_power);
        ratingBarRegualr = (RatingBar) findViewById(R.id.ratingbar_company_regular);
        ratingBarTotal = (RatingBar) findViewById(R.id.ratingbar_company_total);
        ratingBarTotal.setOnRatingBarChangeListener(this);
        ratingBarPower.setOnRatingBarChangeListener(this);
        ratingBarRegualr.setOnRatingBarChangeListener(this);
        ratingBarStrength.setOnRatingBarChangeListener(this);


    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()) {
            case R.id.ratingbar_company_strength:
                strengthScore = rating * 10;
                break;
            case R.id.ratingbar_company_power:
                powerScore = rating * 10;
                break;
            case R.id.ratingbar_company_regular:
                regularScore = rating * 10;
                break;
            case R.id.ratingbar_company_total:
                totalScore = rating * 10;
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_ca:
                finish();
                break;
            case R.id.commit_button:
                sum = (strengthScore + powerScore + regularScore + totalScore)/4;
                Toast.makeText(CompanyAssessmentPage.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();
                new CommitScoreTask().execute();
                break;
        }
    }

    class CommitScoreTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CompanyAssessmentPage.this);
            dialog.setMessage("committing...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                Log.d("company obj", "do in back");
                pairs.add(new BasicNameValuePair("id", companyId));
                pairs.add(new BasicNameValuePair("score", String.valueOf(sum)));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_score, "POST", pairs);
                JSONArray comAry = jsonObject.getJSONArray("info");
                comObj = comAry.getJSONObject(0);
                Log.d("pos obj", comObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(CompanyAssessmentPage.this, "评价成功！", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
