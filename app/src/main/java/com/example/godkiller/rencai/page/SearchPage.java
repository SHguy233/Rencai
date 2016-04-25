package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfSearch;
import com.example.godkiller.rencai.position.PositionPageOfSearch;
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
    private TextView tradeCategoryView;
    private TextView workingCityView;
    private EditText salaryText;
    private TextView positionCategoryView;
    private Button searchBtn;
    private String position;
    private String trade;
    private String city;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_page);


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
        searchBtn.setOnClickListener(this);

        cancelView.setOnClickListener(this);
        tradeCategoryLayout.setOnClickListener(this);
        positionCategoryLayout.setOnClickListener(this);
        workingCityLayout.setOnClickListener(this);


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
                searchEvent();
                break;
        }
    }

    private void searchEvent() {
        tradeCategoryView.getText();
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

    private void updateCity(String city) { workingCityView.setText(city);}
    private void updateTrade(String trade) { tradeCategoryView.setText(trade);}
    private void updatePosition(String position) { positionCategoryView.setText(position);}

}
