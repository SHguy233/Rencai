package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfIntention;

/**
 * Created by GodKiller on 2016/3/9.
 */
public class JobIntentionPage extends BaseActivity implements View.OnClickListener{
    private LinearLayout salaryLayout;
    private LinearLayout workPlaceLayout;
    private TextView workPlaceView;
    private TextView salaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.job_intention_page);

        salaryView = (TextView) findViewById(R.id.salary_intention_view);
        salaryLayout  = (LinearLayout) findViewById(R.id.salary_intention_layout);
        salaryLayout.setOnClickListener(this);

        workPlaceView = (TextView) findViewById(R.id.working_place_view);
        workPlaceLayout = (LinearLayout) findViewById(R.id.working_place_layout);
        workPlaceLayout.setOnClickListener(this);
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
            case R.id.working_place_layout:
                final Intent intent = new Intent(JobIntentionPage.this, CityListOfIntention.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                Bundle b = data.getExtras();
                String city = b.getString("city");
                updateWorkPlace(city);
                break;
            default:
                break;
        }

    }
    private void updateWorkPlace(String city) { workPlaceView.setText(city);}
    private void updateSalary (String salary) {
        salaryView.setText(salary);
    }
}
