package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class EdubgdService {
    private  DatabaseHelper databaseHelper;
    public EdubgdService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(EduBgd eduBgd) {
        String sql = "create table if not exists edubgd(id integer primary key autoincrement,username varchar(20),college varchar(20),enroll varchar(20),graduate varchar(20),major varchar(20), degree varchar(20))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into edubgd(username,college,enroll,graduate,major,degree) values(?,?,?,?,?,?)";
        Object obj[] = {eduBgd.getUsername(), eduBgd.getCollege(), eduBgd.getEnrollDate(), eduBgd.getGraduateDate(), eduBgd.getMajor(), eduBgd.getDegree()};
        db.execSQL(insertSql, obj);
    }

}
