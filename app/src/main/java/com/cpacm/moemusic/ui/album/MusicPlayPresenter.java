package com.cpacm.moemusic.ui.album;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.action.AlbumSubsAction;
import com.cpacm.core.action.FavAction;
import com.cpacm.core.bean.MetaBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.bean.WikiSubBean;
import com.cpacm.core.mvp.presenters.FavIPresenter;
import com.cpacm.core.mvp.presenters.SubIPresenter;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 音乐播放列表页逻辑处理
 */
public class MusicPlayPresenter implements SubIPresenter, FavIPresenter {

    private MusicPlayIView musicPlayView;
    private AlbumSubsAction albumSubsAction;
    private FavAction favAction;
    private int albumPage = 1, albumPerpage = 25;
    private long wikiId;
    private List<Song> songs;

    public MusicPlayPresenter(MusicPlayIView musicPlayView) {
        this.musicPlayView = musicPlayView;
        albumSubsAction = new AlbumSubsAction(this);
        favAction = new FavAction(this);
        songs = new ArrayList<>();
    }

    public void parseWiki(WikiBean wiki) {
        String wikiTitle = TextUtils.isEmpty(wiki.getWiki_title()) ? "MUSIC" : wiki.getWiki_title();
        final Spanned title = Html.fromHtml(wikiTitle);
        final long id = wiki.getWiki_id();
        boolean fav = wiki.getWiki_user_fav() != null;
        List<MetaBean> metas = wiki.getWiki_meta();
        if (metas == null) {
            musicPlayView.wikiDetail(id, title, null, fav);
        } else {
            Html.ImageGetter imageGetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String s) {
                    Drawable drawable = new BitmapDrawable();
                    // Important
                    drawable.setBounds(0, 0, 0, 0);
                    return drawable;
                }
            };
            Spanned desc = null;
            for (MetaBean bean : metas) {
                if (bean.getMeta_key().equals("简介")) {
                    //消除html解析出来的图片
                    desc = Html.fromHtml((String) bean.getMeta_value(), imageGetter, null);
                    break;
                }
            }
            musicPlayView.wikiDetail(id, title, desc, fav);
        }
        Glide.with(MoeApplication.getInstance())
                .load(wiki.getWiki_cover().getLarge())
                .asBitmap()
                .placeholder(R.drawable.cover)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        musicPlayView.wikiCover(resource);
                    }
                });
    }

    public void getAlbumSongs(long wikiId, int page) {
        if (page == 1) {
            songs.clear();
        }
        this.wikiId = wikiId;
        albumPage = page;
        albumSubsAction.getAlbumSubs(wikiId, albumPage);
    }

    @Override
    public void getAlbumSubs(List<WikiSubBean> subs, int curPage, int count) {
        Observable.from(subs)
                .map(new Func1<WikiSubBean, Song>() {
                    @Override
                    public Song call(WikiSubBean wikiSubBean) {
                        return wikiSubBean.parseSong();
                    }
                })
                .subscribe(new Action1<Song>() {
                    @Override
                    public void call(Song song) {
                        songs.add(song);
                    }
                });
        if (curPage * albumPerpage < count) {
            albumPage++;
            getAlbumSongs(wikiId, albumPage);
        } else {
            musicPlayView.songs(songs);
        }
    }

    public void favAlbum(long wikiId, String content) {
        favAction.fav(WikiBean.WIKI_MUSIC, wikiId, Uri.encode(content));
    }

    public void unFavAlbum(long wikiId) {
        favAction.unFav(WikiBean.WIKI_MUSIC, wikiId);
    }

    @Override
    public void favSuccess() {
        musicPlayView.favAlbum(true);
    }

    @Override
    public void unFavSuccess() {
        musicPlayView.favAlbum(false);
    }

    @Override
    public void fail(String msg) {
        if (songs.size() > 0) {
            musicPlayView.songs(songs);
        }
        musicPlayView.fail(msg);
    }
}
