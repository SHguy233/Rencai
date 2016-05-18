package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
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
public class InterviewDetailInfoPage extends BaseActivity {
    private TextView hrnameView;
    private TextView dateView;
    private TextView addressView;
    private TextView phoneView;
    private TextView remarkView;
    private Button backBtn;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_interviewDetails = "http://10.0.3.2:63342/htdocs/db/seeker_interview_details.php";


    private static final String TAG_SUCCESS = "success";
    private String username;
    private String id;
    private JSONObject interviewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.interview_detail_info_page);

        id = getIntent().getStringExtra("interviewId");
        Toast.makeText(InterviewDetailInfoPage.this, id ,Toast.LENGTH_SHORT).show();
        hrnameView = (TextView) findViewById(R.id.interview_item_hrname);
        dateView = (TextView) findViewById(R.id.interview_item_date);
        addressView = (TextView) findViewById(R.id.interview_item_address);
        phoneView = (TextView) findViewById(R.id.interview_item_phone);
        remarkView = (TextView) findViewById(R.id.interview_item_remark);

        backBtn = (Button) findViewById(R.id.back_button_interview_info_detail);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new GetSeekerInterviewTask().execute();


    }


    class GetSeekerInterviewTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InterviewDetailInfoPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("id", id));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_interviewDetails, "GET", pairs);
                JSONArray interviewAry = jsonObject.getJSONArray("info");
                interviewObject = interviewAry.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                hrnameView.setText(interviewObject.getString("hrname"));
                                dateView.setText(interviewObject.getString("date"));
                                addressView.setText(interviewObject.getString("address"));
                                phoneView.setText(interviewObject.getString("phone"));
                                remarkView.setText(interviewObject.getString("remark"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

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
}
