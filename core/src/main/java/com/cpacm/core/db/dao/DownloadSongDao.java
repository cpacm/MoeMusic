package com.cpacm.core.db.dao;

import com.cpacm.core.db.DBHelper;

/**
 * @author: cpacm
 * @date: 2016/9/10
 * @desciption: 下载歌曲数据表
 */
public class DownloadSongDao extends BaseDao {

    private static final String TABLE = "";

    private final static String INDEX = "unique_index_uid";
    private final static String COLUMN_UID = "uid";
    private final static String COLUMN_USERNAME = "username";
    private final static String COLUMN_NICKNAME = "nickname";
    private final static String COLUMN_REGISTERED = "registered";
    private final static String COLUMN_URL = "url";
    private final static String COLUMN_FM_URL = "fm_url";
    private final static String COLUMN_AVATAR_SMALL = "avatar_small";
    private final static String COLUMN_AVATAR_MEDIUM = "avatar_medium";
    private final static String COLUMN_AVATAR_LARGE = "avatar_large";
    private final static String COLUMN_GROUPS = "groups";
    private final static String COLUMN_FOLLOWER = "follower";
    private final static String COLUMN_FOLLOWING = "following";
    private final static String COLUMN_MSG = "msg";
    private final static String COLUMN_ABOUT = "about";

    /**
     * 建表sql
     *
     * @return sql
     */
    public static String createTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + TABLE + "(");
        sb.append(COLUMN_UID + " INTEGER PRIMARY KEY,");
        sb.append(COLUMN_USERNAME + " varchar(50),");
        sb.append(COLUMN_NICKNAME + " varchar(50), ");
        sb.append(COLUMN_REGISTERED + " BIGINT,");
        sb.append(COLUMN_URL + " TEXT,");
        sb.append(COLUMN_FM_URL + " TEXT,");
        sb.append(COLUMN_AVATAR_SMALL + " TEXT,");
        sb.append(COLUMN_AVATAR_MEDIUM + " TEXT,");
        sb.append(COLUMN_AVATAR_LARGE + " TEXT,");
        sb.append(COLUMN_GROUPS + " TEXT,");
        sb.append(COLUMN_FOLLOWER + " TEXT,");
        sb.append(COLUMN_FOLLOWING + " TEXT,");
        sb.append(COLUMN_MSG + " INTEGER,");
        sb.append(COLUMN_ABOUT + " TEXT");
        sb.append(");");
        return sb.toString();
    }
}
