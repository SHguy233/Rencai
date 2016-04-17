package com.example.godkiller.rencai.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class PositionDetailFragment extends Fragment {
    private int id;
    private TextView positionView;
    private TextView salaryView;
    private TextView companyView;
    private TextView cityView;
    private TextView expView;
    private TextView degreeView;
    private TextView numView;
    private TextView descView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_detail_fragment, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        id = bundle.getInt("positionid");
        Toast.makeText(getActivity(), id+ "", Toast.LENGTH_SHORT).show();
        positionView = (TextView) view.findViewById(R.id.position_item_position_fra);
        salaryView = (TextView) view.findViewById(R.id.salary_item_position_fra);
        companyView = (TextView) view.findViewById(R.id.company_item_view_fra);
        cityView = (TextView) view.findViewById(R.id.work_city_item_position_fra);
        expView = (TextView) view.findViewById(R.id.workexp_item_position_fra);
        degreeView = (TextView) view.findViewById(R.id.degree_item_position_fra);
        numView = (TextView) view.findViewById(R.id.num_item_position_fra);
        descView = (TextView) view.findViewById(R.id.desc_position_fra);
        initData();
        return view;
    }


    private void initData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "select * from positioninfo where id=" + Integer.toString(id);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        String position = cursor.getString(cursor.getColumnIndex("position"));
        int salary = cursor.getInt(cursor.getColumnIndex("salary"));
        String company = cursor.getString(cursor.getColumnIndex("company"));
        String city = cursor.getString(cursor.getColumnIndex("city"));
        String workexp = cursor.getString(cursor.getColumnIndex("workexp"));
        String degree = cursor.getString(cursor.getColumnIndex("degree"));
        int num = cursor.getInt(cursor.getColumnIndex("num"));
        String condition = cursor.getString(cursor.getColumnIndex("condition"));
        positionView.setText(position);
        salaryView.setText(salary+"");
        companyView.setText(company);
        cityView.setText(city);
        expView.setText(workexp);
        degreeView.setText(degree);
        numView.setText(num+"");
        descView.setText(condition);
    }

}
