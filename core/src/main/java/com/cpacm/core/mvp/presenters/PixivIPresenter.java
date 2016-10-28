package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.PixivBean;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description: p站接口
 */

public interface PixivIPresenter {

    void getPixivPics(List<PixivBean> pixivBeen);

    void fail(String msg);
}
