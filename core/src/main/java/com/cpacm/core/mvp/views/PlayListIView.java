package com.cpacm.core.mvp.views;

import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public interface PlayListIView {
    void getAlbum(boolean moeAlbum, WikiBean wiki, Album album);

    void fail(String msg);
}
