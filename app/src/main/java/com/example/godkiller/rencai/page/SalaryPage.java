package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by GodKiller on 2016/3/9.
 */
public class SalaryPage extends BaseActivity implements View.OnClickListener{
    private Button backButton;
    private Button searchButton;
    private Spinner salarySpinner;
    private ArrayAdapter<String> salaryAdapter;
    private String[] salaryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.salary_page);

        searchButton = (Button) findViewById(R.id.search_button);
        backButton = (Button) findViewById(R.id.back_button);
        salarySpinner = (Spinner) findViewById(R.id.salary_spinner);
        backButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        salaryList = new String[] {"0-2000元/月", "2001-4000元/月", "4001-6000元/月", "6001-8000元/月", "8001-10000元/月",
                "10001-15000元/月", "15001-25000元/月", "25000元/月以上"};
        salaryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, salaryList);
        salarySpinner.setAdapter(salaryAdapter);
        salaryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        salarySpinner.setSelection(1, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                Intent backIntent = new Intent(this, SearchPage.class);
                startActivity(backIntent);
                break;
            case R.id.search_button:
                Intent searchIntent = new Intent(this, SearchPage.class);
                startActivity(searchIntent);
            default:
                break;
        }
    }
}
