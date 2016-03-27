package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

/**
 * Created by GodKiller on 2016/3/9.
 */
public class JobIntentionPage extends BaseActivity implements View.OnClickListener{
    private LinearLayout salaryLayout;
    private TextView salaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.job_intention_page);

        salaryView = (TextView) findViewById(R.id.salary_intention_view);
        salaryLayout  = (LinearLayout) findViewById(R.id.salary_intention_layout);
        salaryLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.salary_intention_layout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(JobIntentionPage.this);
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

    private void updateSalary (String salary) {
        salaryView.setText(salary);
    }
}
