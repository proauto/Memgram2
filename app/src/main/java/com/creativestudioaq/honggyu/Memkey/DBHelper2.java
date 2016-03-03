package com.creativestudioaq.honggyu.Memkey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper2 extends SQLiteOpenHelper {


    public DBHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE category (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   category TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS category");
        onCreate(db);
    }
}