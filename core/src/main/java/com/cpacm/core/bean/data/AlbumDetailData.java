package com.cpacm.core.bean.data;

import com.cpacm.core.bean.WikiSubBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 包含歌曲信息的专辑播放列表
 */
public class AlbumDetailData extends ResponseData<Object, Object> {
    private List<WikiSubBean> subs;

    public List<WikiSubBean> getSubs() {
        return subs;
    }

    public void setSubs(List<WikiSubBean> subs) {
        this.subs = subs;
    }
}
