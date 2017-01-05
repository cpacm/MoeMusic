package com.cpacm.moemusic.ui.music;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.event.CollectionUpdateEvent;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.http.RxBus;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.permission.OnPermissionsDeniedListener;
import com.cpacm.moemusic.permission.OnPermissionsGrantedListener;
import com.cpacm.moemusic.permission.PermissionBuilder;
import com.cpacm.moemusic.permission.PermissionManager;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.PermissionActivity;
import com.cpacm.moemusic.ui.adapters.CollectionAdapter;
import com.cpacm.moemusic.ui.adapters.MusicPlayerAdapter;
import com.cpacm.moemusic.ui.adapters.OnItemClickListener;
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
 * @date: 2016/10/18
 * @desciption: 音乐专辑详情页
 */

public abstract class MusicDetailActivity extends PermissionActivity implements RefreshRecyclerView.RefreshListener, View.OnClickListener, OnSongChangedListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewGroup animeRootLayout;
    private ImageView blurImg, cover;
    private TextView detailTv;
    private FloatingActionButton favFAB, playFAB, detailFAB, addListFAB,downloadFAB;
    private FloatingMusicMenu musicMenu;
    protected RefreshRecyclerView refreshView;

    protected boolean isPlayingAlbum;

    private MusicPlayerAdapter musicAdapter;
    protected MusicPlaylist musicPlaylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(500);
        }

        musicPlaylist = new MusicPlaylist();
        initToolBar();
        initRefreshView();

        isPlayingAlbum = false;
        MusicPlayerManager.get().registerListener(this);

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
        musicMenu = (FloatingMusicMenu) findViewById(R.id.fmm);
        favFAB = (FloatingActionButton) findViewById(R.id.fab_fav);
        favFAB.setOnClickListener(this);
        playFAB = (FloatingActionButton) findViewById(R.id.fab_playall);
        playFAB.setOnClickListener(this);
        detailFAB = (FloatingActionButton) findViewById(R.id.fab_detail);
        detailFAB.setOnClickListener(this);
        addListFAB = (FloatingActionButton) findViewById(R.id.fab_addlist);
        addListFAB.setOnClickListener(this);
        downloadFAB = (FloatingActionButton) findViewById(R.id.fab_download);
        downloadFAB.setOnClickListener(this);
        musicMenu.removeButton(playFAB);
        musicMenu.removeButton(addListFAB);
        musicMenu.removeButton(downloadFAB);
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

    /**
     * 歌曲播放状态变化
     */
    private void initData() {
        if (isPlayingAlbum) {
            if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
                playFAB.setImageResource(R.drawable.ic_album_pause);
                musicMenu.rotateStart();
            } else {
                playFAB.setImageResource(R.drawable.ic_album_playall);
                musicMenu.rotateStop();
            }
        } else {
            playFAB.setImageResource(R.drawable.ic_album_playall);
        }
    }

    /**
     * 封面动画
     */
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
                    .subscribe(action1, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            MoeLogger.e(throwable.toString());
                        }
                    });
        } else {
            action1.call(null);
        }
    }

    /**
     * 设置专辑细节
     *
     * @param title
     * @param description
     * @param fav
     */
    public void setMusicDetail(Spanned title, Spanned description, boolean fav) {
        getSupportActionBar().setTitle(title);
        if (description != null) {
            detailTv.setText(description);
            changeCoverPosition();
        }
        if (fav) {
            favFAB.setImageResource(R.drawable.ic_star_fav);
        } else {
            favFAB.setImageResource(R.drawable.ic_star_unfav);
        }
    }

    /**
     * 设置专辑细节
     *
     * @param title
     * @param description
     */
    public void setMusicDetail(Spanned title, Spanned description) {
        getSupportActionBar().setTitle(title);
        if (description != null) {
            detailTv.setText(description);
            changeCoverPosition();
        }
    }

    /**
     * 收藏专辑
     *
     * @param fav
     */
    public void favMusic(boolean fav) {
        if (fav) {
            favFAB.setImageResource(R.drawable.ic_star_fav);
        } else {
            favFAB.setImageResource(R.drawable.ic_star_unfav);
        }
    }

    /**
     * 设置封面
     *
     * @param coverBitmap
     */
    public void setMusicCover(Bitmap coverBitmap) {
        cover.setImageBitmap(coverBitmap);
        Bitmap bd = BitmapBlurHelper.doBlur(this, coverBitmap, 8);
        blurImg.setImageBitmap(bd);
        setBgPalette(coverBitmap);
        musicMenu.setMusicCover(coverBitmap);
        initData();
    }

    /**
     * 设置歌曲列表
     *
     * @param songs
     */
    public void setSongList(List<Song> songs) {
        // 排除掉无法播放的歌曲
        for (Song song : songs) {
            if (song.isStatus()) {
                musicPlaylist.addSong(song);
            }
        }
        musicAdapter.setData(songs);
        refreshView.notifySwipeFinish();
        refreshView.enableSwipeRefresh(false);
        if (musicPlaylist.getQueue().size() > 0) {
            musicMenu.addButton(addListFAB);
            musicMenu.addButton(playFAB);
        }
    }

    /**
     * 添加下载按钮
     */
    public void addDownload(){
        musicMenu.addButton(downloadFAB);
    }

    /**
     * 移除收藏按钮
     */
    public void removeFav() {
        musicMenu.removeButton(favFAB);
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
                if (tabSwatch != null) {
                    int color = tabSwatch.getRgb();
                    collapsingToolbarLayout.setContentScrimColor(color);
                    return;
                }
                tabSwatch = palette.getMutedSwatch();
                int color = tabSwatch == null ? getResources().getColor(R.color.colorPrimary) : tabSwatch.getRgb();
                collapsingToolbarLayout.setContentScrimColor(color);
            }
        });
    }

    @Override
    public void onSongChanged(Song song) {
        if (MusicPlayerManager.get().getPlayingSong() != null) {
            musicAdapter.setPlayingId(MusicPlayerManager.get().getPlayingSong().getId());
            refreshView.notifyDataSetChanged();
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_playall:
                if (!isPlayingAlbum) {
                    MusicPlayerManager.get().playQueue(musicPlaylist, 0);
                    gotoSongPlayerActivity();
                    musicMenu.collapse();
                } else {
                    if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
                        MusicPlayerManager.get().pause();
                        musicMenu.rotateStop();
                    } else if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PAUSED) {
                        MusicPlayerManager.get().play();
                        musicMenu.rotateStart();
                    }
                }
                break;
            case R.id.fab_detail:
                gotoSongPlayerActivity();
                musicMenu.collapse();
                break;
            case R.id.fab_addlist:
                MusicPlaylist mp = MusicPlayerManager.get().getMusicPlaylist();
                if (mp == null) {
                    mp = new MusicPlaylist();
                    MusicPlayerManager.get().setMusicPlaylist(mp);
                }
                mp.addQueue(musicPlaylist.getQueue(), false);
                showSnackBar(refreshView,R.string.song_add_playlist);
                break;
        }
    }

    /**
     * 打开更多弹窗
     *
     * @param v
     * @param song
     * @param position
     */
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
                    case R.id.popup_song_fav:
                        showCollectionDialog(song);
                        break;
                    case R.id.popup_song_download:
                        downloadSong(v,song);
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_song_setting);
        menu.show();
    }

    /**
     * 播放歌曲
     *
     * @param position
     */
    protected void playSong(int position) {
        MusicPlayerManager.get().playQueue(musicPlaylist, position);
        gotoSongPlayerActivity();
    }

    /**
     * 添加歌曲到播放列表
     *
     * @param song
     */
    protected void addToPlaylist(Song song) {
        MusicPlaylist mp = MusicPlayerManager.get().getMusicPlaylist();
        if (mp == null) {
            mp = new MusicPlaylist();
            MusicPlayerManager.get().setMusicPlaylist(mp);
        }
        mp.addSong(song);
        showSnackBar(refreshView,R.string.song_add_playlist);
    }


    /**
     * 显示选择收藏夹列表的弹窗
     *
     * @param song
     */
    public void showCollectionDialog(final Song song) {
        CollectionAdapter collectionAdapter = new CollectionAdapter(this, true);
        final MaterialDialog dialog = new MaterialDialog.Builder(MusicDetailActivity.this)
                .title(R.string.collection_dialog_selection_title)
                .adapter(collectionAdapter, new LinearLayoutManager(this))
                .build();
        collectionAdapter.setItemClickListener(new OnItemClickListener<CollectionBean>() {
            @Override
            public void onItemClick(CollectionBean item, int position) {
                if (item == null) {
                    dialog.dismiss();
                    return;
                }
                CollectionManager.getInstance().insertCollectionShipAsync(item, song, new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        dialog.dismiss();
                        showToast(aBoolean ? R.string.collect_song_success : R.string.collect_song_fail);
                        RxBus.getDefault().post(new CollectionUpdateEvent(aBoolean));//通知首页收藏夹数据变化
                    }
                });

            }

            @Override
            public void onItemSettingClick(View v, CollectionBean item, int position) {

            }
        });
        dialog.show();
    }

}