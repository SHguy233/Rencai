package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class WorkExpService {
    private  DatabaseHelper databaseHelper;
    public WorkExpService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(WorkExp workExp) {
        String sql = "create table if not exists workexp(id integer primary key autoincrement,username varchar(20),company varchar(20),entry varchar(20),leave varchar(20),trade varchar(20),position varchar(20),desc varchar(100))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into workexp(username,company,entry,leave,trade,position,desc) values(?,?,?,?,?,?,?)";
        Object obj[] = {workExp.getUsername(), workExp.getCompany(), workExp.getEntryDate(), workExp.getLeaveDate(), workExp.getTrade(), workExp.getPosition(), workExp.getJobDesc()};
        db.execSQL(insertSql, obj);
    }

}
