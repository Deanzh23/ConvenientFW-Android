package com.dean.android.famework.convenient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 架构层真正的操作数据库的类
 * <p/>
 * Created by Dean on 16/5/24.
 */
public class FrameworkDatabaseHelper extends SQLiteOpenHelper {

    public FrameworkDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
