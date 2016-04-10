package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class PersonalInfoService {
    private  DatabaseHelper databaseHelper;
    public PersonalInfoService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(PersonalInfo personalInfo) {
        String sql = "create table if not exists personalinfo(id integer primary key autoincrement,username varchar(20),name varchar(20),gender varchar(20),birth varchar(20),workexptime varchar(20),acceptRemote varchar(20),phone varchar(20))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into personalinfo(username,name,gender,birth,workexptime,acceptRemote,phone) values(?,?,?,?,?,?,?)";
        Object obj[] = {personalInfo.getUsername(), personalInfo.getName(),personalInfo.getGender(),personalInfo.getBirth(),personalInfo.getWorkExpTime(),personalInfo.getAcceptRemote(),personalInfo.getPhone()};
        db.execSQL(insertSql, obj);
    }
    public void update(PersonalInfo personalInfo) {
        String sql = "update personalinfo set name=?,gender=?,birth=?,workexptime=?,acceptRemote=?,phone=? where username=?";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql, new Object[]{personalInfo.getName(),personalInfo.getGender(),personalInfo.getBirth(),personalInfo.getWorkExpTime(),personalInfo.getAcceptRemote(),personalInfo.getPhone(),personalInfo.getUsername()});
    }

}
