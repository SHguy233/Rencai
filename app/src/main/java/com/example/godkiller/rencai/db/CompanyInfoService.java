package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class CompanyInfoService {
    private  DatabaseHelper databaseHelper;
    public CompanyInfoService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(CompanyInfo companyInfo) {
        String sql = "create table if not exists companyinfo(id integer primary key autoincrement,username varchar(20),company varchar(20),trade varchar(20),nature varchar(20),scale varchar(20),address varchar(50),business varchar(200))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into companyinfo(username,company,trade,nature,scale,address,business) values(?,?,?,?,?,?,?)";
        Object obj[] = {companyInfo.getUsername(),companyInfo.getCompany(),companyInfo.getTrade(),companyInfo.getNature(),companyInfo.getScale(),companyInfo.getAddress(),companyInfo.getBusiness()};
        db.execSQL(insertSql, obj);
    }
    public void update(CompanyInfo companyInfo) {
        String sql = "update companyinfo set company=?,trade=?,nature=?,scale=?,address=?,business=? where username=?";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql, new Object[]{companyInfo.getCompany(),companyInfo.getTrade(),companyInfo.getNature(),companyInfo.getScale(),companyInfo.getAddress(),companyInfo.getBusiness(),companyInfo.getUsername()});
    }

}
