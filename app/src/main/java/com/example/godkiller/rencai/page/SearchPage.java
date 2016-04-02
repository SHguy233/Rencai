package com.example.godkiller.rencai.page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfSearch;
import com.example.godkiller.rencai.trade.TradeCategoryOfSearch;

/**
 * Created by GodKiller on 2016/3/7.
 */
public class SearchPage extends BaseActivity implements View.OnClickListener{
    private EditText searchbarText;
    private ImageView cancelView;
    private LinearLayout tradeCategoryLayout;
    private LinearLayout positionCategoryLayout;
    private LinearLayout workingCityLayout;
    private LinearLayout salaryLayout;
    private TextView tradeCategoryView;
    private TextView workingCityView;
    private TextView salaryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_page);


        searchbarText = (EditText) findViewById(R.id.seachbar_edittext);
        cancelView = (ImageView) findViewById(R.id.cancel_image);
        salaryView = (TextView) findViewById(R.id.salary_view);
        workingCityView = (TextView) findViewById(R.id.working_city_view);
        tradeCategoryView = (TextView) findViewById(R.id.trade_category_view);
        tradeCategoryLayout = (LinearLayout) findViewById(R.id.trade_category_layout);
        positionCategoryLayout = (LinearLayout) findViewById(R.id.position_category_layout);
        workingCityLayout = (LinearLayout) findViewById(R.id.working_city_layout);
        salaryLayout = (LinearLayout) findViewById(R.id.salary_layout);

        cancelView.setOnClickListener(this);
        tradeCategoryLayout.setOnClickListener(this);
        positionCategoryLayout.setOnClickListener(this);
        workingCityLayout.setOnClickListener(this);
        salaryLayout.setOnClickListener(this);


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
                jumpIntent(this, PositionCategoryPage.class);
                break;
            case R.id.working_city_layout:
                Intent cityIntent = new Intent(SearchPage.this,CityListOfSearch.class);
                startActivityForResult(cityIntent, 0);
                break;
            case R.id.salary_layout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(SearchPage.this);
                final String[] salaries = {"0-2000元/月", "2001-4000元/月", "4001-6000元/月", "6001-8000元/月", "8001-10000元/月",
                        "10001-15000元/月", "15001-25000元/月", "25000元/月以上"};
                builder.setItems(salaries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateSalary(salaries[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            default:
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
            default:
                break;

        }
    }

    private void updateCity(String city) { workingCityView.setText(city);}
    private void updateSalary(String salary) {
        salaryView.setText(salary);
    }
    private void updateTrade(String trade) { tradeCategoryView.setText(trade);}

}
