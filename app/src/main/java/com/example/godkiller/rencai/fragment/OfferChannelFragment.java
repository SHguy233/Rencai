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

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.page.InterviewDetailPage;
import com.example.godkiller.rencai.page.OfferDetailPage;

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
public class OfferChannelFragment extends Fragment {
    private ListView offerLv;
    private SimpleAdapter offerAdapter;
    private List<Map<String, Object>> dataList;
    private ProgressDialog dialog;
    private String username;
    private String cid;
    private String company;
    private String offerId;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/seeker_offer_view.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_fragment, null);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        offerLv = (ListView) view.findViewById(R.id.offer_lv);
        offerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_positionid2)).getText().toString();
                company = ((TextView) view.findViewById(R.id.offer_item_company)).getText().toString();
                offerId = ((TextView)view.findViewById(R.id.id_no_offerid)).getText().toString();
                Intent intent = new Intent(getActivity(), OfferDetailPage.class);
                intent.putExtra("id", cid);
                intent.putExtra("company", company);
                intent.putExtra("offerId", offerId);
                startActivity(intent);
            }
        });
        if (dataList == null) {
            new LoadOfferTask().execute();
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

    class LoadOfferTask extends AsyncTask<String, String, String> {
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
            pairs.add(new BasicNameValuePair("seekerUsername", username));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray interviewAry = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < interviewAry.length(); i++) {
                        JSONObject info = interviewAry.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("company", info.getString("company"));
                        infoMap.put("city", info.getString("city"));
                        infoMap.put("position", info.getString("position"));
                        infoMap.put("salary", info.getString("salary"));
                        infoMap.put("id", info.getString("id"));
                        infoMap.put("offerId", info.getString("offerId"));
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
            offerAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.offer_item_seeker, new String[]{"position", "company", "city", "salary", "id", "offerId"},
                    new int[]{R.id.offer_item_position, R.id.offer_item_company, R.id.offer_item_city, R.id.offer_item_salary, R.id.id_no_positionid2, R.id.id_no_offerid});
            offerLv.setAdapter(offerAdapter);

        }
    }
}
