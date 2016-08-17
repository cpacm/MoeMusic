package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/1
 * @desciption: 专辑的presenter接口
 */
public interface MusicIPresenter {

    void getMusics(List<WikiBean> newMusics,List<WikiBean> hotMusics);

    void loadMusicFail(String msg);

}
