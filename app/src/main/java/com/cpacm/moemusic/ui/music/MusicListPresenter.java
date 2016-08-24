package com.cpacm.moemusic.ui.music;

import android.text.TextUtils;

import com.cpacm.core.action.WikiAction;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.presenters.WikiIPresenter;
import com.cpacm.core.mvp.views.MusicListIView;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/24
 * @desciption: 专题列表数据处理
 */
public class MusicListPresenter implements WikiIPresenter {

    private String type, date, initial;//类型，时间，首字母
    private MusicListIView musicListView;
    private WikiAction wikiAction;
    private int curPage;
    private boolean add, hasMore;

    public MusicListPresenter(MusicListIView musicListView) {
        this.musicListView = musicListView;
        wikiAction = new WikiAction(this);
        add = false;
        hasMore = false;
    }

    public void requestData() {
        requestData(1);
    }

    public void loadMore() {
        curPage++;
        requestData(curPage);
    }

    public void requestData(int page) {
        curPage = page;
        if (TextUtils.isEmpty(date) && TextUtils.isEmpty(initial)) {
            wikiAction.getWikis(type, page);
        } else if (TextUtils.isEmpty(date) && !TextUtils.isEmpty(initial)) {
            wikiAction.getWikisByInital(type, page, initial);
        } else if (!TextUtils.isEmpty(date) && TextUtils.isEmpty(initial)) {
            wikiAction.getWikisByDate(type, page, date);
        } else {
            wikiAction.getWikis(type, page, initial, date);
        }
    }

    @Override
    public void getWikis(List<WikiBean> wikiBeanList) {
        musicListView.getWikiBean(wikiBeanList, add, hasMore);
    }

    @Override
    public void updateCount(int curPage, int perPage, int total) {
        this.curPage = curPage;
        add = curPage != 1;
        hasMore = curPage * perPage <= total;
    }

    @Override
    public void wikiFail(String msg) {
        musicListView.fail(msg);
    }


    public void setType(String wikiType) {
        if (wikiType.equals("全部")) {
            type = "music,radio";
        } else if (wikiType.equals("专辑")) {
            type = "music";
        } else if (wikiType.equals("电台")) {
            type = "radio";
        }
    }

    public void setDate(String wikiDate) {
        if (wikiDate.equals("全部")) {
            date = null;
        } else {
            date = wikiDate;
        }
    }

    public void setInitial(String wikiInitial) {
        if (wikiInitial.equals("全部")) {
            initial = null;
        } else {
            initial = wikiInitial;
        }
    }
}
