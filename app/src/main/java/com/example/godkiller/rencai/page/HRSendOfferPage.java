package com.example.godkiller.rencai.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HRSendOfferPage extends BaseActivity implements View.OnClickListener {
    private Button backBtn;
    private Button sendBtn;
    private EditText hrnameText;
    private EditText addressText;
    private EditText phoneText;
    private EditText dateText;
    private EditText remarkText;
    private String cid;
    private String username;
    private String seekerUsername;
    private String hrname;
    private String address;
    private String phone;
    private String date;
    private String remark;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_send = "http://10.0.3.2:63342/htdocs/db/hr_send_offer.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hr_offer_detail_page);

        hrnameText = (EditText) findViewById(R.id.offer_item_hrname_hr);
        addressText = (EditText) findViewById(R.id.offer_item_address_hr);
        phoneText = (EditText) findViewById(R.id.offer_item_phone_hr);
        remarkText = (EditText) findViewById(R.id.offer_item_remark_hr);
        dateText = (EditText) findViewById(R.id.offer_item_date_hr);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        cid = getIntent().getStringExtra("positionId");
        seekerUsername = getIntent().getStringExtra("seekerUsername");
        sendBtn = (Button) findViewById(R.id.send_offer_btn);
        backBtn = (Button) findViewById(R.id.back_button_hr_offer_detail);
        backBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_hr_offer_detail:
                finish();
                break;
            case R.id.send_offer_btn:
                hrname = hrnameText.getText().toString();
                address = addressText.getText().toString();
                phone = phoneText.getText().toString();
                remark = remarkText.getText().toString();
                date = dateText.getText().toString();
                new SendofferTask().execute();
                break;

        }
    }

    class SendofferTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HRSendOfferPage.this);
            dialog.setMessage("sending...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("seekerUsername", seekerUsername));
            pairs.add(new BasicNameValuePair("positionId", cid));
            pairs.add(new BasicNameValuePair("address", address));
            pairs.add(new BasicNameValuePair("date", date));
            pairs.add(new BasicNameValuePair("hrname", hrname));
            pairs.add(new BasicNameValuePair("remark", remark));
            pairs.add(new BasicNameValuePair("phone", phone));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_send, "POST", pairs);
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent intent =getIntent();
                    setResult(111, intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(HRSendOfferPage.this, "发送成功", Toast.LENGTH_SHORT).show();
        }
    }


}