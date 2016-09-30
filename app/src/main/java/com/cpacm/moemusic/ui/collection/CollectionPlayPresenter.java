package com.cpacm.moemusic.ui.collection;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.bean.CollectionShipBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.mvp.views.CollectionPlayIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/9/30
 * @desciption: 收藏夹播放页面业务逻辑处理
 */

public class CollectionPlayPresenter {

    private CollectionPlayIView collectionPlayView;
    private CollectionBean collectionBean;

    public CollectionPlayPresenter(CollectionPlayIView collectionPlayView, CollectionBean collectionBean) {
        this.collectionPlayView = collectionPlayView;
        this.collectionBean = collectionBean;
    }

    /**
     * 收集collection数据
     */
    public void init() {
        if (collectionBean == null) {
            collectionPlayView.fail();
        }
        final int id = collectionBean.getId();
        Spanned title = Html.fromHtml(collectionBean.getTitle());
        Spanned description = Html.fromHtml((collectionBean.getDescription()));
        collectionPlayView.collectionDetail(id, title, description);

        Glide.with(MoeApplication.getInstance())
                .load(collectionBean.getCoverUrl())
                .asBitmap()
                .placeholder(R.drawable.moefou)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        collectionPlayView.collectionCover(resource);
                    }
                });

        Observable.create(
                new Observable.OnSubscribe<List<CollectionShipBean>>() {
                    @Override
                    public void call(Subscriber<? super List<CollectionShipBean>> subscriber) {
                        subscriber.onNext(CollectionManager.getInstance().getCollectionShipList(id));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<List<CollectionShipBean>, List<Song>>() {
                    @Override
                    public List<Song> call(List<CollectionShipBean> collectionShipBeen) {
                        List<Song> songs = new ArrayList<>();
                        for (CollectionShipBean bean : collectionShipBeen) {
                            Song song = SongManager.getInstance().querySong(bean.getSid());
                            if (song != null) {
                                songs.add(song);
                            }
                        }
                        return songs;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Song>>() {
                    @Override
                    public void call(List<Song> songs) {
                        collectionPlayView.getSongs(songs);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        collectionPlayView.fail();
                    }
                });
    }
}
