package com.example.godkiller.rencai.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.page.HRResumeViewPage;
import com.example.godkiller.rencai.page.InterviewDetailPage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class HROfferChannelFragment extends Fragment{
    private String cid;
    private String company;
    private ListView offerLv;
    private SimpleAdapter offerAdapter;
    private List<Map<String, Object>> dataList;
    private ProgressDialog dialog;
    private String seekerUsername;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/hr_offer_view.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";
    private View view;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.hr_offer_fragment, null);
            cid = getActivity().getIntent().getStringExtra("id");

            offerLv = (ListView) view.findViewById(R.id.hr_offer_lv);
            offerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    seekerUsername = ((TextView) view.findViewById(R.id.resume_item_username)).getText().toString();
                    Intent intent = new Intent(getActivity(), HRResumeViewPage.class);
                    intent.putExtra("seekerUsername", seekerUsername);
                    intent.putExtra("positionId", cid);
                    startActivity(intent);
                }
            });
            if (dataList == null) {
                new LoadOfferResume().execute();
            }

        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    class LoadOfferResume extends AsyncTask<String, String, String> {
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
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", cid));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray interviewAry = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < interviewAry.length(); i++) {
                        JSONObject info = interviewAry.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("name", info.getString("name"));
                        infoMap.put("username", info.getString("username"));
                        infoMap.put("gender", info.getString("gender"));
                        infoMap.put("workexp", info.getString("workexp"));
                        infoMap.put("birth", info.getString("birth"));
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
            offerAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.resume_item, new String[]{"name", "gender", "workexp", "birth", "username"},
                    new int[]{R.id.resume_item_name, R.id.resume_item_gender, R.id.resume_item_workexp, R.id.resume_item_birth, R.id.resume_item_username});
            offerLv.setAdapter(offerAdapter);

        }
    }
}
