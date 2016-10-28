package com.cpacm.core.mvp.views;

import com.cpacm.core.bean.PixivBean;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public interface PixivIView {
    void getPixivPicture(List<PixivBean> pixivBeen);

    void fail(String msg);
}
