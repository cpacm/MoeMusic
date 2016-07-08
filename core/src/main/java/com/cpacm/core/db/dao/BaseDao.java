package com.cpacm.core.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpacm.core.CoreApplication;
import com.cpacm.core.db.DBHelper;

/**
 * @author: cpacm
 * @date: 2016/7/8
 * @desciption: 基础dao层
 */
public abstract class BaseDao {

    protected SQLiteDatabase db;
    protected DBHelper dh;

    public BaseDao() {
        dh = new DBHelper(CoreApplication.getInstance().getApplicationContext());
        db = dh.getWritableDatabase();
    }

    public void close() {
        db.close();
        dh.close();
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        return c;
    }

    public long insert (String table, String nullColumnHack, ContentValues values){
        return db.insert(table,nullColumnHack,values);
    }


    public int delete(String table, String whereClause, String[] whereArgs){
        return db.delete(table,whereClause,whereArgs);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        return db.update(table,values,whereClause,whereArgs);
    }

    /**
     * 数据替换，原理是先删除存在的整行数据后在重新插入
     * 需要先指定索引才能使用
     * @param table
     * @param nullColumnHack
     * @param initialValues
     * @return
     */
    public long replace(String table, String nullColumnHack, ContentValues initialValues){
        return db.replace(table,nullColumnHack,initialValues);
    }


}
