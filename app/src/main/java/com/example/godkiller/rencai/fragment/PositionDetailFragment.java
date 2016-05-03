package com.example.godkiller.rencai.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.page.PositionDetailPage;
import com.example.godkiller.rencai.page.ResumePreviewPage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class PositionDetailFragment extends Fragment {
    private TextView positionView;
    private TextView salaryView;
    private TextView companyView;
    private TextView cityView;
    private TextView expView;
    private TextView degreeView;
    private TextView numView;
    private TextView descView;
    private ImageButton collectBtn;
    private Button sendBtn;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_details = "http://10.0.3.2:63342/htdocs/db/seeker_position_details.php";
    private static  String url_collect = "http://10.0.3.2:63342/htdocs/db/seeker_position_collect_save.php";

    private static final String TAG_SUCCESS = "success";
    private String username;
    private String id;
    private JSONObject posObj;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_detail_fragment, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        final Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("id");


        collectBtn = (ImageButton) view.findViewById(R.id.collect_button);
        sendBtn = (Button) view.findViewById(R.id.send_resume_fra);
        positionView = (TextView) view.findViewById(R.id.position_item_position_fra);
        salaryView = (TextView) view.findViewById(R.id.salary_item_position_fra);
        companyView = (TextView) view.findViewById(R.id.company_item_view_fra);
        cityView = (TextView) view.findViewById(R.id.work_city_item_position_fra);
        expView = (TextView) view.findViewById(R.id.workexp_item_position_fra);
        degreeView = (TextView) view.findViewById(R.id.degree_item_position_fra);
        numView = (TextView) view.findViewById(R.id.num_item_position_fra);
        descView = (TextView) view.findViewById(R.id.desc_position_fra);
        new GetSeekerPositionTask().execute();
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveCollectionTask().execute();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResumePreviewPage.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        return view;
    }

    class GetSeekerPositionTask extends AsyncTask<String, String, String> {

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
                Log.d("pos obj", "do in back");
                pairs.add(new BasicNameValuePair("id", id));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_details, "GET", pairs);
                JSONArray posAry = jsonObject.getJSONArray("info");
                posObj = posAry.getJSONObject(0);
                Log.d("pos obj", posObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
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
            dialog.dismiss();
        }

    }

    class SaveCollectionTask extends AsyncTask<String, String, String> {

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

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_collect, "POST", pairs);
            Log.d("collect", jsonObject.toString());

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
            Toast.makeText(getActivity(), "收藏成功！", Toast.LENGTH_SHORT).show();
        }

    }


}
