package com.example.peng.graduationproject.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by peng on 2016/4/9.
 */
public class UserInfoManager {

    private static User currentUser = null;

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    public static User getCurrentUser(){
        return currentUser;
    }

/*
    public static User getCurrentUser(Context context){
        User currentUser;
        if (id == null){
            return null;
        }
        currentUser = new User();
        DatabaseHelper database = new DatabaseHelper(context);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.rawQuery("select * from user where id =?",new String[]{id});
        if (c.moveToFirst()){
            currentUser.setId(c.getString(c.getColumnIndex("id")));
            currentUser.setName(c.getString(c.getColumnIndex("name")));
            currentUser.setNumber(c.getString(c.getColumnIndex("number")));
            currentUser.setPassword(c.getString(c.getColumnIndex("password")));
        }
        return currentUser;
    }
*/
}
