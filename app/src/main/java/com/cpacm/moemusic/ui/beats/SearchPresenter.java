package com.cpacm.moemusic.ui.beats;

import android.text.TextUtils;

import com.cpacm.core.action.SearchAction;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.presenters.SearchIPresenter;
import com.cpacm.core.mvp.views.SearchIView;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: 搜索业务
 */

public class SearchPresenter implements SearchIPresenter {

    private final static String WIKI_TYPE = "music,radio";
    private final static int perPage = 20;

    private SearchAction searchAction;
    private SearchIView searchView;

    private int curPage;
    private boolean add, hasMore;
    private String keyword;

    public SearchPresenter(SearchIView searchView) {
        this.searchView = searchView;
        searchAction = new SearchAction(this);
        add = false;
        hasMore = false;
    }

    public void requestData(String text) {
        this.keyword = text;
        requestData(1);
    }

    public void requestData() {
        requestData(1);
    }

    public void loadMore() {
        curPage++;
        requestData(curPage);
    }

    private void requestData(int page) {
        curPage = page;
        if (!TextUtils.isEmpty(keyword)) {
            searchAction.search(keyword, WIKI_TYPE, page);
        }
    }

    @Override
    public void getWikis(List<WikiBean> wikiBeanList) {
        searchView.getWikiBean(wikiBeanList, add, hasMore);
    }

    @Override
    public void wikiFail(String msg) {
        searchView.fail(msg);
    }

    @Override
    public void updateCount(int curPage, int perpage, int total) {
        this.curPage = curPage;
        add = curPage != 1;
        hasMore = curPage * perPage <= total;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
