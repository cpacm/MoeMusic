package com.cpacm.moemusic.ui.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spanned;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.bean.FavBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.bean.event.CollectionUpdateEvent;
import com.cpacm.core.bean.event.FavEvent;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.http.RxBus;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
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
import rx.subscriptions.CompositeSubscription;

/**
 * @author: cpacm
 * @date: 2016/7/20
 * @desciption: 专辑播放页
 */
public class MusicPlayActivity extends AbstractAppActivity implements MusicPlayIView, RefreshRecyclerView.RefreshListener, View.OnClickListener, OnSongChangedListener {

    public static void open(Context context, WikiBean wiki) {
        context.startActivity(getIntent(context, wiki));
    }

    public static Intent getIntent(Context context, WikiBean wiki) {
        Intent intent = new Intent();
        intent.setClass(context, MusicPlayActivity.class);
        intent.putExtra("wiki", wiki);
        return intent;
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewGroup animeRootLayout;
    private ImageView blurImg, cover;
    private TextView detailTv;
    private FloatingActionButton favFAB, playFAB, detailFAB, addListFAB;
    private FloatingMusicMenu musicMenu;
    private RefreshRecyclerView refreshView;

    private MusicPlayerAdapter musicAdapter;
    private WikiBean wikiBean;
    private boolean isPlayingAlbum;

    private MusicPlayPresenter mpPresenter;
    private MusicPlaylist musicPlaylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playlist);

        wikiBean = (WikiBean) getIntent().getSerializableExtra("wiki");
        if (wikiBean == null) {
            showSnackBar(getString(R.string.music_message_error));
            finish();
        }
        mpPresenter = new MusicPlayPresenter(this, wikiBean.getWiki_type());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(500);
        }

        isPlayingAlbum = false;
        musicPlaylist = new MusicPlaylist();
        initToolBar();
        initRefreshView();

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

        mpPresenter.parseWiki(wikiBean);

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
        musicMenu.removeButton(playFAB);
        musicMenu.removeButton(addListFAB);
    }

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

    @Override
    public void wikiDetail(long wikiId, Spanned title, Spanned description, boolean fav) {
        MoeLogger.d(wikiId + "");
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
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == wikiId) {
            isPlayingAlbum = true;
        }
    }

    @Override
    public void favMusic(boolean fav) {
        RxBus.getDefault().post(new FavEvent(wikiBean.getWiki_id(), fav));
        if (fav) {
            wikiBean.setWiki_user_fav(new FavBean());
            favFAB.setImageResource(R.drawable.ic_star_fav);
        } else {
            wikiBean.setWiki_user_fav(null);
            favFAB.setImageResource(R.drawable.ic_star_unfav);
        }
    }

    @Override
    public void wikiCover(Bitmap coverBitmap) {
        cover.setImageBitmap(coverBitmap);
        Bitmap bd = BitmapBlurHelper.doBlur(this, coverBitmap, 8);
        blurImg.setImageBitmap(bd);
        setBgPalette(coverBitmap);
        musicMenu.setMusicCover(coverBitmap);
        initData();
    }

    @Override
    public void songs(List<Song> songs) {
        musicPlaylist.setQueue(songs);
        musicPlaylist.setAlbumId(wikiBean.getWiki_id());
        musicPlaylist.setTitle(wikiBean.getWiki_title());
        musicAdapter.setData(songs);
        refreshView.notifySwipeFinish();
        refreshView.enableSwipeRefresh(false);
        musicMenu.addButton(addListFAB);
        musicMenu.addButton(playFAB);
    }

    @Override
    public void fail(String msg) {
        refreshView.notifySwipeFinish();
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
    public void onSwipeRefresh() {
        mpPresenter.getSongs(wikiBean.getWiki_id());
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_fav:
                if (wikiBean.getWiki_user_fav() == null) {
                    showFavDialog();
                } else {
                    showUnfavDialog();
                }
                break;
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
                break;
        }
    }

    public boolean gotoSongPlayerActivity() {
        if (MusicPlayerManager.get().getPlayingSong() == null) {
            showToast(R.string.music_playing_none);
            return false;
        }
        SongPlayerActivity.open(this);
        return true;
    }

    public void showFavDialog() {
        new MaterialDialog.Builder(this)
                .title(wikiBean.getWiki_title())
                .content(R.string.music_fav)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(0, 50)
                .positiveText(R.string.fav)
                .negativeText(R.string.cancel)
                .input(getString(R.string.evaluation), "", true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        mpPresenter.favMusic(wikiBean.getWiki_id(), input.toString());
                    }
                }).show();
    }

    public void showUnfavDialog() {
        new MaterialDialog.Builder(this)
                .title(wikiBean.getWiki_title())
                .content(R.string.music_unfav)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mpPresenter.unFavMusic(wikiBean.getWiki_id());
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
    }


    @Override
    public void onSongChanged(Song song) {
        if (MusicPlayerManager.get().getPlayingSong() != null) {
            musicAdapter.setPlayingId(MusicPlayerManager.get().getPlayingSong().getId());
            refreshView.notifyDataSetChanged();
        }
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == wikiBean.getWiki_id()) {
            isPlayingAlbum = true;
        } else {
            isPlayingAlbum = false;
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {
        initData();
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
                        break;
                    case R.id.popup_song_fav:
                        showCollectionDialog(song);
                        break;
                    case R.id.popup_song_download:
                        showSnackBar(getString(R.string.song_add_download));
                        SongManager.getInstance().download(song);
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_song_setting);
        menu.show();
    }

    /**
     * 显示选择收藏夹列表的弹窗
     *
     * @param song
     */
    public void showCollectionDialog(final Song song) {
        CollectionAdapter collectionAdapter = new CollectionAdapter(this, true);
        final MaterialDialog dialog = new MaterialDialog.Builder(MusicPlayActivity.this)
                .title(R.string.collection_dialog_selection_title)
                .adapter(collectionAdapter, new LinearLayoutManager(this))
                .build();
        collectionAdapter.setItemClickListener(new OnItemClickListener<CollectionBean>() {
            @Override
            public void onItemClick(CollectionBean item, int position) {
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