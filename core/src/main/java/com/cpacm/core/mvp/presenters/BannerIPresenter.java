package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.BannerBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption:
 */

public interface BannerIPresenter {

    void getBanners(List<BannerBean> been);

    void fail(String msg);
}
