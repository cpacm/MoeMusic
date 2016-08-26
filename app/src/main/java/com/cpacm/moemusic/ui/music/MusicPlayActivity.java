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
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spanned;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.core.bean.FavBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.http.RxBus;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.event.FavEvent;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.OnChangedListener;
import com.cpacm.core.bean.Song;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.MusicPlayerAdapter;
import com.cpacm.moemusic.ui.widgets.BitmapBlurHelper;
import com.cpacm.moemusic.ui.widgets.RefreshRecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author: cpacm
 * @date: 2016/7/20
 * @desciption: 专辑播放页
 */
public class MusicPlayActivity extends AbstractAppActivity implements MusicPlayIView, RefreshRecyclerView.RefreshListener, View.OnClickListener, OnChangedListener {

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
    private FloatingActionButton favFAB;
    private RefreshRecyclerView refreshView;

    private MusicPlayerAdapter musicAdapter;
    private WikiBean wikiBean;

    private MusicPlayPresenter mpPresenter;

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
        favFAB = (FloatingActionButton) findViewById(R.id.fav_fab);
        favFAB.setOnClickListener(this);
        mpPresenter.parseWiki(wikiBean);
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
    }

    @Override
    public void songs(List<Song> songs) {
        musicAdapter.setData(songs);
        refreshView.notifySwipeFinish();
        refreshView.enableSwipeRefresh(false);
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
            case R.id.fav_fab:
                if (wikiBean.getWiki_user_fav() == null) {
                    showFavDialog();
                } else {
                    showUnfavDialog();
                }
                break;
        }
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
        //playListAdapter.setPlayingId(song.getSongId());
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {
        if (state.getState() == PlaybackStateCompat.STATE_PAUSED) {
            //songToggle.setImageResource(R.drawable.music_pause);
        } else if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            //songToggle.setImageResource(R.drawable.music_play);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}