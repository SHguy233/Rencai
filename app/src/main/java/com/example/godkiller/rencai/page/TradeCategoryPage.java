package com.example.godkiller.rencai.page;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/9.
 */
public class TradeCategoryPage extends BaseActivity{

    private ListView tradeLv;
    private String tradeData[][] = {{"1", "IT|通信|电子|互联网"}, {"2","金融业"}, {"3","房地产|建筑业"}, {"4", "商业服务"},
            {"5", "贸易|批发|零售|租赁业"}, {"6", "生产|加工|制造"},{"7", "服务业"}, {"8", "能源|矿产|环保"},
            {"9", "文体教育|工艺美术"},{"0","文化|传媒|娱乐|体育"}};

    private SimpleAdapter tradeAdapter;
    private List<Map<String,String>> tradeList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.trade_category_page);

        tradeLv = (ListView) findViewById(R.id.trade_list);
        for (int i = 0;i < tradeData.length; i++) {
            Map<String,String> tradeMap = new HashMap<String, String>();
            tradeMap.put("id", tradeData[i][0]);
            tradeMap.put("category", tradeData[i][1]);
            tradeList.add(tradeMap);
        }
//        this.tradeAdapter = new SimpleAdapter(this, tradeList,
//                R.layout.trade_list_item, new String[] {"id", "category"},
//                new int[]{R.id.trade_id, R.id.trade_name});
        tradeLv.setAdapter(tradeAdapter);
        tradeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> tradeMap = (Map<String, String>) TradeCategoryPage.this.tradeAdapter.getItem(position);
                String tradeId = tradeMap.get("id");
                showOptions(tradeId);
            }
        });

    }
    private void showOptions(String tradeId) {
        switch (tradeId) {
            case "1" :
                Toast.makeText(TradeCategoryPage.this, "你选了1", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
