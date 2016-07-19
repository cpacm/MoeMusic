package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 歌曲实体类
 */
public class SongBean {

    private int id;
    private String title;
    private String author;
    private String mp3Url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }
}
