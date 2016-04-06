package com.example.godkiller.rencai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static String name = "rencai.db";
    private static int dbVersion = 1;
    public  DatabaseHelper(Context context) {
        super(context, name, null, dbVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(id integer primary key autoincrement,username varchar(20),password varchar(20),identity varchar(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
