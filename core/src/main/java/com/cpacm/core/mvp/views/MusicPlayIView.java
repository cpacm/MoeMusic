package com.cpacm.core.mvp.views;

import android.graphics.Bitmap;
import android.text.Spanned;

import com.cpacm.core.bean.Song;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 播放列表页面
 */
public interface MusicPlayIView {

    void wikiDetail(long wikiId, Spanned title, Spanned description, boolean fav);

    void wikiCover(Bitmap cover);

    void songs(List<Song> songs);

    void favMusic(boolean fav);

    void fail(String msg);
}
