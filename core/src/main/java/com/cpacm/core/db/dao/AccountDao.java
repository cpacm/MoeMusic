package com.cpacm.core.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.bean.CoverBean;

/**
 * @author: cpacm
 * @date: 2016/7/8
 * @desciption: 用户信息数据表
 */
public class AccountDao extends BaseDao {

    private static final String TABLE = "ACCOUNT";

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

    /**
     * 建立索引
     *
     * @return sql
     */
    public static String createIndex() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE UNIQUE INDEX " + INDEX + " ON ");
        sb.append(TABLE + " (" + COLUMN_UID + ")");
        return sb.toString();
    }

    public AccountBean query(int uid) {
        String selection = COLUMN_UID + "=?";
        String[] selectionArgs = new String[]{uid + ""};

        Cursor cursor = query(TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();//将游标移动到第一条数据，使用前必须调用
        AccountBean accountBean = getAccountBean(cursor);
        cursor.close();
        return accountBean;
    }

    public AccountBean getAccountBean(Cursor cursor) {

        AccountBean accountBean = new AccountBean();
        CoverBean userAvatarBean = new CoverBean();
        int uid = cursor.getInt(cursor.getColumnIndex(COLUMN_UID));
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String nickname = cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME));
        long registered = cursor.getLong(cursor.getColumnIndex(COLUMN_REGISTERED));
        String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
        String fm_url = cursor.getString(cursor.getColumnIndex(COLUMN_FM_URL));
        String avatar_small = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR_SMALL));
        String avatar_medium = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR_MEDIUM));
        String avatar_large = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR_LARGE));
        String groups = cursor.getString(cursor.getColumnIndex(COLUMN_GROUPS));
        String follower = cursor.getString(cursor.getColumnIndex(COLUMN_FOLLOWER));
        String following = cursor.getString(cursor.getColumnIndex(COLUMN_FOLLOWING));
        int msg = cursor.getInt(cursor.getColumnIndex(COLUMN_MSG));
        String about = cursor.getString(cursor.getColumnIndex(COLUMN_ABOUT));

        accountBean.setUid(uid);
        accountBean.setUser_name(username);
        accountBean.setUser_nickname(nickname);
        accountBean.setUser_registered(registered);
        accountBean.setUser_url(url);
        accountBean.setUser_fm_url(fm_url);
        userAvatarBean.setSmall(avatar_small);
        userAvatarBean.setMedium(avatar_medium);
        userAvatarBean.setLarge(avatar_large);
        accountBean.setUser_avatar(userAvatarBean);
        accountBean.setGroups(groups);
        accountBean.setFollower(follower);
        accountBean.setFollowing(following);
        accountBean.setMsg(msg);
        accountBean.setAbout(about);

        return accountBean;
    }

    public void updateAccount(AccountBean accountBean) {
        replace(TABLE, null, setAccountBean(accountBean));
    }

    public ContentValues setAccountBean(AccountBean accountBean) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, accountBean.getUid());
        values.put(COLUMN_USERNAME, accountBean.getUser_name());
        values.put(COLUMN_NICKNAME, accountBean.getUser_nickname());
        values.put(COLUMN_REGISTERED, accountBean.getUser_registered());
        values.put(COLUMN_URL, accountBean.getUser_url());
        values.put(COLUMN_FM_URL, accountBean.getUser_fm_url());
        values.put(COLUMN_AVATAR_SMALL, accountBean.getUser_avatar().getSmall());
        values.put(COLUMN_AVATAR_MEDIUM, accountBean.getUser_avatar().getMedium());
        values.put(COLUMN_AVATAR_LARGE, accountBean.getUser_avatar().getLarge());
        values.put(COLUMN_GROUPS, accountBean.getGroups());
        values.put(COLUMN_FOLLOWER, accountBean.getFollower());
        values.put(COLUMN_FOLLOWING, accountBean.getFollowing());
        values.put(COLUMN_MSG, accountBean.getMsg());
        values.put(COLUMN_ABOUT, accountBean.getAbout());
        return values;
    }
}
