package com.example.godkiller.rencai.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.example.godkiller.rencai.R.id.workexp_checkbox;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class HRResumeChannelFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private CheckBox ageBox;
    private CheckBox genderBox;
    private CheckBox foreignBox;
    private CheckBox computerBox;
    private CheckBox workexpBox;
    private CheckBox degreeBox;
    private CheckBox remoteBox;
    private Button filterBtn;
    private ListView resumeLv;
    private String cid;
    private String positionId;
    private SimpleAdapter resumeAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private String seekerUsername;
    private String age = "0";
    private String gender = "0";
    private String workexp = "0";
    private String foreign = "0";
    private String degree = "0";
    private String remote = "0";
    private String computer = "0";
    private ProgressDialog dialog;
    private View view;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/hr_resume_view.php";
    private static String url_filter = "http://10.0.3.2:63342/htdocs/db/hr_resume_filter_view.php";
    private static String url_send = "http://10.0.3.2:63342/htdocs/db/hr_send_interview.php";
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
        if (view == null) {
            view = inflater.inflate(R.layout.hr_resume_fragment, null);
            cid = getActivity().getIntent().getStringExtra("id");

            ageBox = (CheckBox) view.findViewById(R.id.age_checkbox);
            genderBox = (CheckBox) view.findViewById(R.id.gender_checkbox);
            degreeBox = (CheckBox) view.findViewById(R.id.degree_checkbox);
            foreignBox = (CheckBox) view.findViewById(R.id.foreign_checkbox);
            workexpBox = (CheckBox) view.findViewById(workexp_checkbox);
            remoteBox = (CheckBox) view.findViewById(R.id.remote_checkbox);
            computerBox = (CheckBox) view.findViewById(R.id.computer_checkbox);
            ageBox.setOnCheckedChangeListener(this);
            genderBox.setOnCheckedChangeListener(this);
            degreeBox.setOnCheckedChangeListener(this);
            foreignBox.setOnCheckedChangeListener(this);
            workexpBox.setOnCheckedChangeListener(this);
            remoteBox.setOnCheckedChangeListener(this);
            computerBox.setOnCheckedChangeListener(this);

            filterBtn = (Button) view.findViewById(R.id.filter_btn);
            resumeLv = (ListView) view.findViewById(R.id.my_resume_lv);
            resumeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    seekerUsername = ((TextView) view.findViewById(R.id.resume_item_username)).getText().toString();
                    Intent intent = new Intent(getActivity(), HRResumeViewPage.class);
                    startActivity(intent);
                }
            });
            resumeLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    seekerUsername = ((TextView) view.findViewById(R.id.resume_item_username)).getText().toString();
                    sendDialog();
                    return false;
                }
            });

            filterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FilterTask().execute();
                }
            });
            new LoadPositionResume().execute();
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

    private String setCondition(boolean isChecked) {
        if (isChecked) {
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.age_checkbox:
                age = setCondition(isChecked);
                break;
            case R.id.gender_checkbox:
                gender = setCondition(isChecked);
                break;
            case R.id.degree_checkbox:
                degree = setCondition(isChecked);
                break;
            case R.id.foreign_checkbox:
                foreign = setCondition(isChecked);
                break;
            case R.id.workexp_checkbox:
                workexp = setCondition(isChecked);
                break;
            case R.id.computer_checkbox:
                computer = setCondition(isChecked);
                break;
            case R.id.remote_checkbox:
                remote = setCondition(isChecked);
                break;
        }
    }

    private void sendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("确定发送？");
        builder.setTitle("发送面试通知");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                new SendInterviewTask().execute();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    class LoadPositionResume extends AsyncTask<String, String, String> {
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
                    JSONArray resAry = jsonObject.getJSONArray(TAG_INFO);
                    for (int i=0; i<resAry.length(); i++) {
                        JSONObject info = resAry.getJSONObject(i);
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
            resumeAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.resume_item, new String[]{"name", "gender", "workexp", "birth", "username"},
                    new int[]{R.id.resume_item_name, R.id.resume_item_gender, R.id.resume_item_workexp, R.id.resume_item_birth, R.id.resume_item_username});
            resumeLv.setAdapter(resumeAdapter);

        }
    }

    class FilterTask extends AsyncTask<String, String, String> {
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
            pairs.add(new BasicNameValuePair("age", age));
            pairs.add(new BasicNameValuePair("gender", gender));
            pairs.add(new BasicNameValuePair("foreign", foreign));
            pairs.add(new BasicNameValuePair("computer", computer));
            pairs.add(new BasicNameValuePair("remote", remote));
            pairs.add(new BasicNameValuePair("workexp", workexp));
            pairs.add(new BasicNameValuePair("degree", degree));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_filter, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray resAry = jsonObject.getJSONArray(TAG_INFO);
                    JSONObject info = resAry.getJSONObject(0);
                    Log.d("resume data", info.toString());
                    Map<String, Object> infoMap = new HashMap<String, Object>();
                    infoMap.put("name", info.getString("name"));
                    infoMap.put("username", info.getString("username"));
                    infoMap.put("gender", info.getString("gender"));
                    infoMap.put("workexp", info.getString("workexp"));
                    infoMap.put("birth", info.getString("birth"));
                    dataList.add(infoMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            resumeAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.resume_item, new String[]{"name", "gender", "workexp", "birth", "username"},
                    new int[]{R.id.resume_item_name, R.id.resume_item_gender, R.id.resume_item_workexp, R.id.resume_item_birth, R.id.resume_item_username});
            resumeLv.setAdapter(resumeAdapter);

        }
    }

    class SendInterviewTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
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
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_send, "POST", pairs);
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
            Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
        }
    }


}
