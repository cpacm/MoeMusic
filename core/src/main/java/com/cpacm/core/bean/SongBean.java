package com.cpacm.core.bean;

import android.text.TextUtils;

import com.cpacm.core.cache.SongManager;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 歌曲实体类
 */
public class SongBean {

    /**
     * up_id : 55776
     * url : http://nyan.90g.org/d/f/b3/5b07194f1a64121104cd065cf29ab637_320.mp3
     * stream_length : 313
     * stream_time : 05:13
     * file_size : 12276
     * file_type : mp3
     * wiki_id : 36372
     * wiki_type : music
     * cover : null
     * title : song.04 Surely Someday
     * wiki_title : TVアニメ「宇宙兄弟」ED5テーマ -「BEYOND」
     * wiki_url : http://moe.fm/music/36372
     * sub_id : 177285
     * sub_type : song
     * sub_title : Surely Someday
     * sub_url : http://moe.fm/song/177285
     * artist :
     * fav_wiki :
     * fav_sub :
     */

    private long up_id;
    private String url;
    private String stream_length;
    private String stream_time;
    private int file_size;
    private String file_type;
    private long wiki_id;
    private String wiki_type;
    private CoverBean cover;
    private String title;
    private String wiki_title;
    private String wiki_url;
    private long sub_id;
    private String sub_type;
    private String sub_title;
    private String sub_url;
    private String artist;
    private String fav_wiki;
    private String fav_sub;

    public long getUp_id() {
        return up_id;
    }

    public void setUp_id(long up_id) {
        this.up_id = up_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStream_length() {
        return stream_length;
    }

    public void setStream_length(String stream_length) {
        this.stream_length = stream_length;
    }

    public String getStream_time() {
        return stream_time;
    }

    public void setStream_time(String stream_time) {
        this.stream_time = stream_time;
    }

    public int getFile_size() {
        return file_size;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public long getWiki_id() {
        return wiki_id;
    }

    public void setWiki_id(long wiki_id) {
        this.wiki_id = wiki_id;
    }

    public String getWiki_type() {
        return wiki_type;
    }

    public void setWiki_type(String wiki_type) {
        this.wiki_type = wiki_type;
    }

    public CoverBean getCover() {
        return cover;
    }

    public void setCover(CoverBean cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWiki_title() {
        return wiki_title;
    }

    public void setWiki_title(String wiki_title) {
        this.wiki_title = wiki_title;
    }

    public String getWiki_url() {
        return wiki_url;
    }

    public void setWiki_url(String wiki_url) {
        this.wiki_url = wiki_url;
    }

    public long getSub_id() {
        return sub_id;
    }

    public void setSub_id(long sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getSub_url() {
        return sub_url;
    }

    public void setSub_url(String sub_url) {
        this.sub_url = sub_url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFav_wiki() {
        return fav_wiki;
    }

    public void setFav_wiki(String fav_wiki) {
        this.fav_wiki = fav_wiki;
    }

    public String getFav_sub() {
        return fav_sub;
    }

    public void setFav_sub(String fav_sub) {
        this.fav_sub = fav_sub;
    }

    public Song parseSong() {
        Song song = new Song();
        song.setId(sub_id);
        song.setAlbumId(wiki_id);
        song.setTitle(sub_title);
        song.setStatus(true);
        song.setSize(file_size);
        song.setUrl(url);
        song.setCoverUrl(cover.getLarge());
        song.setArtistName(artist);
        song.setAlbumName(wiki_title);
        SongManager.getInstance().updateSongFromLibrary(song);
        return song;
    }
}
