package com.cpacm.core.action;

import com.cpacm.core.http.HttpUtil;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: 轮播图数据请求
 */

public class BannerAction extends BaseAction {

    public BannerAction() {
        super(HttpUtil.BANNER_URL);
    }
}
