package com.cpacm.core.bean.event;

/**
 * @author: cpacm
 * @date: 2016/8/22
 * @desciption: 收藏事件
 */
public class FavEvent {
    long wikiId;
    boolean fav;

    public FavEvent(long wikiId, boolean fav) {
        this.wikiId = wikiId;
        this.fav = fav;
    }

    public long getWikiId() {
        return wikiId;
    }

    public boolean isFav() {
        return fav;
    }
}
