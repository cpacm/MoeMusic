package com.cpacm.moemusic.ui.album;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpacm.core.bean.MetaBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.MusicPlayIView;
import com.cpacm.core.utils.BitmapUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnChangedListener;
import com.cpacm.moemusic.music.Song;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.widgets.RefreshRecyclerView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: cpacm
 * @date: 2016/7/20
 * @desciption: 专辑播放页
 */
public class MusicPlayActivity extends AbstractAppActivity implements MusicPlayIView, RefreshRecyclerView.RefreshListener, View.OnClickListener, OnChangedListener {

    public static void open(Context context, WikiBean wiki) {
        Intent intent = new Intent();
        intent.setClass(context, MusicPlayActivity.class);
        intent.putExtra("wiki", wiki);
        context.startActivity(intent);
    }

    //private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView blurImg, cover;
    private TextView detailTv;
    private FloatingActionButton favFAB;

    private WikiBean wikiBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        wikiBean = (WikiBean) getIntent().getSerializableExtra("wiki");
        if (wikiBean == null) {
            showSnackBar(getString(R.string.music_message_error));
            finish();
        }

        //collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);
        initToolBar();
        initRefreshView();

        MusicPlayerManager.get().registerListener(this);

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getString(R.string.music_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        blurImg = (ImageView) findViewById(R.id.blur_img);
        cover = (ImageView) findViewById(R.id.album_cover);
        detailTv = (TextView) findViewById(R.id.detail);
        favFAB = (FloatingActionButton) findViewById(R.id.fab);

        Spanned title = Html.fromHtml(wikiBean.getWiki_title());
        toolbar.setTitle(title);

/*        paletteImg = (ImageView) findViewById(R.id.palette_img);
        paletteImg.setImageResource(R.drawable.music_bg2);
        setBgPalette(R.drawable.music_bg2);*/
    }

    @Override
    public void wikiDetail(long wikiId, Spanned title, Spanned decription, Bitmap cover) {

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
    public void setBgPalette(int drawableId) {
        // 用来提取颜色的Bitmap
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        // Palette的部分
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //活力色调
                Palette.Swatch tabSwatch = palette.getVibrantSwatch();
                if (tabSwatch == null)
                    tabSwatch = palette.getLightVibrantSwatch();
                if (tabSwatch == null)
                    tabSwatch = palette.getDarkVibrantSwatch();
                int tabTextColor = tabSwatch == null ? getResources().getColor(R.color.black_normal) : tabSwatch.getRgb();
                //tabLayout.setTabTextColors(getResources().getColor(R.color.white_normal), tabTextColor);

                Palette.Swatch scrimSwatch = palette.getMutedSwatch();
                if (scrimSwatch == null)
                    scrimSwatch = palette.getLightMutedSwatch();
                if (scrimSwatch == null)
                    scrimSwatch = palette.getDarkMutedSwatch();
                int scrimColor = scrimSwatch == null ? getResources().getColor(R.color.colorPrimary) : scrimSwatch.getRgb();
                //根据调色板Palette获取到图片中的颜色设置到toolbar和tab中背景，标题等，使整个UI界面颜色统一
                //collapsingToolbarLayout.setContentScrimColor(scrimColor);
                bitmap.recycle();
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