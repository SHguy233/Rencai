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
import com.example.godkiller.rencai.db.CompanyInfo;
import com.example.godkiller.rencai.db.CompanyInfoService;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.db.EduBgd;
import com.example.godkiller.rencai.db.EdubgdService;
import com.example.godkiller.rencai.trade.TradeCategoryOfCompany;
import com.example.godkiller.rencai.trade.TradeCategoryOfIntention;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class CompanyInfoEditPage extends BaseActivity implements View.OnClickListener{
    private Button backBtn;
    private Button saveBtn;
    private EditText companyText;
    private EditText scaleText;
    private EditText addressText;
    private LinearLayout tradeLayout;
    private LinearLayout natureLayout;
    private LinearLayout addressLayout;
    private LinearLayout businessLayout;
    private TextView tradeView;
    private TextView natureView;
    private TextView addressView;
    private TextView businessView;
    private String address;
    private String busDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.company_info_edit_page);

        backBtn = (Button) findViewById(R.id.back_button_company_info_edit);
        saveBtn = (Button) findViewById(R.id.save_btn_ci);
        companyText = (EditText) findViewById(R.id.company_name_edit_text);
        scaleText = (EditText) findViewById(R.id.scale_edit_text);
        tradeLayout = (LinearLayout) findViewById(R.id.trade_category_layout_ci);
        natureLayout = (LinearLayout) findViewById(R.id.company_category_layout_ci);
        addressLayout = (LinearLayout) findViewById(R.id.company_address_layout_ci);
        businessLayout = (LinearLayout) findViewById(R.id.business_desc_layout_ci);
        tradeView = (TextView) findViewById(R.id.trade_category_view_ci);
        natureView = (TextView) findViewById(R.id.company_nature_view_ci);
        addressView = (TextView) findViewById(R.id.company_address_view_ci);
        businessView = (TextView) findViewById(R.id.business_desc_view_ci);
        addressText = new EditText(this);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        tradeLayout.setOnClickListener(this);
        natureLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        businessLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_company_info_edit:
                finish();
                break;
            case R.id.save_btn_ci:
                saveEvent();
                break;
            case R.id.trade_category_layout_ci:
                Intent tradeIntent = new Intent(CompanyInfoEditPage.this, TradeCategoryOfCompany.class);
                startActivityForResult(tradeIntent, 0);
                break;
            case R.id.company_category_layout_ci:
                AlertDialog.Builder natureBuilder = new AlertDialog.Builder(CompanyInfoEditPage.this);
                natureBuilder.setTitle("性质");
                final String[] nature = {"国有企业", "集体所有制企业", "联营企业", "三资企业", "私营企业", "其他企业"};
                natureBuilder.setSingleChoiceItems(nature, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateNature(nature[which]);
                        dialog.dismiss();
                    }
                });
                natureBuilder.show();
                break;
            case R.id.company_address_layout_ci:
                AlertDialog.Builder addressBuilder = new AlertDialog.Builder(CompanyInfoEditPage.this)
                        .setTitle("公司地址")
                        .setView(addressText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                address = addressText.getText().toString();
                                updateAddress(address);
                            }
                        });
                addressBuilder.show();
                break;
            case R.id.business_desc_layout_ci:
                Intent intent = new Intent(CompanyInfoEditPage.this, BusinessDescPage.class);
                startActivityForResult(intent, 0);
                break;

            default:
                break;

        }
    }



    public void saveEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        CompanyInfo info = new CompanyInfo();
        info.setUsername(username);
        info.setCompany(companyText.getText().toString());
        info.setTrade(tradeView.getText().toString());
        info.setNature(natureView.getText().toString());
        info.setScale(scaleText.getText().toString());
        info.setAddress(addressView.getText().toString());
        info.setBusiness(busDesc);
        CompanyInfoService service = new CompanyInfoService(this);
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String sql =  "select * from companyinfo where username='" + username + "'";
        if (exits("companyinfo")) {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() == 0){
                service.save(info);
                Toast.makeText(CompanyInfoEditPage.this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                service.update(info);
                Toast.makeText(CompanyInfoEditPage.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            service.save(info);
            Toast.makeText(CompanyInfoEditPage.this, "保存成功", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case TradeCategoryOfIntention.TRADE_RESULT_OK:
                Bundle tradeBundle = data.getExtras();
                String trade = tradeBundle.getString("trade");
                updateTrade(trade);
                break;
            case BusinessDescPage.BUS_DESC_RESULT_OK:
                Bundle busDescBundle = data.getExtras();
                busDesc = busDescBundle.getString("desc");
                updateBusDesc(busDesc);
                break;
        }
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

    private void updateAddress(String address) {
        addressView.setText(address);
    }
    private void updateNature(String nature) {
        natureView.setText(nature);
    }
    private void updateTrade(String trade) {
        tradeView.setText(trade);
    }
    private void updateBusDesc(String busDesc) {
        businessView.setText(busDesc);
    }
}
