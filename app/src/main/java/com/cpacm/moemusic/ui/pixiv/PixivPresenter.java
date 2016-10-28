package com.cpacm.moemusic.ui.pixiv;

import com.cpacm.core.action.PixivAction;
import com.cpacm.core.bean.PixivBean;
import com.cpacm.core.mvp.presenters.PixivIPresenter;
import com.cpacm.core.mvp.views.PixivIView;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public class PixivPresenter implements PixivIPresenter {

    private PixivIView pixivView;
    private PixivAction pixivAction;

    public PixivPresenter(PixivIView pixivView) {
        this.pixivView = pixivView;
        pixivAction = new PixivAction(this);
    }

    /**
     * 每日p图 前50
     */
    public void requestPixiv() {
        pixivAction.requestPixivDaily();
    }

    @Override
    public void getPixivPics(List<PixivBean> pixivBeen) {
        pixivView.getPixivPicture(pixivBeen);
    }

    @Override
    public void fail(String msg) {
        pixivView.fail(msg);
    }
}
