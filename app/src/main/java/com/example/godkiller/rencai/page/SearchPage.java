package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfSearch;
import com.example.godkiller.rencai.db.JSONParser;
import com.example.godkiller.rencai.position.PositionPageOfSearch;
import com.example.godkiller.rencai.trade.TradeCategoryOfSearch;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GodKiller on 2016/3/7.
 */
public class SearchPage extends BaseActivity implements View.OnClickListener,View.OnLongClickListener{
    private EditText searchbarText;
    private ImageView cancelView;
    private LinearLayout tradeCategoryLayout;
    private LinearLayout positionCategoryLayout;
    private LinearLayout workingCityLayout;
    private TextView tradeCategoryView;
    private TextView workingCityView;
    private EditText salaryText;
    private TextView positionCategoryView;
    private Button searchBtn;
    private Button saveBtn;
    private String position;
    private String trade;
    private String city;
    private String salary;

    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    private static  String url_details = "http://10.0.3.2:63342/htdocs/db/job_intention_details.php";
    private static  String url_update = "http://10.0.3.2:63342/htdocs/db/job_intention_update.php";
    private static final String TAG_SUCCESS = "success";
    private String username;
    private JSONObject intentionObj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        searchbarText = (EditText) findViewById(R.id.seachbar_edittext);
        cancelView = (ImageView) findViewById(R.id.cancel_image);
        salaryText = (EditText) findViewById(R.id.salary_text);
        positionCategoryView = (TextView) findViewById(R.id.position_category_view);
        workingCityView = (TextView) findViewById(R.id.working_city_view);
        tradeCategoryView = (TextView) findViewById(R.id.trade_category_view);

        tradeCategoryLayout = (LinearLayout) findViewById(R.id.trade_category_layout);
        positionCategoryLayout = (LinearLayout) findViewById(R.id.position_category_layout);
        workingCityLayout = (LinearLayout) findViewById(R.id.working_city_layout);

        searchBtn = (Button) findViewById(R.id.search_button);
        saveBtn = (Button) findViewById(R.id.save_button_sp);
        searchBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        cancelView.setOnClickListener(this);
        tradeCategoryLayout.setOnClickListener(this);
        positionCategoryLayout.setOnClickListener(this);
        workingCityLayout.setOnClickListener(this);
        tradeCategoryLayout.setOnLongClickListener(this);
        positionCategoryLayout.setOnLongClickListener(this);
        workingCityLayout.setOnLongClickListener(this);


        new GetIntentionTask().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_image:
                SearchPage.this.finish();
                break;
            case R.id.trade_category_layout:
                Intent tradeIntent = new Intent(SearchPage.this, TradeCategoryOfSearch.class);
                startActivityForResult(tradeIntent, 0);
                break;
            case R.id.position_category_layout:
                Intent positionIntent = new Intent(SearchPage.this, PositionPageOfSearch.class);
                startActivityForResult(positionIntent, 0);
                break;
            case R.id.working_city_layout:
                Intent cityIntent = new Intent(SearchPage.this,CityListOfSearch.class);
                startActivityForResult(cityIntent, 0);
                break;
            case R.id.search_button:
                Intent searchIntent = new Intent(SearchPage.this, SearchResultPage.class);
                searchIntent.putExtra("trade", tradeCategoryView.getText().toString());
                searchIntent.putExtra("position", positionCategoryView.getText().toString());
                searchIntent.putExtra("salary", salaryText.getText().toString());
                searchIntent.putExtra("city", workingCityView.getText().toString());
                searchIntent.putExtra("keywords", searchbarText.getText().toString());
                startActivity(searchIntent);
                break;
            case R.id.save_button_sp:
                city = workingCityView.getText().toString();
                trade = tradeCategoryView.getText().toString();
                position = positionCategoryView.getText().toString();
                salary = salaryText.getText().toString();
                new SaveIntentionTask().execute();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                SearchPage.this.finish();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case CityListOfSearch.CITY_RESULT_OK:
                Bundle cityBundle = data.getExtras();
                String city = cityBundle.getString("city");
                updateCity(city);
                break;
            case TradeCategoryOfSearch.TRADE_RESULT_OK:
                Bundle tradeBundle = data.getExtras();
                String trade = tradeBundle.getString("trade");
                updateTrade(trade);
                break;
            case PositionPageOfSearch.POSITION_RESULT_OK:
                Bundle positionBundle = data.getExtras();
                String position = positionBundle.getString("position");
                updatePosition(position);
                break;

            default:
                break;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.trade_category_layout:
                AlertDialog.Builder tradeBuilder = new AlertDialog.Builder(SearchPage.this);
                tradeBuilder.setTitle("行业类别");
                final String[] trade = {"不限"};
                tradeBuilder.setSingleChoiceItems(trade, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateTrade("不限");
                        dialog.dismiss();
                    }
                });
                tradeBuilder.show();
                break;
            case R.id.position_category_layout:
                AlertDialog.Builder positionBuilder = new AlertDialog.Builder(SearchPage.this);
                positionBuilder.setTitle("职业类别");
                final String[] position = {"不限"};
                positionBuilder.setSingleChoiceItems(position, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePosition("不限");
                        dialog.dismiss();
                    }
                });
                positionBuilder.show();
                break;
            case R.id.working_city_layout:
                AlertDialog.Builder cityBuilder = new AlertDialog.Builder(SearchPage.this);
                cityBuilder.setTitle("工作地点");
                final String[] city = {"不限"};
                cityBuilder.setSingleChoiceItems(city, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateCity("不限");
                        dialog.dismiss();
                    }
                });
                cityBuilder.show();
                break;
        }
        return false;
    }

    class GetIntentionTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchPage.this);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("username", username));
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_details, "GET", pairs);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray intentionAry = jsonObject.getJSONArray("info");
                    intentionObj = intentionAry.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                workingCityView.setText(intentionObj.getString("city"));
                                tradeCategoryView.setText(intentionObj.getString("trade"));
                                positionCategoryView.setText(intentionObj.getString("position"));
                                salaryText.setText(intentionObj.getString("salary"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
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

    class SaveIntentionTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchPage.this);
            dialog.setMessage("saving...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("trade", trade));
            pairs.add(new BasicNameValuePair("position", position));
            pairs.add(new BasicNameValuePair("salary", salary));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_update, "POST", pairs);

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Toast.makeText(SearchPage.this, "保存成功！", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateCity(String city) { workingCityView.setText(city);}
    private void updateTrade(String trade) { tradeCategoryView.setText(trade);}
    private void updatePosition(String position) { positionCategoryView.setText(position);}

}
