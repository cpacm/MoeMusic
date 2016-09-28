package com.cpacm.core.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.cpacm.core.bean.CollectionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/11
 * @desciption: 收藏夹数据表
 */
public class CollectionDao extends BaseDao {

    private static final String TABLE = "COLLECTION";

    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_TITLE = "title";
    private final static String COLUMN_COVER_URL = "cover_url";
    private final static String COLUMN_DESCRIPTION = "description";
    private final static String COLUMN_COUNT = "count";

    /**
     * 建表sql
     *
     * @return sql
     */
    public static String createTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + TABLE + "(");
        sb.append(COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLUMN_TITLE + " varchar(100),");
        sb.append(COLUMN_COVER_URL + " varchar(200),");
        sb.append(COLUMN_DESCRIPTION + " TEXT,");
        sb.append(COLUMN_COUNT + " INTEGER");
        sb.append(");");
        return sb.toString();
    }

    /**
     * 获取表上的所有收藏夹
     *
     * @return collectionList
     */
    public List<CollectionBean> queryAll() {
        List<CollectionBean> collectionList = new ArrayList<>();
        Cursor cursor = query(TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            collectionList.add(getCollection(cursor));
        }
        cursor.close();
        return collectionList;
    }

    /**
     * 获取表上的收藏夹
     *
     * @return 收藏夹
     */
    public CollectionBean query(int id) {
        CollectionBean bean = null;
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        List<CollectionBean> collectionList = new ArrayList<>();
        Cursor cursor = query(TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToNext()) {
            bean = getCollection(cursor);
        }
        cursor.close();
        return bean;
    }


    /**
     * 插入一条收藏夹记录
     *
     * @param collectionBean
     */
    public long insertCollection(CollectionBean collectionBean) {
        return insert(TABLE, null, getCollectionContent(collectionBean));
    }

    /**
     * 更新一条收藏夹信息
     *
     * @param collectionBean
     */
    public int updateCollection(CollectionBean collectionBean) {
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = new String[]{collectionBean.getId() + ""};
        return update(TABLE, getCollectionContent(collectionBean), whereClause, whereArgs);
    }

    /**
     * 删除一条收藏夹信息
     *
     * @param collectionBean
     */
    public void deleteCollection(CollectionBean collectionBean) {
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = new String[]{collectionBean.getId() + ""};
        delete(TABLE, whereClause, whereArgs);
    }

    private CollectionBean getCollection(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
        String coverUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_URL));
        int count = cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT));
        CollectionBean collection = new CollectionBean(id, title, coverUrl, count, description);
        return collection;
    }

    public ContentValues getCollectionContent(CollectionBean collection) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, collection.getTitle());
        values.put(COLUMN_COVER_URL, collection.getCoverUrl());
        values.put(COLUMN_DESCRIPTION, collection.getDescription());
        values.put(COLUMN_COUNT, collection.getCount());
        return values;
    }
}
