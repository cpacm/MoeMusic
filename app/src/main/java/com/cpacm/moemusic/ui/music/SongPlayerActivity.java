package com.cpacm.moemusic.ui.music;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.cpacm.core.bean.Song;
import com.cpacm.core.utils.DateUtils;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.widgets.CircleImageView;
import com.cpacm.moemusic.ui.widgets.CircularSeekBar;
import com.cpacm.moemusic.ui.widgets.timely.TimelyView;

import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: cpacm
 * @date: 2016/8/24
 * @desciption: 播放器界面
 */
public class SongPlayerActivity extends AbstractAppActivity implements OnChangedListener {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SongPlayerActivity.class);
        context.startActivity(intent);
    }

    private GLAudioVisualizationView visualizationView;
    private Toolbar toolbar;
    private CircularSeekBar circularSeekBar;
    private CircleImageView circleCover;
    private TimelyView hourTv, min1Tv, min2Tv, sec1Tv, sec2Tv;
    private TextView hourColon;
    private TextView titleTv;
    private ImageView randomImg, previousImg, nextImg, downloadImg;
    private FloatingActionButton playBtn;
    private Song song;

    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initView();
    }

    private void initData() {
        String path = FileUtils.getSDCardFilePath("xiami/Heaven's Sky.mp3");
        song = new Song(1L, "test", 2, "test", 3, "cpacm", Uri.parse(path), 100, 1000, 12512, "320k", 12344, "test", "http://moefou.90g.org/wiki_cover/000/04/02/000040276_192.jpg?v=1401731676", true);
        //song = MusicPlayerManager.get().getPlayingSong();
        if (song == null) {
            MoeLogger.e("没有找到正在播放的歌曲");
            finish();
        }
        MusicPlayerManager.get().play(song);
    }

    private void initView() {
        circularSeekBar = (CircularSeekBar) findViewById(R.id.circle_seekbar);
        circleCover = (CircleImageView) findViewById(R.id.song_cover);
        hourTv = (TimelyView) findViewById(R.id.timely_hour);
        min1Tv = (TimelyView) findViewById(R.id.timely_min1);
        min2Tv = (TimelyView) findViewById(R.id.timely_min2);
        sec1Tv = (TimelyView) findViewById(R.id.timely_sec1);
        sec2Tv = (TimelyView) findViewById(R.id.timely_sec2);
        hourColon = (TextView) findViewById(R.id.hour_colon);
        titleTv = (TextView) findViewById(R.id.song_title);
        randomImg = (ImageView) findViewById(R.id.song_mode);
        previousImg = (ImageView) findViewById(R.id.song_previous);
        nextImg = (ImageView) findViewById(R.id.song_next);
        downloadImg = (ImageView) findViewById(R.id.song_download);

        //seekbar
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgress(circularSeekBar.getMax(), progress);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        //歌曲封面
        String coverUrl = TextUtils.isEmpty(song.getCoverUrl()) ? MusicPlayerManager.get().getPlayCover() : song.getCoverUrl();
        Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.cover)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        circleCover.setImageDrawable(resource);
                    }
                });

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateProgress(MusicPlayerManager.get().getCurrentMaxDuration(), MusicPlayerManager.get().getCurrentPosition());
            }
        };
        timer.schedule(timerTask, 0, 100);

        setTime(0);

        //背景频谱
        visualizationView = (GLAudioVisualizationView) findViewById(R.id.visualizer_view);
        visualizationView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, MusicPlayerManager.get().getMediaPlayer().getAudioSessionId()));
    }

    private void updateProgress(final int max, final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                circularSeekBar.setMax(max);
                circularSeekBar.setProgress(progress);
            }
        });

    }

    public void setTime(int progress) {
        String time = DateUtils.getTime(progress).toString();
        int length = time.length();
        if (time.length() == 5) {
            hourTv.setVisibility(View.GONE);
            hourColon.setVisibility(View.GONE);
            changeDigit(sec1Tv, time.charAt(3) - '0');
            changeDigit(sec2Tv, time.charAt(4) - '0');
            changeDigit(min1Tv, time.charAt(0) - '0');
            changeDigit(min2Tv, time.charAt(1) - '0');
        } else if (time.length() > 5) {
            hourColon.setVisibility(View.VISIBLE);
            hourTv.setVisibility(View.VISIBLE);
            changeDigit(sec2Tv, time.charAt(length - 1) - '0');
            changeDigit(sec1Tv, time.charAt(length - 2) - '0');
            changeDigit(min2Tv, time.charAt(length - 4) - '0');
            changeDigit(min1Tv, time.charAt(length - 5) - '0');
            changeDigit(hourTv, time.charAt(length - 7) - '0');
        }
    }

    public void changeDigit(TimelyView tv, int end) {
        ObjectAnimator obja = tv.animate(end);
        obja.setDuration(400);
        obja.start();
    }

    public void changeDigit(TimelyView tv, int start, int end) {
        try {
            ObjectAnimator obja = tv.animate(start, end);
            obja.setDuration(400);
            obja.start();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSongChanged(Song song) {

    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    @Override
    public void onResume() {
        super.onResume();
        visualizationView.onResume();
    }

    @Override
    public void onPause() {
        visualizationView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        visualizationView.release();
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
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

}
