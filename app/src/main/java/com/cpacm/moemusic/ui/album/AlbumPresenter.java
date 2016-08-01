package com.cpacm.moemusic.ui.album;

import com.cpacm.core.action.AlbumAction;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.presenters.AlbumIPresenter;
import com.cpacm.core.mvp.views.AlbumIView;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/1
 * @desciption:
 */
public class AlbumPresenter implements AlbumIPresenter {

    private AlbumIView albumView;
    private AlbumAction albumAction;

    public AlbumPresenter(AlbumIView albumView) {
        this.albumView = albumView;
        albumAction = new AlbumAction(this);
    }

    public void requestAlbumIndex() {
        albumAction.getAlbumIndex();
    }

    @Override
    public void getMusics(List<WikiBean> newMusics, List<WikiBean> hotMusics) {
        albumView.getMusics(newMusics, hotMusics);
    }

    @Override
    public void loadMusicFail(String msg) {
        albumView.loadMusicFail(msg);
    }
}
