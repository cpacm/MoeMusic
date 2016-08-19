package com.cpacm.moemusic.ui.album;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.OnChangedListener;
import com.cpacm.moemusic.music.Song;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.widgets.BitmapBlurHelper;
import com.cpacm.moemusic.ui.widgets.RefreshRecyclerView;

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

    private WikiBean wikiBean;

    private MusicPlayPresenter mpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        mpPresenter = new MusicPlayPresenter(this);
        wikiBean = (WikiBean) getIntent().getSerializableExtra("wiki");
        if (wikiBean == null) {
            showSnackBar(getString(R.string.music_message_error));
            finish();
        }

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
        cover = (ImageView) findViewById(R.id.album_cover);
        detailTv = (TextView) findViewById(R.id.detail);
        favFAB = (FloatingActionButton) findViewById(R.id.fab);

        mpPresenter.parseWiki(wikiBean);
    }

    @Override
    public void wikiDetail(long wikiId, Spanned title, Spanned description) {
        MoeLogger.d(wikiId + "");
        getSupportActionBar().setTitle(title);
        if (description != null) {
            detailTv.setText(description);
            changeCoverPosition();
        }
    }

    @Override
    public void wikiCover(Bitmap coverBitmap) {
        cover.setImageBitmap(coverBitmap);
        Bitmap bd = BitmapBlurHelper.doBlur(this, coverBitmap, 10);
        blurImg.setImageBitmap(bd);
        setBgPalette(coverBitmap);
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
/*        refreshView = (RefreshRecyclerView) findViewById(R.id.refresh_view);
        refreshView.setLayoutManager(new LinearLayoutManager(this));
        refreshView.setLoadEnable(false);
        refreshView.setRefreshListener(this);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
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

    @Override
    public void onSwipeRefresh() {
    }

    @Override
    public void onLoadMore() {

    }

}