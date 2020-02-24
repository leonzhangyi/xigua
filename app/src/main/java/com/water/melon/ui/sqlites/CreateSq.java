package com.water.melon.ui.sqlites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateSq extends SQLiteOpenHelper {


    public static final String XG_TABLE_NAME = "xgdomain";
    public static final String XG_DOMAIN = "domain";
    public static final String DATABASE_NAME = "xgdatabase.db";

    public CreateSq(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // TODO Auto- constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table " + XG_TABLE_NAME + " (id integer primary key autoincrement,domain varchar(30))");//LS:url,times  是根据历史记录添加的字段
//		db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }


}