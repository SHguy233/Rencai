package com.example.godkiller.rencai.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.DatabaseHelper;

/**
 * Created by GodKiller on 2016/3/6.
 */
public class CompanyDetailFragment extends Fragment {
    private int id;
    private TextView companyView;
    private TextView tradeView;
    private TextView natureView;
    private TextView scaleView;
    private TextView addressView;
    private TextView descView;
    private Button followBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_detail_fragment, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        id = bundle.getInt("positionid");
        companyView = (TextView) view.findViewById(R.id.company_ci_fra);
        tradeView = (TextView) view.findViewById(R.id.trade_text_fra);
        scaleView = (TextView) view.findViewById(R.id.scale_text_fra);
        addressView = (TextView) view.findViewById(R.id.address_text_fra);
        descView = (TextView) view.findViewById(R.id.company_desc_text_fra);
        natureView = (TextView) view.findViewById(R.id.nature_text_fra);
        initData();
        return view;
    }

    private void initData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "select * from positioninfo where id=" + Integer.toString(id);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        String company = cursor.getString(cursor.getColumnIndex("company"));
        String sql2 = "select * from companyinfo where company='" + company + "'";
        Cursor cursor2 = db.rawQuery(sql2, null);
        cursor2.moveToNext();
        String trade = cursor2.getString(cursor2.getColumnIndex("trade"));
        String scale = cursor2.getString(cursor2.getColumnIndex("scale"));
        String address = cursor2.getString(cursor2.getColumnIndex("address"));
        String desc = cursor2.getString(cursor2.getColumnIndex("business"));
        String nature = cursor2.getString(cursor2.getColumnIndex("nature"));
        companyView.setText(company);
        tradeView.setText(trade);
        scaleView.setText(scale);
        addressView.setText(address);
        descView.setText(desc);
        natureView.setText(nature);

    }


}
