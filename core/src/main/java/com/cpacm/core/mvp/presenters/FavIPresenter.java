package com.cpacm.core.mvp.presenters;

/**
 * @author: cpacm
 * @date: 2016/8/22
 * @desciption: 收藏数据接口
 */
public interface FavIPresenter {
    void favSuccess();

    void unFavSuccess();

    void fail( String msg);
}
