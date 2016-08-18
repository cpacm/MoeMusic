package com.cpacm.moemusic.ui.album;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.cpacm.core.bean.MetaBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.MusicPlayIView;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption:
 */
public class MusicPlayPresenter {

    private MusicPlayIView musicPlayView;

    public MusicPlayPresenter(MusicPlayIView musicPlayView) {
        this.musicPlayView = musicPlayView;
    }

    public void parseWiki(WikiBean wiki) {
        String wikiTitle = TextUtils.isEmpty(wiki.getWiki_title()) ? "MUSIC" : wiki.getWiki_title();
        Spanned title = Html.fromHtml(wikiTitle);
        List<MetaBean> metas = wiki.getWiki_meta();


    }
}
