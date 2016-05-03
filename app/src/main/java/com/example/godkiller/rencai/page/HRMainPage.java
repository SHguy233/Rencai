package com.example.godkiller.rencai.page;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.ActivityCollector;

public class HRMainPage extends Activity {
    private ListView drawerLv;
    private DrawerLayout drawerLayout;
    private ArrayList<String> menuList;
    private ArrayAdapter<String> menuAdapter;
    private long exitTime = 0;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_main_page);
        initSlidingMenu();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        if (username == null) {
            Toast.makeText(HRMainPage.this, "身份已过期，请重新登录！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSlidingMenu(){
        drawerLv = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = new ArrayList<String>();
        menuList.add("我的职位");
        menuList.add("我的简历");
        menuList.add("公司信息");
        menuList.add("我的消息");
        menuList.add("帐号信息");
        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        drawerLv.setAdapter(menuAdapter);
        drawerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(HRMainPage.this, PositionPage.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(HRMainPage.this, CompanyInfoPage.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(HRMainPage.this, MyAccountPage.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(drawerLv);
            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }



}
