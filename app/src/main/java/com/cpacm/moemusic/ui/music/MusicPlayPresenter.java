package com.cpacm.moemusic.ui.music;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.action.AlbumSubsAction;
import com.cpacm.core.action.FavAction;
import com.cpacm.core.action.RadioSubsAction;
import com.cpacm.core.bean.RelationshipBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.bean.WikiSubBean;
import com.cpacm.core.mvp.presenters.FavIPresenter;
import com.cpacm.core.mvp.presenters.AlbumSubIPresenter;
import com.cpacm.core.mvp.presenters.RadioSubIPresenter;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: 音乐播放列表页逻辑处理
 */
public class MusicPlayPresenter implements AlbumSubIPresenter, RadioSubIPresenter, FavIPresenter {

    private MusicPlayIView musicPlayView;
    private AlbumSubsAction albumSubsAction;
    private RadioSubsAction radioSubsAction;
    private FavAction favAction;
    private int albumPage = 1, albumPerpage = 25;
    private long wikiId;
    private List<Song> songs;
    private String wikiType;
    private String cover, title, largeCover;

    public MusicPlayPresenter(MusicPlayIView musicPlayView, String wikiType) {
        this.musicPlayView = musicPlayView;
        albumSubsAction = new AlbumSubsAction(this);
        radioSubsAction = new RadioSubsAction(this);
        favAction = new FavAction(this);
        songs = new ArrayList<>();
        this.wikiType = wikiType;
    }

    public void parseWiki(WikiBean wiki) {
        title = TextUtils.isEmpty(wiki.getWiki_title()) ? "MUSIC" : wiki.getWiki_title();
        final Spanned title = Html.fromHtml(this.title);
        final long id = wiki.getWiki_id();
        boolean fav = wiki.getWiki_user_fav() != null;
        musicPlayView.wikiDetail(id, title, wiki.getWikiDescription(), fav);
        cover = wiki.getWiki_cover().getLarge();
        largeCover = wiki.getWiki_cover().getLarge();
        Glide.with(MoeApplication.getInstance())
                .load(cover)
                .asBitmap()
                .placeholder(R.drawable.cover)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        musicPlayView.wikiCover(resource);
                    }
                });
    }

    public void getSongs(long wikiId) {
        songs.clear();
        if (wikiType.equals(WikiBean.WIKI_MUSIC)) {
            getAlbumSongs(wikiId, 1);
        } else if (wikiType.equals(WikiBean.WIKI_RADIO)) {
            getRadioSongs(wikiId);
        }
    }

    public void getRadioSongs(long wikiId) {
        radioSubsAction.getRadioSubs(wikiId);
    }

    public void getAlbumSongs(long wikiId, int page) {
        this.wikiId = wikiId;
        albumPage = page;
        albumSubsAction.getAlbumSubs(wikiId, albumPage);
    }

    @Override
    public void getAlbumSubs(List<WikiSubBean> subs, int curPage, final int count) {
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
                        song.setCoverUrl(cover);
                        song.setAlbumName(title);
                        songs.add(song);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MoeLogger.e(throwable.toString());
                    }
                });
        if (curPage * albumPerpage < count) {
            albumPage++;
            getAlbumSongs(wikiId, albumPage);
        } else {
            musicPlayView.songs(songs);
        }
    }

    @Override
    public void getRadioSubs(List<RelationshipBean> subs) {
        Observable.from(subs)
                .map(new Func1<RelationshipBean, Song>() {
                    @Override
                    public Song call(RelationshipBean relationshipBean) {
                        if (relationshipBean == null || relationshipBean.getObj() == null) {
                            return null;
                        }
                        Song song = relationshipBean.getObj().parseSong();
                        if (!TextUtils.isEmpty(relationshipBean.getWr_about())) {
                            song.setDescription(relationshipBean.getWr_about());
                        }
                        return song;
                    }
                })
                .subscribe(new Action1<Song>() {
                    @Override
                    public void call(Song song) {
                        if (song != null) {
                            song.setCoverUrl(cover);
                            songs.add(song);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MoeLogger.e(throwable.toString());
                    }
                });
        musicPlayView.songs(songs);
    }

    public void favMusic(long wikiId, String content) {
        favAction.fav(wikiType, wikiId, Uri.encode(content));
    }

    public void unFavMusic(long wikiId) {
        favAction.unFav(wikiType, wikiId);
    }

    @Override
    public void favSuccess() {
        musicPlayView.favMusic(true);
    }

    @Override
    public void unFavSuccess() {
        musicPlayView.favMusic(false);
    }


    @Override
    public void fail(String msg) {
        if (songs.size() > 0) {
            musicPlayView.songs(songs);
        }
        musicPlayView.fail(msg);
    }
}
