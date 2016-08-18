package com.cpacm.core.mvp.views;

import android.graphics.Bitmap;
import android.text.Spanned;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 播放列表页面
 */
public interface MusicPlayIView {

    void wikiDetail(long wikiId, Spanned title, Spanned decription, Bitmap cover);
}
