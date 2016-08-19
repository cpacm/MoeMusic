package com.cpacm.moemusic.ui.album;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.bean.MetaBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.MoeApplication;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 音乐播放列表页逻辑处理
 */
public class MusicPlayPresenter {

    private MusicPlayIView musicPlayView;

    public MusicPlayPresenter(MusicPlayIView musicPlayView) {
        this.musicPlayView = musicPlayView;
    }

    private Html.ImageGetter imageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String s) {
            Drawable drawable = new BitmapDrawable();
            // Important
            drawable.setBounds(0, 0, 0, 0);
            return drawable;
        }
    };

    public void parseWiki(WikiBean wiki) {
        String wikiTitle = TextUtils.isEmpty(wiki.getWiki_title()) ? "MUSIC" : wiki.getWiki_title();
        final Spanned title = Html.fromHtml(wikiTitle);
        final long id = wiki.getWiki_id();
        List<MetaBean> metas = wiki.getWiki_meta();
        if (metas == null) {
            musicPlayView.wikiDetail(id, title, null);
        } else {
            Spanned desc = null;
            for (MetaBean bean : metas) {
                if (bean.getMeta_key().equals("简介")) {
                    desc = Html.fromHtml((String) bean.getMeta_value(), imageGetter, null);
                    break;
                }
            }
            musicPlayView.wikiDetail(id, title, desc);
        }
        Glide.with(MoeApplication.getInstance())
                .load(wiki.getWiki_cover().getLarge())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        musicPlayView.wikiCover(resource);
                    }
                });

    }
}
