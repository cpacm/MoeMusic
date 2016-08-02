package com.cpacm.moemusic.music;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import com.cpacm.core.bean.SongBean;

import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE;

/**
 * @author: cpacm
 * @date: 2016/7/19
 * @desciption: 将song实体类转换成标准歌曲实体
 */
public class Song {

    private static final MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();

    private SongBean songBean;
    public MediaMetadataCompat data;

    public Song(SongBean songBean) {
        this.songBean = songBean;
        setUri(songBean.getUrl());
        setSongTitle(songBean.getTitle());
        setSongId(songBean.getSub_id());
    }

    public QueueItem toQueueItem() {
        return new QueueItem(data.getDescription(), songBean.getSub_id());
    }

    ///////////////////////////////////////////////////////////////////////////
    // Uri
    ///////////////////////////////////////////////////////////////////////////
    public Uri getBaseUri() {
        return EXTERNAL_CONTENT_URI;
    }

    public Song setUri(String url) {
        putString(METADATA_KEY_ART_URI, url);
        return this;
    }

    public Uri getUri() {
        return Uri.parse(data.getString(METADATA_KEY_ART_URI));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Title
    ///////////////////////////////////////////////////////////////////////////
    public String getSongTitle() {
        return data.getString(METADATA_KEY_TITLE);
    }

    public Song setSongTitle(String title) {
        putString(METADATA_KEY_TITLE, title);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // id
    ///////////////////////////////////////////////////////////////////////////
    public long getSongId() {
        return data.getLong(METADATA_KEY_MEDIA_ID);
    }

    public Song setSongId(long id) {
        putLong(METADATA_KEY_MEDIA_ID, id);
        return this;
    }


    protected void putLong(String key, long value) {
        builder.putLong(key, value);
        data = builder.build();
    }

    protected void putString(String key, String value) {
        builder.putString(key, value);
        data = builder.build();
    }

    protected void putBitmap(String key, Bitmap value) {
        builder.putBitmap(key, value);
        data = builder.build();
    }

}
