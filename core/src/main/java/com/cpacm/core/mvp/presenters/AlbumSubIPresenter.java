package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.WikiSubBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 曲目列表
 */
public interface AlbumSubIPresenter {
    void getAlbumSubs(List<WikiSubBean> subs, int curPage, int count);

    void fail(String msg);
}
