package com.cpacm.core.mvp.views;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: 搜索视图接口
 */

public interface SearchIView {

    void getWikiBean(List<WikiBean> wikis, boolean add, boolean hasMore);

    void fail(String msg);
}
