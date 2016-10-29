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

/**
 * @author: cpacm
 * @date: 2016/7/20
 * @desciption: 萌否信息详情页
 */
public class MoeDetailActivity extends MusicDetailActivity implements MusicPlayIView {

    public static void open(Context context, WikiBean wiki) {
        context.startActivity(getIntent(context, wiki));
    }

    public static Intent getIntent(Context context, WikiBean wiki) {
        Intent intent = new Intent();
        intent.setClass(context, MoeDetailActivity.class);
        intent.putExtra("wiki", wiki);
        return intent;
    }

    private WikiBean wikiBean;
    private MusicPlayPresenter mpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wikiBean = (WikiBean) getIntent().getSerializableExtra("wiki");
        if (wikiBean == null) {
            showSnackBar(getString(R.string.music_message_error));
            finish();
        }
        mpPresenter = new MusicPlayPresenter(this, wikiBean.getWiki_type());
        mpPresenter.parseWiki(wikiBean);
    }


    @Override
    public void wikiDetail(long wikiId, Spanned title, Spanned description, boolean fav) {
        MoeLogger.d(wikiId + "");
        setMusicDetail(title, description, fav);
        if (MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == wikiId) {
            isPlayingAlbum = true;
        }
    }

    @Override
    public void favMusic(boolean fav) {
        RxBus.getDefault().post(new FavEvent(wikiBean.getWiki_id(), fav));
        if (fav) {
            wikiBean.setWiki_user_fav(new FavBean());
        } else {
            wikiBean.setWiki_user_fav(null);
        }
        super.favMusic(fav);
    }

    @Override
    public void wikiCover(Bitmap coverBitmap) {
        setMusicCover(coverBitmap);
    }

    @Override
    public void songs(List<Song> songs) {
        musicPlaylist.setAlbumId(wikiBean.getWiki_id());
        musicPlaylist.setTitle(wikiBean.getWiki_title());
        setSongList(songs);
    }

    @Override
    public void onSongChanged(Song song) {
        super.onSongChanged(song);
        isPlayingAlbum = MusicPlayerManager.get().getMusicPlaylist() != null && MusicPlayerManager.get().getMusicPlaylist().getAlbumId() == wikiBean.getWiki_id();
    }

    @Override
    public void fail(String msg) {
        refreshView.notifySwipeFinish();
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
        }
        super.onClick(v);
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
}