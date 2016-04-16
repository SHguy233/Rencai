package com.example.godkiller.rencai.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;
import com.example.godkiller.rencai.city.CityListOfDemand;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.PositionInfo;
import com.example.godkiller.rencai.db.PositionInfoService;
import com.example.godkiller.rencai.db.ProjectExpService;
import com.example.godkiller.rencai.position.PositionPageOfDemand;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class AddPositionPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button releaseBtn;
    private LinearLayout workPlaceLayout;
    private LinearLayout positionLayout;
    private LinearLayout numLayout;
    private LinearLayout salaryLayout;
    private LinearLayout conditionLayout;
    private LinearLayout degreeLayout;
    private LinearLayout workexpLayout;
    private TextView workPlaceView;
    private TextView positionView;
    private EditText numText;
    private EditText salaryText;
    private TextView conditionView;
    private TextView workexpView;
    private TextView degreeView;
    private String conDesc;
    private String company;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_position_page);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        backBtn = (Button) findViewById(R.id.back_button_add_position);
        releaseBtn = (Button) findViewById(R.id.release_btn_mp);
        workPlaceLayout = (LinearLayout) findViewById(R.id.working_place_layout_mp);
        positionLayout = (LinearLayout) findViewById(R.id.job_category_layout_mp);
        numLayout = (LinearLayout) findViewById(R.id.demand_num_layout_mp);
        salaryLayout = (LinearLayout) findViewById(R.id.salary_layout_mp);
        conditionLayout = (LinearLayout) findViewById(R.id.condition_layout_mp);
        workexpLayout = (LinearLayout) findViewById(R.id.workexp_layout_mp);
        degreeLayout = (LinearLayout) findViewById(R.id.degree_layout_mp);
        workPlaceView = (TextView) findViewById(R.id.working_place_view_mp);
        positionView = (TextView) findViewById(R.id.job_category_view_mp);
        numText = (EditText) findViewById(R.id.demand_num_view_mp);
        salaryText = (EditText) findViewById(R.id.salary_view_mp);
        conditionView = (TextView) findViewById(R.id.condition_view_mp);
        degreeView = (TextView) findViewById(R.id.degree_view_mp);
        workexpView = (TextView) findViewById(R.id.workexp_view_mp);

        backBtn.setOnClickListener(this);
        releaseBtn.setOnClickListener(this);
        workPlaceLayout.setOnClickListener(this);
        positionLayout.setOnClickListener(this);
        numLayout.setOnClickListener(this);
        conditionLayout.setOnClickListener(this);
        salaryLayout.setOnClickListener(this);
        degreeLayout.setOnClickListener(this);
        workexpLayout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_add_position:
                finish();
                break;
            case R.id.release_btn_mp:
                releaseEvent();
                break;
            case R.id.working_place_layout_mp:
                Intent cityIntent = new Intent(AddPositionPage.this, CityListOfDemand.class);
                startActivityForResult(cityIntent, 0);
                break;
            case R.id.job_category_layout_mp:
                Intent positionIntent = new Intent(AddPositionPage.this, PositionPageOfDemand.class);
                startActivityForResult(positionIntent, 0);
                break;
            case R.id.degree_layout_mp:
                AlertDialog.Builder degreeBuilder = new AlertDialog.Builder(AddPositionPage.this);
                degreeBuilder.setTitle("学历/学位");
                final String[] degree = {"初中", "中专", "高中", "大专", "本科", "硕士", "博士"};
                degreeBuilder.setSingleChoiceItems(degree, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDegree(degree[which]);
                        dialog.dismiss();
                    }
                });
                degreeBuilder.show();
                break;
            case R.id.workexp_layout_mp:
                AlertDialog.Builder workBuilder = new AlertDialog.Builder(AddPositionPage.this);
                workBuilder.setTitle("工作经验");
                final String[] experience = {"无", "1~3年", "3~5年", "5年以上"};
                workBuilder.setSingleChoiceItems(experience, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateExperience(experience[which]);
                        dialog.dismiss();
                    }
                });
                workBuilder.show();
                break;
            case R.id.condition_layout_mp:
                Intent intent = new Intent(AddPositionPage.this,DemandDescPage.class);
                startActivityForResult(intent, 0);
                break;

        }
    }

    private void updateExperience(String exp) {
        workexpView.setText(exp);
    }

    private void updateDegree(String degree) {
        degreeView.setText(degree);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case CityListOfDemand.CITY_RESULT_OK:
                Bundle cityBundle = data.getExtras();
                String city = cityBundle.getString("city");
                updateWorkPlace(city);
                break;
            case PositionPageOfDemand.POSITION_RESULT_OK:
                Bundle positionBundle = data.getExtras();
                String position = positionBundle.getString("position");
                updatePosition(position);
                break;
            case DemandDescPage.CON_DESC_RESULT_OK:
                Bundle busDescBundle = data.getExtras();
                conDesc = busDescBundle.getString("desc");
                updateConDesc(conDesc);
                break;


        }
    }

    private void updateConDesc(String conDesc) {
        conditionView.setText(conDesc);
    }

    public void releaseEvent() {

        PositionInfo info = new PositionInfo();
        info.setUsername(username);
        info.setCity(workPlaceView.getText().toString());
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql =  "select * from companyinfo where username='" + username + "'";
        if (exits("companyinfo")) {
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                company = cursor.getString(cursor.getColumnIndex("company"));
            }
        }
        info.setCompany(company);
        info.setPosition(positionView.getText().toString());
        info.setNum(Integer.parseInt(numText.getText().toString()));
        info.setSalary(Integer.parseInt(salaryText.getText().toString()));
        info.setDegree(degreeView.getText().toString());
        info.setWorkexp(workexpView.getText().toString());
        info.setCondition(conditionView.getText().toString());
        PositionInfoService service = new PositionInfoService(this);
        service.save(info);

        Toast.makeText(AddPositionPage.this, "发布成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddPositionPage.this, MyPositionPage.class));
        finish();
    }

    private void updateWorkPlace(String city) { workPlaceView.setText(city);}
    private void updatePosition(String position) {
        positionView.setText(position);
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


}
