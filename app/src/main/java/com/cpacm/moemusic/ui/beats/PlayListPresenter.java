package com.cpacm.moemusic.ui.beats;

import com.cpacm.core.action.WikiAction;
import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.SongBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.cache.LocalMusicLibrary;
import com.cpacm.core.mvp.presenters.WikiIPresenter;
import com.cpacm.core.mvp.views.PlayListIView;
import com.cpacm.moemusic.MoeApplication;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public class PlayListPresenter implements WikiIPresenter {

    private PlayListIView playListView;
    private WikiAction wikiAction;

    public PlayListPresenter(PlayListIView playListView) {
        this.playListView = playListView;
        wikiAction = new WikiAction(this);
    }

    public void requestAlbum(Song song) {
        if (song.getId() > 0) {
            String wikiType = WikiBean.WIKI_MUSIC + "," + WikiBean.WIKI_RADIO;
            wikiAction.getWikiById(wikiType, song.getAlbumId());
        } else {
            Album album = LocalMusicLibrary.getAlbum(MoeApplication.getInstance(), song.getAlbumId());
            playListView.getAlbum(false, null, album);
        }
    }

    @Override
    public void getWikis(List<WikiBean> wikiBeanList) {
        if (wikiBeanList != null && wikiBeanList.size() > 0) {
            playListView.getAlbum(true, wikiBeanList.get(0), null);
        }
    }

    @Override
    public void wikiFail(String msg) {
        playListView.fail(msg);
    }

    @Override
    public void updateCount(int curPage, int perpage, int total) {

    }
}
