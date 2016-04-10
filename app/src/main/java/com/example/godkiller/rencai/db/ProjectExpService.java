package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class ProjectExpService {
    private  DatabaseHelper databaseHelper;
    public ProjectExpService(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public void save(ProjectExp projectExp) {
        String sql = "create table if not exists projectexp(id integer primary key autoincrement,username varchar(20),project varchar(20),start varchar(20),finish varchar(20),desc varchar(100))";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.execSQL(sql);
        String insertSql = "insert into projectexp(username,project,start,finish,desc) values(?,?,?,?,?)";
        Object obj[] = {projectExp.getUsername(), projectExp.getProject(), projectExp.getStartDate(), projectExp.getFinishDate(), projectExp.getPrjectDesc()};
        db.execSQL(insertSql, obj);
    }

}
