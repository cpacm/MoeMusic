package com.cpacm.core.bean;

/**
 * @Author: cpacm
 * @Date: 2016/9/22.
 * @description: 艺术家类
 */

public class Artist {
    public final int albumCount;
    public final long id;
    public final String name;
    public final int songCount;

    public Artist() {
        this.id = -1;
        this.name = "";
        this.songCount = -1;
        this.albumCount = -1;
    }

    public Artist(long _id, String _name, int _albumCount, int _songCount) {
        this.id = _id;
        this.name = _name;
        this.songCount = _songCount;
        this.albumCount = _albumCount;
    }
}
