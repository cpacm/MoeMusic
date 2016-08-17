package com.cpacm.core.bean.data;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/1
 * @desciption: 新曲速递 音乐热榜
 */
public class AlbumData extends ResponseData<Object, String> {

    private List<WikiBean> musics;

    private List<WikiBean> hot_musics;

    public List<WikiBean> getMusics() {
        return musics;
    }

    public void setMusics(List<WikiBean> musics) {
        this.musics = musics;
    }

    public List<WikiBean> getHot_musics() {
        return hot_musics;
    }

    public void setHot_musics(List<WikiBean> hot_musics) {
        this.hot_musics = hot_musics;
    }
}
