package com.cpacm.moemusic.ui.album;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpacm.core.utils.BitmapUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnChangedListener;
import com.cpacm.moemusic.music.Song;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.widgets.RefreshRecyclerView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: cpacm
 * @date: 2016/7/20
 * @desciption: 专辑播放页
 */
public class MusicPlayActivity extends AbstractAppActivity implements RefreshRecyclerView.RefreshListener, View.OnClickListener, OnChangedListener {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MusicPlayActivity.class);
        context.startActivity(intent);
    }

    //private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView paletteImg;

    private MusicPlaylist musicPlaylist;

    private View playController;

    private boolean hasController = false;
    private int playMode = MusicPlaylist.CYCLETYPE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        //collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);

        initToolBar();
        initController();
        initRefreshView();

        MusicPlayerManager.startServiceIfNecessary(getApplicationContext());
        MusicPlayerManager.get().registerListener(this);

        musicPlaylist = new MusicPlaylist();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getString(R.string.music_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*        paletteImg = (ImageView) findViewById(R.id.palette_img);
        paletteImg.setImageResource(R.drawable.music_bg2);
        setBgPalette(R.drawable.music_bg2);*/
    }

    private void initRefreshView() {
/*        refreshView = (RefreshRecyclerView) findViewById(R.id.refresh_view);
        refreshView.setLayoutManager(new LinearLayoutManager(this));
        refreshView.setLoadEnable(false);
        refreshView.setRefreshListener(this);*/
    }

    private void initController() {
       // playController = findViewById(R.id.play_controller);
    }

    public void changeControllerSong(Song song) {
        //----------定时器记录播放进度---------//
        //songArtist.setText(song.getSongArtist());
    }

    private void addPlayController() {
        if (hasController) return;
        playController.setVisibility(View.VISIBLE);
        setControllerSpace();
        hasController = true;
    }

    public void setControllerSpace() {
        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) refreshView.getLayoutParams();
        //layoutParams.bottomMargin = BitmapUtils.dp2px(56);
        //refreshView.setLayoutParams(layoutParams);
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
        MusicPlayerManager.get().stop();
    }


    @Override
    public void onSongChanged(Song song) {
        //playListAdapter.setPlayingId(song.getSongId());
        changeControllerSong(song);
        addPlayController();
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