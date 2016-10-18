package com.cpacm.core.mvp.views;

import com.cpacm.core.bean.BannerBean;
import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/1
 * @desciption: 专辑的view接口
 */
public interface AlbumIView {

    void getMusics(List<WikiBean> newMusics,List<WikiBean> hotMusics);

    void getBanner(List<BannerBean> been);

    void loadFail(String msg);
}
