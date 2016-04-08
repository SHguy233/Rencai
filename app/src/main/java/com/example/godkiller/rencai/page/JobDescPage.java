package com.example.godkiller.rencai.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/4/8.
 */
public class JobDescPage extends BaseActivity implements View.OnClickListener{
    private Button saveBtn;
    private Button backBtn;
    private EditText jobDescText;
    public static final int DESC_RESULT_OK = 05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.job_description_page);
        saveBtn = (Button) findViewById(R.id.save_btn_jd);
        backBtn = (Button) findViewById(R.id.back_button_jd);
        jobDescText = (EditText) findViewById(R.id.job_desc_text);

        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_jd:
                finish();
                break;
            case R.id.save_btn_jd:
                saveEvent();
                break;
            default:
                break;
        }

    }
    private void saveEvent() {
        Intent descIntent = new Intent();
        Bundle b = new Bundle();
        b.putString("desc", jobDescText.getText().toString());
        descIntent.putExtras(b);
        setResult(DESC_RESULT_OK, descIntent);
        finish();
    }
}
