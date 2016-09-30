package com.cpacm.core.mvp.views;

import android.graphics.Bitmap;
import android.text.Spanned;

import com.cpacm.core.bean.Song;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/30
 * @desciption: 收藏夹歌曲列表的view接口
 */

public interface CollectionPlayIView {

    void collectionDetail(int collectionId, Spanned title, Spanned description);

    void collectionCover(Bitmap cover);

    void getSongs(List<Song> songs);

    void fail();
}
