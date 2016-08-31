package com.cpacm.core.bean.data;

import com.cpacm.core.bean.SongBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/31
 * @desciption: 播放列表
 */
public class PlayListData extends ResponseData<Object, Object> {
    private List<SongBean> playlist;

    public List<SongBean> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<SongBean> playlist) {
        this.playlist = playlist;
    }
}
