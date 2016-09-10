package com.cpacm.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cpacm.core.db.dao.AccountDao;

/**
 * 数据库帮助类
 * Created by cpacm on 2016/6/8.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "beats.db";
    private static final int DATABASEVERSION = 1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountDao.createTable());
        db.execSQL(AccountDao.createIndex());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE note ADD COLUMN marktes integer");//增减一项 保存用户数据
        //onCreate(db);
    }
}
