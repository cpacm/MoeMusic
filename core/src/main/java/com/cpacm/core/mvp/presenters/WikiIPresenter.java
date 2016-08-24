package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 获取wiki数据
 */
public interface WikiIPresenter {

    void getWikis(List<WikiBean> wikiBeanList);

    void wikiFail(String msg);

    void updateCount(int curPage, int perpage, int total);

}
