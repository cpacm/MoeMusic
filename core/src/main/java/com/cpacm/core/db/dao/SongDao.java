package com.cpacm.core.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/10
 * @desciption: 本地歌曲数据表
 */
public class SongDao extends BaseDao {

    private static final String TABLE = "SONG";

    private final static String INDEX = "unique_index_sid";
    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_SID = "sid";//正数表示为萌否歌曲，负数表示为本地歌曲
    private final static String COLUMN_TITLE = "title";
    private final static String COLUMN_ALBUM_ID = "album_id";
    private final static String COLUMN_ALBUM_NAME = "album_name";
    private final static String COLUMN_ARTIST_ID = "artist_id";
    private final static String COLUMN_ARTIST_NAME = "artist_name";
    private final static String COLUMN_URL = "url";
    private final static String COLUMN_SIZE = "size";
    private final static String COLUMN_DURATION = "duration";
    private final static String COLUMN_DATE = "date";
    private final static String COLUMN_QUALITY = "quality";
    private final static String COLUMN_TRACK_NUMBER = "track_number";
    private final static String COLUMN_DESCRIPTION = "description";
    private final static String COLUMN_COVER_URL = "cover_url";
    private final static String COLUMN_DOWNLOAD = "download";//0：代表未下载；1：代表下载完成；2：代表下载正在进行中
    private final static String COLUMN_PATH = "path";//存储路径

    /**
     * 建表sql
     *
     * @return sql
     */
    public static String createTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + TABLE + "(");
        sb.append(COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLUMN_SID + " BIGINT,");
        sb.append(COLUMN_TITLE + " varchar(100),");
        sb.append(COLUMN_ALBUM_ID + " BIGINT, ");
        sb.append(COLUMN_ALBUM_NAME + " varchar(100),");
        sb.append(COLUMN_ARTIST_ID + " BIGINT,");
        sb.append(COLUMN_ARTIST_NAME + " varchar(100),");
        sb.append(COLUMN_URL + " TEXT,");
        sb.append(COLUMN_SIZE + " INTEGER,");
        sb.append(COLUMN_DURATION + " INTEGER,");
        sb.append(COLUMN_DATE + " BIGINT,");
        sb.append(COLUMN_QUALITY + " varchar(20),");
        sb.append(COLUMN_TRACK_NUMBER + " INTEGER,");
        sb.append(COLUMN_DESCRIPTION + " TEXT,");
        sb.append(COLUMN_COVER_URL + " TEXT,");
        sb.append(COLUMN_DOWNLOAD + " INTEGER,");
        sb.append(COLUMN_PATH + " TEXT");
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
        sb.append(TABLE + " (" + COLUMN_SID + ")");
        return sb.toString();
    }

    /**
     * 获取表上的所有歌曲
     *
     * @return 歌曲列表
     */
    public List<Song> queryAll() {
        List<Song> songs = new ArrayList<>();
        Cursor cursor = query(TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            songs.add(getSong(cursor));
        }
        cursor.close();
        return songs;
    }

    /**
     * 判断歌曲是否下载
     *
     * @param sid
     * @return
     */
    public Song queryIsDownloaded(int sid) {
        String selection = COLUMN_SID + "=? and " + COLUMN_DOWNLOAD + "=?";
        String[] selectionArgs = new String[]{sid + "", "1"};

        Cursor cursor = query(TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();//将游标移动到第一条数据，使用前必须调用
        Song song = getSong(cursor);
        cursor.close();
        return song;
    }

    /**
     * 查询下载歌曲信息
     *
     * @param download
     * @return
     */
    public List<Song> queryDownloadList(int download) {
        String selection = COLUMN_DOWNLOAD + "=?";
        String[] selectionArgs = new String[]{download + ""};
        Cursor cursor = query(TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        List<Song> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            songs.add(getSong(cursor));
        }
        cursor.close();
        return songs;
    }

    /**
     * 数据库中插入或更新歌曲
     *
     * @param song
     * @return
     */
    public void insertOrUpdateSong(Song song) {
        replace(TABLE, null, getSongContent(song));
    }

    public boolean deleteSong(Song song) {
        String whereClause = COLUMN_SID + "=?";
        String[] whereArgs = new String[]{song.getId() + ""};
        int count = delete(TABLE, whereClause, whereArgs);
        return count > 0;
    }


    private Song getSong(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(COLUMN_SID));
        String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        long albumId = cursor.getLong(cursor.getColumnIndex(COLUMN_ALBUM_ID));
        String albumName = cursor.getString(cursor.getColumnIndex(COLUMN_ALBUM_NAME));
        long artistId = cursor.getLong(cursor.getColumnIndex(COLUMN_ARTIST_ID));
        String artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME));
        String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
        int size = cursor.getInt(cursor.getColumnIndex(COLUMN_SIZE));
        int duration = cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION));
        long date = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
        String quality = cursor.getString(cursor.getColumnIndex(COLUMN_QUALITY));
        int trackNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_NUMBER));
        String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
        String coverUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_URL));
        int download = cursor.getInt(cursor.getColumnIndex(COLUMN_DOWNLOAD));
        String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
        boolean status = false;
        if (download == 1 && !TextUtils.isEmpty(path) || !TextUtils.isEmpty(url)) {
            status = true;
        }
        Song song = new Song(id, title, albumId, albumName, artistId, artistName, url, size, duration,
                date, quality, trackNumber, description, coverUrl, download, path, status);
        return song;
    }

    public ContentValues getSongContent(Song song) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_SID, song.getId());
        values.put(COLUMN_TITLE, song.getTitle());
        values.put(COLUMN_ALBUM_ID, song.getAlbumId());
        values.put(COLUMN_ALBUM_NAME, song.getAlbumName());
        values.put(COLUMN_ARTIST_ID, song.getArtistId());
        values.put(COLUMN_ARTIST_NAME, song.getArtistName());
        values.put(COLUMN_URL, song.getUri() == null ? null : song.getUri().toString());
        values.put(COLUMN_SIZE, song.getSize());
        values.put(COLUMN_DURATION, song.getDuration());
        values.put(COLUMN_DATE, song.getDate());
        values.put(COLUMN_QUALITY, song.getQuality());
        values.put(COLUMN_TRACK_NUMBER, song.getTrackNumber());
        values.put(COLUMN_DESCRIPTION, song.getDescription());
        values.put(COLUMN_COVER_URL, song.getCoverUrl());
        values.put(COLUMN_DOWNLOAD, song.getDownload());
        values.put(COLUMN_PATH, song.getPath());
        return values;
    }
}
