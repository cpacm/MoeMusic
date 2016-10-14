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

public class CollectionPlayActivity extends AbstractAppActivity implements RefreshRecyclerView.RefreshListener, View.OnClickListener, CollectionPlayIView, OnSongChangedListener {

    public static void open(Context context, CollectionBean collection) {
        context.startActivity(getIntent(context, collection));
    }

    public static Intent getIntent(Context context, CollectionBean collection) {
        Intent intent = new Intent();
        intent.setClass(context, CollectionPlayActivity.class);
        intent.putExtra("collection", collection);
        return intent;
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewGroup animeRootLayout;
    private ImageView blurImg, cover;
    private TextView detailTv;
    private FloatingActionButton playFAB, detailFAB, addListFAB;
    private FloatingMusicMenu collectionMenu;
    private RefreshRecyclerView refreshView;

    private MusicPlayerAdapter musicAdapter;
    private CollectionBean collection;
    private boolean isPlayingCollection;

    private CollectionPlayPresenter cpPresenter;
    private MusicPlaylist musicPlaylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_playlist);

        collection = (CollectionBean) getIntent().getSerializableExtra("collection");
        if (collection == null) {
            showSnackBar(getString(R.string.music_message_error));
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(500);
        }

        musicPlaylist = new MusicPlaylist();
        initToolBar();
        initRefreshView();

        isPlayingCollection = false;
        MusicPlayerManager.get().registerListener(this);

        cpPresenter = new CollectionPlayPresenter(this, collection);
        cpPresenter.init();

    }

    private void initToolBar() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animeRootLayout = (ViewGroup) findViewById(R.id.anime_root);
        blurImg = (ImageView) findViewById(R.id.blur_img);
        cover = (ImageView) findViewById(R.id.cover);
        detailTv = (TextView) findViewById(R.id.detail);

        initMusicMenu();

    }

    private void initMusicMenu() {
        collectionMenu = (FloatingMusicMenu) findViewById(R.id.fmm);
        playFAB = (FloatingActionButton) findViewById(R.id.fab_playall);
        playFAB.setOnClickListener(this);
        detailFAB = (FloatingActionButton) findViewById(R.id.fab_detail);
        detailFAB.setOnClickListener(this);
        addListFAB = (FloatingActionButton) findViewById(R.id.fab_addlist);
        addListFAB.setOnClickListener(this);
        collectionMenu.removeButton(playFAB);
        collectionMenu.removeButton(addListFAB);
    }

    private void initData() {
        if (isPlayingCollection) {
            if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
                playFAB.setImageResource(R.drawable.ic_album_pause);
                collectionMenu.rotateStart();
            } else {
                playFAB.setImageResource(R.drawable.ic_album_playall);
                collectionMenu.rotateStop();
            }
        } else {
            playFAB.setImageResource(R.drawable.ic_album_playall);
        }
    }

    private void initRefreshView() {
        musicAdapter = new MusicPlayerAdapter(this);

        refreshView = (RefreshRecyclerView) findViewById(R.id.refresh_view);
        refreshView.setLoadEnable(false);
        refreshView.setRefreshListener(this);
        refreshView.setAdapter(musicAdapter);
        refreshView.startSwipeAfterViewCreate();

        if (MusicPlayerManager.get().getPlayingSong() != null) {
            musicAdapter.setPlayingId(MusicPlayerManager.get().getPlayingSong().getId());
        }
        musicAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayerManager.get().playQueue(musicPlaylist, position);
                gotoSongPlayerActivity();
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });
    }

    @Override
    public void collectionDetail(int collectionId, Spanned title, Spanned description) {
        MoeLogger.d(collectionId + "");
        getSupportActionBar().setTitle(title);
        if (description != null) {
            detailTv.setText(description);
            changeCoverPosition();
        }
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == collectionId) {
            isPlayingCollection = true;
        }
    }

    @Override
    public void collectionCover(Bitmap coverBitmap) {
        cover.setImageBitmap(coverBitmap);
        Bitmap bd = BitmapBlurHelper.doBlur(this, coverBitmap, 8);
        blurImg.setImageBitmap(bd);
        setBgPalette(coverBitmap);
        collectionMenu.setMusicCover(coverBitmap);
        initData();
    }

    @Override
    public void getSongs(List<Song> songs) {
        musicPlaylist.setQueue(songs);
        musicPlaylist.setAlbumId(collection.getId());
        musicPlaylist.setTitle(collection.getTitle());
        musicAdapter.setData(songs);
        refreshView.notifySwipeFinish();
        refreshView.enableSwipeRefresh(false);
        collectionMenu.addButton(addListFAB);
        collectionMenu.addButton(playFAB);
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
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
    }

    private void changeCoverPosition() {
        Action1 action1 = new Action1() {
            @Override
            public void call(Object o) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(animeRootLayout);
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cover.getLayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                } else {
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                }
                cover.setLayoutParams(layoutParams);
                detailTv.setVisibility(View.VISIBLE);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Observable.timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(action1);
        } else {
            action1.call(null);
        }
    }

    /**
     * Palette.Swatch s = p.getVibrantSwatch();       //获取到充满活力的这种色调
     * Palette.Swatch s = p.getDarkVibrantSwatch();    //获取充满活力的黑
     * Palette.Swatch s = p.getLightVibrantSwatch();   //获取充满活力的亮
     * Palette.Swatch s = p.getMutedSwatch();           //获取柔和的色调
     * Palette.Swatch s = p.getDarkMutedSwatch();      //获取柔和的黑
     * Palette.Swatch s = p.getLightMutedSwatch();    //获取柔和的亮
     */
    public void setBgPalette(final Bitmap bitmap) {
        // Palette的部分
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //活力色调
                Palette.Swatch tabSwatch = palette.getVibrantSwatch();
                int tabTextColor = tabSwatch == null ? getResources().getColor(R.color.colorPrimary) : tabSwatch.getRgb();
                collapsingToolbarLayout.setContentScrimColor(tabTextColor);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_playall:
                if (!isPlayingCollection) {
                    MusicPlayerManager.get().playQueue(musicPlaylist, 0);
                    gotoSongPlayerActivity();
                    collectionMenu.collapse();
                } else {
                    if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
                        MusicPlayerManager.get().pause();
                        collectionMenu.rotateStop();
                    } else if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PAUSED) {
                        MusicPlayerManager.get().play();
                        collectionMenu.rotateStart();
                    }
                }
                break;
            case R.id.fab_detail:
                gotoSongPlayerActivity();
                collectionMenu.collapse();
                break;
            case R.id.fab_addlist:
                MusicPlaylist mp = MusicPlayerManager.get().getMusicPlaylist();
                if (mp == null) {
                    mp = new MusicPlaylist();
                    MusicPlayerManager.get().setMusicPlaylist(mp);
                }
                mp.addQueue(musicPlaylist.getQueue(), false);
                break;
        }
    }

    @Override
    public void onSongChanged(Song song) {
        if (MusicPlayerManager.get().getPlayingSong() != null) {
            musicAdapter.setPlayingId(MusicPlayerManager.get().getPlayingSong().getId());
            refreshView.notifyDataSetChanged();
        }
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == collection.getId()) {
            isPlayingCollection = true;
        } else {
            isPlayingCollection = false;
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu(View v, final Song song, final int position) {

        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        MusicPlayerManager.get().playQueue(musicPlaylist, position);
                        gotoSongPlayerActivity();
                        break;
                    case R.id.popup_song_addto_playlist:
                        MusicPlaylist mp = MusicPlayerManager.get().getMusicPlaylist();
                        if (mp == null) {
                            mp = new MusicPlaylist();
                            MusicPlayerManager.get().setMusicPlaylist(mp);
                        }
                        mp.addSong(song);
                        showSnackBar(getString(R.string.song_add_playlist));
                        break;
                    case R.id.popup_song_delete:
                        CollectionManager.getInstance().deleteCollectionShip(collection.getId(),(int)song.getId());
                        musicAdapter.notifyDataSetChanged();
                        cpPresenter.refresh();
                        break;
                    case R.id.popup_song_download:
                        showSnackBar(getString(R.string.song_add_download));
                        SongManager.getInstance().download(song);
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_collection_song_setting);
        menu.show();
    }
}
