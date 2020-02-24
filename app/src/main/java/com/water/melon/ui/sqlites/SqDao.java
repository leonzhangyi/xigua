package com.water.melon.ui.sqlites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SqDao {

    private CreateSq createsq;
    Context context;

    public SqDao(Context context) {
        this.context = context;
        createsq = new CreateSq(context);
    }

    // 添加�?条记录到数据�?
    public void add(String domain) {
        SQLiteDatabase db = createsq.getWritableDatabase();
        db.execSQL("insert into " + CreateSq.XG_TABLE_NAME + " (" + CreateSq.XG_DOMAIN + ",id) values (?,1)",
                new Object[]{domain});
        db.close();
    }

    // 修改
    public void update(String domain) {
        SQLiteDatabase db = createsq.getWritableDatabase();
        db.execSQL("update " + CreateSq.XG_TABLE_NAME + " set " + CreateSq.XG_DOMAIN + "=? where id=?", new Object[]{
                domain, 1});
        db.close();
    }

    // 查询 name �?
    public String getDomain() {
        String liuliangs = null;
        SQLiteDatabase db = createsq.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CreateSq.XG_TABLE_NAME + " where id=?",
                new String[]{"1"});
        while (cursor.moveToNext()) {
            String names = cursor.getString(cursor.getColumnIndex(CreateSq.XG_DOMAIN));
            liuliangs = names;

        }
        // boolean result=cursor.moveToNext();
        cursor.close();
        db.close();

        return liuliangs;

    }
}
