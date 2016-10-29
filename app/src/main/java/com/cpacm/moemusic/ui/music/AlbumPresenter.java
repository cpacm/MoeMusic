package com.cpacm.moemusic.ui.music;

import com.cpacm.core.action.BannerAction;
import com.cpacm.core.action.ExploreAction;
import com.cpacm.core.bean.BannerBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.presenters.BannerIPresenter;
import com.cpacm.core.mvp.presenters.MusicIPresenter;
import com.cpacm.core.mvp.views.AlbumIView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/1
 * @desciption: 专辑界面的逻辑处理
 */
public class AlbumPresenter implements MusicIPresenter, BannerIPresenter {

    private AlbumIView albumView;
    private ExploreAction exploreAction;
    private BannerAction bannerAction;

    public AlbumPresenter(AlbumIView albumView) {
        this.albumView = albumView;
        exploreAction = new ExploreAction(this);
        bannerAction = new BannerAction(this);
    }

    public void requestBanner() {
        bannerAction.getBanners();
    }

    public void requestAlbumIndex() {
        exploreAction.getAlbumIndex();
    }

    @Override
    public void getMusics(List<WikiBean> newMusics, List<WikiBean> hotMusics) {
        albumView.getMusics(newMusics, hotMusics);
    }

    @Override
    public void loadMusicFail(String msg) {
        albumView.loadFail(msg);
    }

    @Override
    public void getBanners(List<BannerBean> been) {
        if (been == null || been.size() == 0) {
            been = new ArrayList<>();
            BannerBean bean = new BannerBean();
            bean.setName("key");
            bean.setBanner("http://ofrf20oms.bkt.clouddn.com/Key%20Official.jpg");
            bean.setKeyword("key");
            been.add(bean);
        }
        albumView.getBanner(been);
    }

    @Override
    public void fail(String msg) {
        getBanners(null);
    }
}
