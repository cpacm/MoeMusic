package com.cpacm.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by Cpacm on 2015/6/8.
 */
public class DBManager {
    private Context mContext=null;
    private SQLiteDatabase mSQLiteDatabase = null;//用于操作数据库的对象
    private DBHelper dh = null;

    private String dbName ="moe";//数据库名称
    private int dbVersion = 1;//数据库版本
    public DBManager(Context context){
        mContext = context;
    }

    public void open(){
        try{
            dh = new DBHelper(mContext,dbName,null,dbVersion);
            mSQLiteDatabase = dh.getWritableDatabase();
        }catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    public void close(){
        mSQLiteDatabase.close();
        dh.close();
    }

}
