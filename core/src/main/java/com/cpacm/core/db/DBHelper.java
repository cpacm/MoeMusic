package com.cpacm.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * Created by Administrator on 2015/6/8.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String INFORMATIONTABLE = "UserInformation";
    public static final String MESSAGETABLE = "UserMessage";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立用户信息表
        createInformationTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createInformationTable(SQLiteDatabase db){
        db.execSQL("create table UserInformation(_id integer primary key autoincrement,name varchar(20),status varchar(50),pic varchar(20))");
    }
}
