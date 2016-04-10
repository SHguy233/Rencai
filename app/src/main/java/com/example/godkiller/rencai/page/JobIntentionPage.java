package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfIntention;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.JobIntent;
import com.example.godkiller.rencai.db.JobIntentService;
import com.example.godkiller.rencai.trade.TradeCategoryOfIntention;
import com.example.godkiller.rencai.position.PositionPageOfIntention;

/**
 * Created by GodKiller on 2016/3/9.
 */
public class JobIntentionPage extends BaseActivity implements View.OnClickListener{
    private LinearLayout salaryLayout;
    private LinearLayout workPlaceLayout;
    private LinearLayout tradeLayout;
    private LinearLayout positionLayout;
    private TextView workPlaceView;
    private TextView salaryView;
    private TextView tradeView;
    private TextView positionView;
    private Button backBtn;
    private Button saveBtn;
    private String username;

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

        tradeView = (TextView) findViewById(R.id.trade_category_view_ji);
        tradeLayout = (LinearLayout) findViewById(R.id.trade_category_layout_ji);
        tradeLayout.setOnClickListener(this);

        positionView = (TextView) findViewById(R.id.job_category_view);
        positionLayout = (LinearLayout) findViewById(R.id.job_category_layout);
        positionLayout.setOnClickListener(this);

        backBtn = (Button) findViewById(R.id.back_button_ji);
        backBtn.setOnClickListener(this);

        saveBtn = (Button) findViewById(R.id.save_btn_ji);
        saveBtn.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
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
                Intent cityIntent = new Intent(JobIntentionPage.this, CityListOfIntention.class);
                startActivityForResult(cityIntent, 0);
                break;
            case R.id.trade_category_layout_ji:
                Intent tradeIntent = new Intent(JobIntentionPage.this, TradeCategoryOfIntention.class);
                startActivityForResult(tradeIntent, 0);
                break;
            case R.id.job_category_layout:
                Intent positionIntent = new Intent(JobIntentionPage.this, PositionPageOfIntention.class);
                startActivityForResult(positionIntent, 0);
                break;
            case R.id.back_button_ji:
                finish();
                break;
            case R.id.save_btn_ji:
                saveEvent();
            default:
                break;
        }
    }

    private void saveEvent() {
        JobIntent jobIntent = new JobIntent();
        jobIntent.setUsername(username);
        jobIntent.setWorkPlace(workPlaceView.getText().toString());
        jobIntent.setTradeCategory(tradeView.getText().toString());
        jobIntent.setPositionCategory(positionView.getText().toString());
        jobIntent.setSalary(salaryView.getText().toString());
        JobIntentService service = new JobIntentService(this);
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String sql =  "select * from personalinfo where username='" + username + "'";
        if (exits("personalinfo")) {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() == 0){
                service.save(jobIntent);
                Toast.makeText(JobIntentionPage.this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                service.update(jobIntent);
                Toast.makeText(JobIntentionPage.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        }
        finish();

    }

    public boolean exits(String table){
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        boolean exits = false;
        String sql = "select * from sqlite_master where name="+"'"+table+"'";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount()!=0){
            exits = true;
        }
        return exits;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                Bundle cityBundle = data.getExtras();
                String city = cityBundle.getString("city");
                updateWorkPlace(city);
                break;
            case TradeCategoryOfIntention.TRADE_RESULT_OK:
                Bundle tradeBundle = data.getExtras();
                String trade = tradeBundle.getString("trade");
                updateTrade(trade);
                break;
            case PositionPageOfIntention.POSITION_RESULT_OK:;
                Bundle positionBundle = data.getExtras();
                String position = positionBundle.getString("position");
                updatePosition(position);
                break;
            default:
                break;
        }

    }

    private void updatePosition(String position) {
        positionView.setText(position);
    }

    private void updateWorkPlace(String city) { workPlaceView.setText(city);}
    private void updateSalary (String salary) {
        salaryView.setText(salary);
    }
    private void updateTrade (String trade) { tradeView.setText(trade);};

}
