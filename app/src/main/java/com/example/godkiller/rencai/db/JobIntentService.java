package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class JobIntentService {
    private  DatabaseHelper databaseHelper;
    public JobIntentService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(JobIntent jobIntent) {
        String sql = "create table if not exists jobintention(id integer primary key autoincrement,username varchar(20),workplace varchar(20),tradecategory varchar(20),positioncategory varchar(20),salary integer(20))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into jobintention(username,workplace,tradecategory,positioncategory,salary) values(?,?,?,?,?)";
        Object obj[] = {jobIntent.getUsername(), jobIntent.getWorkPlace(),jobIntent.getTradeCategory(),jobIntent.getPositionCategory(),jobIntent.getSalary()};
        db.execSQL(insertSql, obj);
    }
    public void update(JobIntent jobIntent) {
        String sql = "update jobintention set workplace=?,tradecategory=?,positioncategory=?,salary=? where username=?";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql, new Object[]{jobIntent.getWorkPlace(),jobIntent.getTradeCategory(),jobIntent.getPositionCategory(),jobIntent.getSalary(),jobIntent.getUsername()});
    }

}
