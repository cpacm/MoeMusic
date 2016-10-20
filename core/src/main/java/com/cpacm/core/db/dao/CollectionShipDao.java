package com.cpacm.core.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.cpacm.core.bean.CollectionShipBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/11
 * @desciption: 收藏夹歌曲关系表
 */
public class CollectionShipDao extends BaseDao {

    private static final String TABLE = "COLLECTIONSHIP";

    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_CID = "cid";
    private final static String COLUMN_SID = "sid";

    /**
     * 建表sql
     *
     * @return sql
     */
    public static String createTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + TABLE + "(");
        sb.append(COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLUMN_CID + " INTEGER,");
        sb.append(COLUMN_SID + " INTEGER,");
        sb.append("unique (" + COLUMN_CID + "," + COLUMN_SID + ")");
        sb.append(");");
        return sb.toString();
    }

    /**
     * 获取收藏夹上的所有歌曲
     *
     * @return collectionList
     */
    public List<CollectionShipBean> queryByCid(int cid) {
        String selection = COLUMN_CID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(cid)};
        List<CollectionShipBean> collectionList = new ArrayList<>();
        Cursor cursor = query(TABLE, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            collectionList.add(getCollectionShip(cursor));
        }
        cursor.close();
        return collectionList;
    }

    public CollectionShipBean queryByCidAndSid(int cid,int sid){
        String selection = COLUMN_CID + "=? and "+COLUMN_SID +"=?";
        String[] selectionArgs = new String[]{String.valueOf(cid),String.valueOf(sid)};
        Cursor cursor = query(TABLE, null, selection, selectionArgs, null, null, null);
        CollectionShipBean bean = null;
        if(cursor.moveToNext()){
            bean =  getCollectionShip(cursor);
        }
        cursor.close();
        return bean;
    }

    /**
     * 插入一条收藏夹关联记录
     *
     * @param collectionBean
     */
    public long insertCollectionShip(CollectionShipBean collectionBean) {
        return insert(TABLE, null, getCollectionContent(collectionBean));
    }

    /**
     * 更新一条收藏夹关联信息
     *
     * @param collectionBean
     */
    public int updateCollection(CollectionShipBean collectionBean) {
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = new String[]{collectionBean.getId() + ""};
        return update(TABLE, getCollectionContent(collectionBean), whereClause, whereArgs);
    }

    /**
     * 删除一条收藏夹关联信息
     *
     * @param id
     */
    public int deleteCollection(int id) {
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = new String[]{id + ""};
        return delete(TABLE, whereClause, whereArgs);
    }

    /**
     * 删除一条收藏夹关联信息
     *
     * @param cid
     * @param sid
     * @return
     */
    public int deleteCollection(int cid,int sid) {
        String whereClause = COLUMN_CID + "=? and "+COLUMN_SID+"=?";
        String[] whereArgs = new String[]{String.valueOf(cid),String.valueOf(sid)};
        return delete(TABLE, whereClause, whereArgs);
    }


    private CollectionShipBean getCollectionShip(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        int cid = cursor.getInt(cursor.getColumnIndex(COLUMN_CID));
        int sid = cursor.getInt(cursor.getColumnIndex(COLUMN_SID));
        CollectionShipBean collectionShip = new CollectionShipBean(id, cid, sid);
        return collectionShip;
    }

    public ContentValues getCollectionContent(CollectionShipBean collectionShip) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CID, collectionShip.getCid());
        values.put(COLUMN_SID, collectionShip.getSid());
        return values;
    }

}
