package com.cpacm.core.bean;

import java.io.Serializable;

/**
 * @Author: cpacm
 * @Date: 2016/9/22.
 * @description: 专辑类
 */

public class Album implements Serializable{

    private static final long serialVersionUID = 1L;

    public final long artistId;
    public final String artistName;
    public final long id;
    public final int songCount;
    public final String title;
    public final int year;
    public final String cover;

    public Album() {
        this.id = -1;
        this.title = "";
        this.artistName = "";
        this.artistId = -1;
        this.songCount = -1;
        this.year = -1;
        this.cover = "";
    }

    public Album(long _id, String _title, String _artistName, long _artistId, int _songCount, int _year,String cover) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;
        this.artistId = _artistId;
        this.songCount = _songCount;
        this.year = _year;
        this.cover = cover;
    }
}
