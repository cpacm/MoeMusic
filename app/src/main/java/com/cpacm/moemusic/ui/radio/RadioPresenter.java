package com.cpacm.moemusic.ui.radio;

import com.cpacm.core.action.ExploreAction;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.presenters.MusicIPresenter;
import com.cpacm.core.mvp.views.RadioIView;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/17
 * @desciption: 电台推荐
 */
public class RadioPresenter implements MusicIPresenter {

    private RadioIView radioView;
    private ExploreAction exploreAction;

    public RadioPresenter(RadioIView radioView) {
        this.radioView = radioView;
        exploreAction = new ExploreAction(this);
    }

    public void requestHotRadio() {
        exploreAction.getRadioIndex();
    }

    @Override
    public void getMusics(List<WikiBean> hotRadios, List<WikiBean> musics) {
        radioView.getMusics(hotRadios);
    }

    @Override
    public void loadMusicFail(String msg) {
        radioView.loadMusicFail(msg);
    }
}
