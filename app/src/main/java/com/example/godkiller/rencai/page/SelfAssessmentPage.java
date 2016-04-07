package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class SelfAssessmentPage extends BaseActivity implements View.OnClickListener{
    private EditText selfAssessText;
    private Button backBtn;
    private Button saveBtn;
    private String selfAssessStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edu_background_page);

        selfAssessText = (EditText) findViewById(R.id.self_assessment_text);
        backBtn = (Button) findViewById(R.id.back_button_sa);
        saveBtn = (Button) findViewById(R.id.save_btn_sa);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_sa:
                finish();
                break;
            case R.id.save_btn_sa:
                saveEvent();
                break;
            default:
                break;
        }
    }

    public void saveEvent() {
        selfAssessStr = selfAssessText.getText().toString();
        Toast.makeText(SelfAssessmentPage.this, selfAssessStr, Toast.LENGTH_SHORT).show();
    }
}
