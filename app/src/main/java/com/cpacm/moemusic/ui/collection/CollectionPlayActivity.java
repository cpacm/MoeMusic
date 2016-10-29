package com.cpacm.moemusic.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.mvp.views.CollectionPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.MusicPlayerAdapter;
import com.cpacm.moemusic.ui.adapters.OnItemClickListener;
import com.cpacm.moemusic.ui.music.MusicDetailActivity;
import com.cpacm.moemusic.ui.music.SongPlayerActivity;
import com.cpacm.moemusic.ui.widgets.BitmapBlurHelper;
import com.cpacm.moemusic.ui.widgets.floatingmusicmenu.FloatingMusicMenu;
import com.cpacm.moemusic.ui.widgets.recyclerview.RefreshRecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author: cpacm
 * @date: 2016/9/30
 * @desciption: 收藏夹播放列表
 */
public class CollectionPlayActivity extends MusicDetailActivity implements CollectionPlayIView {

    public static void open(Context context, CollectionBean collection) {
        context.startActivity(getIntent(context, collection));
    }

    public static Intent getIntent(Context context, CollectionBean collection) {
        Intent intent = new Intent();
        intent.setClass(context, CollectionPlayActivity.class);
        intent.putExtra("collection", collection);
        return intent;
    }

    private CollectionBean collection;
    private CollectionPlayPresenter cpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //移除收藏按钮
        removeFav();

        collection = (CollectionBean) getIntent().getSerializableExtra("collection");
        if (collection == null) {
            showSnackBar(refreshView,R.string.music_message_error);
            finish();
        }

        cpPresenter = new CollectionPlayPresenter(this, collection);
    }


    @Override
    public void collectionDetail(int collectionId, Spanned title, Spanned description) {
        getSupportActionBar().setTitle(title);
        setMusicDetail(title, description);
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == collectionId) {
            isPlayingAlbum = true;
        }
    }

    @Override
    public void collectionCover(Bitmap coverBitmap) {
        setMusicCover(coverBitmap);
    }

    @Override
    public void getSongs(List<Song> songs) {
        musicPlaylist.setAlbumId(collection.getId());
        musicPlaylist.setTitle(collection.getTitle());
        setSongList(songs);
    }

    @Override
    public void fail() {
        refreshView.notifySwipeFinish();
    }

    @Override
    public void onSwipeRefresh() {
        cpPresenter.init();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onSongChanged(Song song) {
        super.onSongChanged(song);
        isPlayingAlbum = MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == collection.getId();
    }

    @Override
    protected void showPopupMenu(final View v, final Song song, final int position) {
        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        playSong(position);
                        break;
                    case R.id.popup_song_addto_playlist:
                        addToPlaylist(song);
                        break;
                    case R.id.popup_song_delete:
                        CollectionManager.getInstance().deleteCollectionShip(collection.getId(), (int) song.getId());
                        refreshView.notifySwipeFinish();
                        cpPresenter.refresh();
                        break;
                    case R.id.popup_song_download:
                        downloadSong(v,song);
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_collection_song_setting);
        menu.show();
    }
}
