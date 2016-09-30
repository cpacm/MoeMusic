package com.cpacm.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cpacm.core.db.dao.AccountDao;
import com.cpacm.core.db.dao.CollectionDao;
import com.cpacm.core.db.dao.CollectionShipDao;
import com.cpacm.core.db.dao.SongDao;

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
        //用户表
        db.execSQL(AccountDao.createTable());
        db.execSQL(AccountDao.createIndex());
        //歌曲表
        db.execSQL(SongDao.createTable());
        db.execSQL(SongDao.createIndex());
        //收藏夹表
        db.execSQL(CollectionDao.createTable());
        //收藏夹关联表
        db.execSQL(CollectionShipDao.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE note ADD COLUMN marktes integer");//增减一项 保存用户数据
        //onCreate(db);
    }
}
