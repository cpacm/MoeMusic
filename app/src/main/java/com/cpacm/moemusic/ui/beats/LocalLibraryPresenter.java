package com.cpacm.moemusic.ui.beats;

import android.content.Context;

import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Artist;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.LocalMusicLibrary;
import com.cpacm.core.mvp.views.LocalIView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/10/4
 * @desciption:
 */

public class LocalLibraryPresenter {

    private LocalIView.LocalMusic localMusic;
    private LocalIView.LocalAlbum localAlbum;
    private LocalIView.LocalArtist localArtist;

    private Context context;

    public LocalLibraryPresenter(LocalIView.LocalMusic localMusic, Context context) {
        this.localMusic = localMusic;
        this.context = context;
    }

    public LocalLibraryPresenter(LocalIView.LocalAlbum localAlbum, Context context) {
        this.localAlbum = localAlbum;
        this.context = context;
    }

    public LocalLibraryPresenter(LocalIView.LocalArtist localArtist, Context context) {
        this.localArtist = localArtist;
        this.context = context;
    }

    public void requestMusic() {
        Observable.create(
                new Observable.OnSubscribe<List<Song>>() {
                    @Override
                    public void call(Subscriber<? super List<Song>> subscriber) {
                        List<Song> songs = LocalMusicLibrary.getAllSongs(context);
                        subscriber.onNext(songs);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Song>>() {
                    @Override
                    public void call(List<Song> songs) {
                        if (localMusic != null)
                            localMusic.getLocalMusic(songs);
                    }
                });
    }

    public void requestAlbum() {
        Observable.create(
                new Observable.OnSubscribe<List<Album>>() {
                    @Override
                    public void call(Subscriber<? super List<Album>> subscriber) {
                        List<Album> albums = LocalMusicLibrary.getAllAlbums(context);
                        subscriber.onNext(albums);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Album>>() {
                    @Override
                    public void call(List<Album> albums) {
                        if (localAlbum != null)
                            localAlbum.getLocalAlbum(albums);
                    }
                });
    }

    public void requestArtist() {
        Observable.create(
                new Observable.OnSubscribe<List<Artist>>() {
                    @Override
                    public void call(Subscriber<? super List<Artist>> subscriber) {
                        List<Artist> artists = LocalMusicLibrary.getAllArtists(context);
                        subscriber.onNext(artists);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Artist>>() {
                    @Override
                    public void call(List<Artist> artists) {
                        if (localArtist != null)
                            localArtist.getLocalArtist(artists);
                    }
                });
    }
}
