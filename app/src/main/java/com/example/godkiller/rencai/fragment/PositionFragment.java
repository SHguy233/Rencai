package com.example.godkiller.rencai.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.DatabaseHelper;
import com.example.godkiller.rencai.page.PositionDetailPage;
import com.example.godkiller.rencai.page.SearchPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class PositionFragment extends Fragment{
    private Button msgBtn;
    private ImageView searchbarView;
    private ListView positionLv;
    private SimpleAdapter positionAdapter;
    private List<Map<String, Object>> dataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_fragment, null);
        final Intent searchIntent = new Intent(getActivity(), SearchPage.class);
        msgBtn = (Button) view.findViewById(R.id.msg_btn);
        searchbarView = (ImageView) view.findViewById(R.id.searchbar_view);
        searchbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().startActivity(searchIntent);
            }
        });
        positionLv = (ListView) view.findViewById(R.id.position_lv_seeker);
        positionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpEvent(position);

            }
        });
        setAdapter(getActivity());
        return view;

    }

    private void jumpEvent(int index) {
        int current = 0;
        SQLiteDatabase db = new DatabaseHelper(getActivity()).getReadableDatabase();
        String company = dataList.get(index).get("company").toString();
        String position = dataList.get(index).get("position").toString();
        String sql =  "select * from positioninfo";
        //查询表中所有符合条件的数据
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String mCompany = cursor.getString(cursor.getColumnIndex("company"));
            String mPosition = cursor.getString(cursor.getColumnIndex("position"));
            if (company.equals(mCompany) && position.equals(mPosition)) {
               current = cursor.getInt(0);
                break;
            }
        }
        Intent intent = new Intent(getActivity(), PositionDetailPage.class);
        Bundle bundle = new Bundle();
        bundle.putInt("positionid", current);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setAdapter(Context context) {
        dataList = getData();
        positionAdapter = new SimpleAdapter(context, dataList, R.layout.position_item_seeker, new String[]{"position", "salary", "company", "city", "degree", "num"},
                new int[]{R.id.position_item_position_seeker, R.id.salary_item_position_seeker, R.id.company_item_position_seeker, R.id.work_city_item_position_seeker, R.id.degree_item_position_seeker, R.id.num_item_position_seeker});
        positionLv.setAdapter(positionAdapter);
    }

    private List<Map<String, Object>> getData() {
            List<Map<String, Object>> positionList = new ArrayList<Map<String, Object>>();
            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String sql =  "select * from positioninfo";
            Cursor cursor = db.rawQuery(sql, null);

            while(cursor.moveToNext()) {
                String position = cursor.getString(cursor.getColumnIndex("position"));
                int salary = cursor.getInt(cursor.getColumnIndex("salary"));
                String company = cursor.getString(cursor.getColumnIndex("company"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String workexp = cursor.getString(cursor.getColumnIndex("workexp"));
                String degree = cursor.getString(cursor.getColumnIndex("degree"));
                int num = cursor.getInt(cursor.getColumnIndex("num"));
                String condition = cursor.getString(cursor.getColumnIndex("condition"));
                Map<String, Object> positionMap = new HashMap<String, Object>();
                positionMap.put("position", position);
                positionMap.put("salary", salary);
                positionMap.put("company", company);
                positionMap.put("city", city);
                positionMap.put("workexp", workexp);
                positionMap.put("degree", degree);
                positionMap.put("num", num);
                positionMap.put("condition", condition);
                positionList.add(positionMap);
            }
            return positionList;
    }
}
