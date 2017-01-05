package com.cpacm.moemusic.ui.beats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Song;
import com.cpacm.core.mvp.views.LocalAlbumIView;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.ui.music.MusicDetailActivity;
import com.cpacm.moemusic.ui.music.MusicPlayPresenter;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/20
 * @desciption: 本地专辑详情列表页
 */

public class LocalAlbumDetailActivity extends MusicDetailActivity implements LocalAlbumIView {

    public static void open(Context context, Album album) {
        context.startActivity(getIntent(context, album));
    }

    public static Intent getIntent(Context context, Album album) {
        Intent intent = new Intent();
        intent.setClass(context, LocalAlbumDetailActivity.class);
        intent.putExtra("album", album);
        return intent;
    }

    private Album album;
    private LocalAlbumPresenter laPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        removeFav();

        album = (Album) getIntent().getSerializableExtra("album");
        if (album == null) {
            showSnackBar(refreshView,R.string.music_message_error);
            finish();
        }
        laPresenter = new LocalAlbumPresenter(this, this);
    }

    @Override
    public void onSwipeRefresh() {
        laPresenter.initAlbum(album);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void localAlbumDetail(long albumId, Spanned title, Spanned description) {
        setMusicDetail(title, description);
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == album.id) {
            isPlayingAlbum = true;
        }
    }

    @Override
    public void albumCover(Bitmap cover) {
        setMusicCover(cover);
    }

    @Override
    public void songs(List<Song> songs) {
        musicPlaylist.setAlbumId(album.id);
        musicPlaylist.setTitle(album.title);
        setSongList(songs);
        refreshView.enableSwipeRefresh(false);
    }

    @Override
    public void onSongChanged(Song song) {
        super.onSongChanged(song);
        isPlayingAlbum = MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == album.id;
    }

    @Override
    public void fail(String msg) {
        refreshView.notifySwipeFinish();
    }
}
