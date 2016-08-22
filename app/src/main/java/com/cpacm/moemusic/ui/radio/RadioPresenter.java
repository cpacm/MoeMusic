package com.cpacm.moemusic.ui.radio;

import com.cpacm.core.action.ExploreAction;
import com.cpacm.core.action.WikiAction;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.presenters.MusicIPresenter;
import com.cpacm.core.mvp.presenters.WikiIPresenter;
import com.cpacm.core.mvp.views.RadioIView;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/17
 * @desciption: 电台推荐
 */
public class RadioPresenter implements MusicIPresenter, WikiIPresenter {

    private RadioIView radioView;
    private ExploreAction exploreAction;
    private WikiAction wikiAction;

    public RadioPresenter(RadioIView radioView) {
        this.radioView = radioView;
        exploreAction = new ExploreAction(this);
        wikiAction = new WikiAction(this);
    }

    public void requestRadios() {
        exploreAction.getRadioIndex();
        wikiAction.getWikis(WikiBean.WIKI_RADIO, 1, 20);
    }

    @Override
    public void getMusics(List<WikiBean> hotRadios, List<WikiBean> musics) {
        radioView.getMusics(hotRadios, musics);
    }

    @Override
    public void loadMusicFail(String msg) {
        radioView.loadMusicFail(msg);
    }

    @Override
    public void getWikis(List<WikiBean> wikiBeanList) {
        radioView.getMusics(null, wikiBeanList);
    }

    @Override
    public void wikiFail(String msg) {
        radioView.loadMusicFail(msg);
    }

    @Override
    public void updateCount(int curPage, int total) {

    }
}
