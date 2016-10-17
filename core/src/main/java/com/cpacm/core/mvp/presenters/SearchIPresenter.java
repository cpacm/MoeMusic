package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: 搜索业务接口
 */

public interface SearchIPresenter {

    void getWikis(List<WikiBean> wikiBeanList);

    void wikiFail(String msg);

    void updateCount(int curPage, int perpage, int total);
}
