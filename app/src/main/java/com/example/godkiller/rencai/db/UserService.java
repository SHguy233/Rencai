package com.example.godkiller.rencai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Objects;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class UserService {
    private DatabaseHelper databaseHelper;
    public UserService(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean login(String username, String password, String identity) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "select * from user where username=? and password=? and identity=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password, identity});
        if (cursor.moveToFirst() == true) {
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean register(User user) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "insert into user(username,password,identity) values(?,?,?)";
        Object obj[] = {user.getUsername(), user.getPassword(), user.getIdentity()};
        db.execSQL(sql, obj);
        return true;
    }

    public void modifyPw(String username,String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "update user set password='" + password + "' where username='" + username + "'";
        db.execSQL(sql);
    }

}
