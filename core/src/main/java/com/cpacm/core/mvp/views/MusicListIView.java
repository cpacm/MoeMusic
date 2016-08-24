package com.cpacm.core.mvp.views;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/24
 * @desciption:
 */
public interface MusicListIView {

    void getWikiBean(List<WikiBean> wikis, boolean add, boolean hasMore);

    void fail(String msg);
}
