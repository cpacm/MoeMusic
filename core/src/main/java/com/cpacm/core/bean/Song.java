package com.cpacm.core.bean;

import android.net.Uri;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author: cpacm
 * @date: 2016/7/19
 * @desciption: 将song实体类转换成标准歌曲实体
 */
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int DOWNLOAD_NONE = 0;//未下载
    public static final int DOWNLOAD_COMPLETE = 1;//下载完成
    public static final int DOWNLOAD_ING = 2;//下载中
    public static final int DOWNLOAD_DISABLE = 3;//无可用网络
    public static final int DOWNLOAD_WITH_WIFI = 4;//wifi下下载歌曲

    private long id;
    private String title;
    private long albumId;
    private String albumName;
    private long artistId;
    private String artistName;
    private String url;
    private int size;
    private int duration;
    private long date;
    private String quality;
    private int trackNumber;
    private String description;
    private String coverUrl;
    private int download;
    private String path;
    private boolean status;

    public Song() {
    }

    public Song(long id, String title, long albumId, String albumName, long artistId, String artistName, String url, int size, int duration, long date, String quality, int trackNumber, String description, String coverUrl, int download, String path, boolean status) {
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.url = url;
        this.size = size;
        this.duration = duration;
        this.date = date;
        this.quality = quality;
        this.trackNumber = trackNumber;
        this.description = description;
        this.coverUrl = coverUrl;
        this.download = download;
        this.path = path;
        this.status = status;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public Uri getUri() {
        if ((download == Song.DOWNLOAD_COMPLETE || id < 0) && !TextUtils.isEmpty(path))
            return Uri.parse(path);
        else
            return Uri.parse(url);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
