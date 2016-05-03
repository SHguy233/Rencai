package com.example.godkiller.rencai.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.page.PositionDetailPage;
import com.example.godkiller.rencai.page.SearchPage;
import com.example.godkiller.rencai.page.WorkExpEditPage;

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
public class PositionFragment extends Fragment{
    private RadioGroup orderGroup;
    private RadioButton attentionBtn;
    private RadioButton popularityBtn;
    private RadioButton numBtn;
    private RadioButton assessmentBtn;
    private RadioButton defaultBtn;
    private Button msgBtn;
    private ImageView searchbarView;
    private ListView positionLv;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String cid;
    private String company;
    private String order;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/seeker_position_view.php";
    private static String url_order = "http://10.0.3.2:63342/htdocs/db/seeker_position_order.php";
    private static String url_add = "http://10.0.3.2:63342/htdocs/db/seeker_position_attention.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_fragment, null);
        final Intent searchIntent = new Intent(getActivity(), SearchPage.class);
        orderGroup = (RadioGroup) view.findViewById(R.id.order_group);
        attentionBtn = (RadioButton) view.findViewById(R.id.attention_btn);
        popularityBtn = (RadioButton) view.findViewById(R.id.popularity_btn);
        numBtn = (RadioButton) view.findViewById(R.id.demand_num_btn);
        defaultBtn = (RadioButton) view.findViewById(R.id.default_btn);
        assessmentBtn = (RadioButton) view.findViewById(R.id.assessment_btn);
        final int attId = attentionBtn.getId();
        final int popId = popularityBtn.getId();
        final int numId = numBtn.getId();
        final int assId = assessmentBtn.getId();
        final int deaId = defaultBtn.getId();
        orderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == attId) {
                    order = "attention";
                    new OrderByAttention().execute();
                } else if (checkedId == popId) {
                    order = "popularity";
                    new OrderByAttention().execute();
                } else if (checkedId == numId) {
                    order = "num";
                    new OrderByAttention().execute();
                } else if (checkedId == assId) {
                    order = "assessment";
                    new OrderByAttention().execute();
                } else if (checkedId == deaId) {
                    new LoadSeekerPosition().execute();
                }

            }
        });


        msgBtn = (Button) view.findViewById(R.id.msg_btn);
        searchbarView = (ImageView) view.findViewById(R.id.searchbar_view);
        searchbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().startActivity(searchIntent);
            }
        });
        positionLv = (ListView) view.findViewById(R.id.position_lv_seeker);
        positionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_position_seeker)).getText().toString();
                company = ((TextView) view.findViewById(R.id.company_item_position_seeker)).getText().toString();
                new AddAttentionTask().execute();
                Intent intent = new Intent(getActivity(), PositionDetailPage.class);
                intent.putExtra("id", cid);
                intent.putExtra("company", company);
                startActivity(intent);
            }
        });
        new LoadSeekerPosition().execute();
        return view;

    }

    class LoadSeekerPosition extends AsyncTask<String, String, String> {
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
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray posObj = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < posObj.length(); i++) {
                        JSONObject info = posObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString("id"));
                        infoMap.put("company", info.getString("company"));
                        infoMap.put("city", info.getString("city"));
                        infoMap.put("position", info.getString("position"));
                        infoMap.put("num", info.getString("num"));
                        infoMap.put("salary", info.getString("salary"));
                        infoMap.put("degree", info.getString("degree"));
                        infoMap.put("workexp", info.getString("workexp"));
                        infoMap.put("condition", info.getString("condition"));
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
            positionAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.position_item_seeker, new String[]{"position", "salary", "company", "city", "degree", "num", "id"},
                    new int[]{R.id.position_item_position_seeker, R.id.salary_item_position_seeker, R.id.company_item_position_seeker, R.id.work_city_item_position_seeker, R.id.degree_item_position_seeker, R.id.num_item_position_seeker, R.id.id_no_position_seeker});
            positionLv.setAdapter(positionAdapter);

        }
    }

    class OrderByAttention extends AsyncTask<String, String, String> {
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
            pairs.add(new BasicNameValuePair("order", order));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_order, "POST", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray posObj = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < posObj.length(); i++) {
                        JSONObject info = posObj.getJSONObject(i);
                        Map<String, Object> infoMap = new HashMap<String, Object>();
                        infoMap.put("id", info.getString("id"));
                        infoMap.put("company", info.getString("company"));
                        infoMap.put("city", info.getString("city"));
                        infoMap.put("position", info.getString("position"));
                        infoMap.put("num", info.getString("num"));
                        infoMap.put("salary", info.getString("salary"));
                        infoMap.put("degree", info.getString("degree"));
                        infoMap.put("workexp", info.getString("workexp"));
                        infoMap.put("condition", info.getString("condition"));
                        dataList.add(infoMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

    }

    class AddAttentionTask extends AsyncTask<String, String, String> {
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
            //Log.d("id", cid);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_add, "POST", pairs);
            //Log.d("ADD ATTENTION", jsonObject.toString());
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }
    }
}
