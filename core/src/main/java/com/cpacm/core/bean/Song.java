package com.cpacm.core.bean;

import android.net.Uri;

/**
 * @author: cpacm
 * @date: 2016/7/19
 * @desciption: 将song实体类转换成标准歌曲实体
 */
public class Song {

    private long id;
    private String title;
    private long albumId;
    private String albumName;
    private long artistId;
    private String artistName;
    private Uri uri;
    private int size;
    private int duration;
    private long date;
    private String quality;
    private int trackNumber;

    private boolean status;

    public Song() {}

    public Song(long id, String title, long albumId, String albumName, long artistId, String artistName, Uri uri, int size, int duration, long date, String quality, int trackNumber) {
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.uri = uri;
        this.size = size;
        this.duration = duration;
        this.date = date;
        this.quality = quality;
        this.trackNumber = trackNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setUrl(String url){
        this.uri = Uri.parse(url);
    }

    public Uri getUri() {
        return uri;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
