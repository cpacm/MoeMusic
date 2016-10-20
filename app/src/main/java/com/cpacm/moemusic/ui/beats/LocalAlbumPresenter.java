package com.cpacm.moemusic.ui.beats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.LocalMusicLibrary;
import com.cpacm.core.mvp.views.LocalAlbumIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/10/20
 * @desciption: 本地专辑事务处理
 */

public class LocalAlbumPresenter {

    private Context context;
    private LocalAlbumIView localAlbumIView;

    public LocalAlbumPresenter(Context context, LocalAlbumIView localAlbumIView) {
        this.context = context;
        this.localAlbumIView = localAlbumIView;
    }

    public void initAlbum(final Album album) {
        localAlbumIView.localAlbumDetail(album.id, Html.fromHtml(album.title), Html.fromHtml(album.artistName));
        Glide.with(context)
                .load(album.cover)
                .asBitmap()
                .placeholder(R.drawable.cover)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        localAlbumIView.albumCover(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Bitmap resource = BitmapFactory.decodeResource(context.getResources(), R.drawable.cover);
                        localAlbumIView.albumCover(resource);
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
        Observable.create(
                new Observable.OnSubscribe<List<Song>>() {
                    @Override
                    public void call(Subscriber<? super List<Song>> subscriber) {
                        subscriber.onNext(LocalMusicLibrary.getSongsForAlbum(context, album.id));
                    }
                }).subscribeOn(Schedulers.io())
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Song>>() {
                    @Override
                    public void call(List<Song> songs) {
                        localAlbumIView.songs(songs);
                    }
                });
    }
}
