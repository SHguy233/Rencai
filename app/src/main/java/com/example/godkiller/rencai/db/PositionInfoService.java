package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class PositionInfoService {
    private  DatabaseHelper databaseHelper;
    public PositionInfoService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(PositionInfo positionInfo) {
        String sql = "create table if not exists positioninfo(id integer primary key autoincrement,username varchar(20),company varchar(20),city varchar(20),position varchar(20),num integer(20),salary integer(20),degree varchar(20),workexp varchar(20),condition varchar(100))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into positioninfo(username,company,city,position,num,salary,degree,workexp,condition) values(?,?,?,?,?,?,?,?,?)";
        Object obj[] = {positionInfo.getUsername(),positionInfo.getCompany(),positionInfo.getCity(),positionInfo.getPosition(),positionInfo.getNum(),positionInfo.getSalary(),positionInfo.getDegree(),positionInfo.getWorkexp(),positionInfo.getCondition()};
        db.execSQL(insertSql, obj);
    }

}
