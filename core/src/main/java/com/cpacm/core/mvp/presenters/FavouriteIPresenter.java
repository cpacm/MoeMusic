package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/27
 * @desciption: 收藏的action接口
 */

public interface FavouriteIPresenter {

    void getWikis(List<WikiBean> wikiBeanList);

    void wikiFail(String msg);

    void updateCount(int curPage, int perpage, int total);
}
