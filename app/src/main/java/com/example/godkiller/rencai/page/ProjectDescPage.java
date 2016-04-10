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
public class ProjectDescPage extends BaseActivity implements View.OnClickListener{
    private Button saveBtn;
    private Button backBtn;
    private EditText proDescText;
    public static final int PRO_DESC_RESULT_OK = 06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.project_description_page);
        saveBtn = (Button) findViewById(R.id.save_btn_pd);
        backBtn = (Button) findViewById(R.id.back_button_pd);
        proDescText = (EditText) findViewById(R.id.project_desc_text);

        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_pd:
                finish();
                break;
            case R.id.save_btn_pd:
                saveEvent();
                break;
            default:
                break;
        }

    }
    private void saveEvent() {
        Intent descIntent = new Intent();
        Bundle b = new Bundle();
        b.putString("desc", proDescText.getText().toString());
        descIntent.putExtras(b);
        setResult(PRO_DESC_RESULT_OK, descIntent);
        finish();
    }
}
