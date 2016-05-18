package com.example.godkiller.rencai.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.page.CompanyDetailPage;
import com.example.godkiller.rencai.page.HRResumeViewPage;
import com.example.godkiller.rencai.page.HRSendInterviewPage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.godkiller.rencai.R.id.workexp_checkbox;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class AdminBeforeChannelFragment extends Fragment {
    private View view;
    private ListView companyLv;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private String cid;
    private static  String url_view = "http://10.0.3.2:63342/htdocs/db/company_info_commit_view.php";
    private SimpleAdapter companyAdapter;
    private List<Map<String, Object>> dataList;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.admin_before_fragment, null);
            companyLv = (ListView) view.findViewById(R.id.admin_company_lv_before);
            companyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cid = ((TextView) view.findViewById(R.id.id_no_admin_company)).getText().toString();
                    Intent intent = new Intent(getActivity(), CompanyDetailPage.class);
                    intent.putExtra("companyid", cid);
                    startActivityForResult(intent, 900);
                }
            });
            new GetAdminCompanyTask().execute();

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

    class GetAdminCompanyTask extends AsyncTask<String, String, String> {

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
            String result = "";
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
                dataList = new ArrayList<Map<String, Object>>();
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray comObj = jsonObject.getJSONArray("info");
                    for (int i = 0; i < comObj.length(); i++) {
                        JSONObject info = comObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString("id"));
                        infoMap.put("company", info.getString("company"));
                        infoMap.put("username", info.getString("username"));
                        dataList.add(infoMap);
                    }
                    result = "success";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("success")) {
                companyAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.admin_company_item, new String[]{"id", "company", "username"},
                        new int[]{R.id.id_no_admin_company, R.id.admin_company_view, R.id.admin_username_view});
                companyLv.setAdapter(companyAdapter);
            }
        }

    }

}
