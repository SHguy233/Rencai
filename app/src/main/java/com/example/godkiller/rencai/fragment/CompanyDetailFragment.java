package com.example.godkiller.rencai.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class CompanyDetailFragment extends Fragment {
    private String id;
    private TextView companyView;
    private TextView tradeView;
    private TextView natureView;
    private TextView scaleView;
    private TextView addressView;
    private TextView descView;
    private TextView idView;
    private String username;
    private String company;
    private Button followBtn;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_details = "http://10.0.3.2:63342/htdocs/db/seeker_company_details.php";
    private static  String url_follow = "http://10.0.3.2:63342/htdocs/db/seeker_company_follow_save.php";
    private static final String TAG_SUCCESS = "success";
    private JSONObject comObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.company_detail_fragment, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        Intent intent = getActivity().getIntent();
        company = intent.getStringExtra("company");

        companyView = (TextView) view.findViewById(R.id.company_ci_fra);
        tradeView = (TextView) view.findViewById(R.id.trade_text_fra);
        scaleView = (TextView) view.findViewById(R.id.scale_text_fra);
        addressView = (TextView) view.findViewById(R.id.address_text_fra);
        descView = (TextView) view.findViewById(R.id.company_desc_text_fra);
        natureView = (TextView) view.findViewById(R.id.nature_text_fra);
        idView = (TextView) view.findViewById(R.id.id_no_company);
        followBtn = (Button) view.findViewById(R.id.follow_btn);
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idView.getText().toString();
                new FollowTask().execute();
            }
        });
        new GetSeekerCompanyTask().execute();
        return view;
    }


    class GetSeekerCompanyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
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
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_details, "GET", pairs);
                JSONArray comAry = jsonObject.getJSONArray("info");
                comObj = comAry.getJSONObject(0);
                Log.d("pos obj", comObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tradeView.setText(comObj.getString("trade"));
                        scaleView.setText(comObj.getString("scale"));
                        companyView.setText(company);
                        addressView.setText(comObj.getString("address"));
                        descView.setText(comObj.getString("desc"));
                        natureView.setText(comObj.getString("nature"));
                        idView.setText(comObj.getString("id"));
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

    class FollowTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", id));
            pairs.add(new BasicNameValuePair("username", username));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_follow, "POST", pairs);
            Log.d("follow", jsonObject.toString());

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(getActivity(), "关注成功！", Toast.LENGTH_SHORT).show();
        }

    }

}
