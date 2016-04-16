package com.example.godkiller.rencai.position;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/4/4.
 */
public class PositionPageOfDemand extends BaseActivity implements View.OnClickListener{
    private Spinner tradeSpinner;
    private Spinner categorySpinner;
    private Spinner positionSpinner;
    private Map<String, Map<String, List<String>>> positionData = null;
    public static final int POSITION_RESULT_OK = 04;

    private String currentTrade;
    private String currentCategory;
    private String currentPosition;
    private Button backBtn;
    private Button saveBtn;

    private Handler positionHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PullPosition.PARSESUCC:
                        positionData = (Map<String, Map<String, List<String>>>) msg.obj;
                        initData();
                        break;
                    default:
                        break;
                }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.position_choose_page);
        backBtn = (Button) findViewById(R.id.back_button_pc);
        saveBtn = (Button) findViewById(R.id.save_btn_pc);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        tradeSpinner = (Spinner) findViewById(R.id.trade_spinner);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        positionSpinner = (Spinner) findViewById(R.id.position_spinner);
        PullPosition pullPosition = new PullPosition(positionHandler);
        InputStream inputStream = null;
        try {
            inputStream = this.getResources().getAssets().open("trade_data.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        pullPosition.getTrades(inputStream);
    }

    private void initData() {
        if (positionData != null) {
            String[] strings = positionData.keySet().toArray(new String[0]);
            System.out.println(strings);
            tradeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, strings));
            currentTrade = getCurrentTrade();
            bindCategoryAdapter(currentTrade);
            currentCategory = getCurrentCategory();
            bindPositionAdapter(currentCategory);
            setOnItemSelectedListener();

        }
    }

    private void setOnItemSelectedListener() {
        tradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTrade = getCurrentTrade();
                bindCategoryAdapter(currentTrade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = getCurrentCategory();
                bindPositionAdapter(currentCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = getCurrentPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void bindPositionAdapter(String currentCategory) {
        positionSpinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                positionData.get(currentTrade).get(currentCategory)));
    }

    private void bindCategoryAdapter(String currentTrade) {
        categorySpinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                positionData.get(currentTrade).keySet().toArray(new String[0])));
    }

    private String getCurrentTrade() {
        return tradeSpinner.getSelectedItem().toString();
    }

    private String getCurrentCategory() {
        return  categorySpinner.getSelectedItem().toString();
    }

    private String getCurrentPosition() {
        return  positionSpinner.getSelectedItem().toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_pc:
                finish();
                break;
            case R.id.save_btn_pc:
                currentPosition = positionSpinner.getSelectedItem().toString();
                Intent positionIntent = new Intent();
                Bundle b = new Bundle();
                b.putString("position", currentPosition);
                positionIntent.putExtras(b);
                setResult(POSITION_RESULT_OK, positionIntent);
                finish();
                break;
            default:
                break;

        }
    }
}
