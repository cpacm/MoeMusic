package com.cpacm.core.bean.data;

import com.cpacm.core.bean.PixivBean;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description: p站每日排行榜
 */

public class PixivData {

    private List<PixivBean> contents;

    public List<PixivBean> getContents() {
        return contents;
    }

    public void setContents(List<PixivBean> contents) {
        this.contents = contents;
    }
}
