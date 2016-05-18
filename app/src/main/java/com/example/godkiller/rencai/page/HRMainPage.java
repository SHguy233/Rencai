package com.example.godkiller.rencai.page;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.ActivityCollector;
import com.example.godkiller.rencai.db.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class HRMainPage extends Activity implements View.OnClickListener {
    private ListView drawerLv;
    private DrawerLayout drawerLayout;
    private ArrayList<String> menuList;
    private ArrayAdapter<String> menuAdapter;
    private long exitTime = 0;
    private Button backBtn;
    private Button addBtn;
    private ListView positionLv;
    private String cid;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;
    private String username;
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_view = "http://10.0.3.2:63342/htdocs/db/hr_position_view.php";
    private static String url_delete = "http://10.0.3.2:63342/htdocs/db/hr_position_delete.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INFO = "info";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_main_page);
        initSlidingMenu();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        if (username == null) {
            Toast.makeText(HRMainPage.this, "身份已过期，请重新登录！", Toast.LENGTH_SHORT).show();
        }
        addBtn = (Button) findViewById(R.id.add_my_position_btn);
        positionLv = (ListView) findViewById(R.id.my_position_lv);

        addBtn.setOnClickListener(this);

        new LoadHRPosition().execute();
        positionLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cid = ((TextView) view.findViewById(R.id.id_no_position)).getText().toString();
                deleteDialog();
                return false;
            }
        });
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HRMainPage.this);
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DeletePositionTask().execute();


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


    private void initSlidingMenu(){
        drawerLv = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = new ArrayList<String>();
        menuList.add("我的简历");
        menuList.add("公司信息");
        menuList.add("帐号信息");
        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        drawerLv.setAdapter(menuAdapter);
        drawerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(HRMainPage.this, HRResumePage.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(HRMainPage.this, CompanyInfoPage.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(HRMainPage.this, MyAccountPage.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(drawerLv);
            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    class LoadHRPosition extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HRMainPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_view, "GET", pairs);
            dataList = new ArrayList<Map<String, Object>>();

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray posAry = jsonObject.getJSONArray(TAG_INFO);
                    for (int i = 0; i < posAry.length(); i++) {
                        JSONObject info = posAry.getJSONObject(i);
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
            positionAdapter = new SimpleAdapter(getApplicationContext(), dataList, R.layout.position_item, new String[]{"position", "salary", "company", "city", "workexp", "degree", "num", "condition", "id"},
                    new int[]{R.id.position_item_position, R.id.salary_item_position, R.id.company_item_view, R.id.work_city_item_position, R.id.workexp_item_position, R.id.degree_item_position, R.id.num_item_position, R.id.desc_position, R.id.id_no_position});
            positionLv.setAdapter(positionAdapter);

        }
    }

    class DeletePositionTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HRMainPage.this);
            dialog.setMessage("deleting...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id",cid));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_delete, "POST", pairs);
            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent intent = getIntent();
                    startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(HRMainPage.this, "删除成功！", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_my_position_btn:
                Intent intent = new Intent(getApplicationContext(), PositionAddPage.class);
                startActivityForResult(intent, 400);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 400) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }


}
