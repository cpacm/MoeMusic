package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.Song;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/31
 * @desciption: 播放列表
 */
public interface PlaylistIPresenter {

    void getPlayList(List<Song> songs);

    void getPlaylistFail(String msg);

}
