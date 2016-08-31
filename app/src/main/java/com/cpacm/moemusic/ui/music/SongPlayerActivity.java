package com.cpacm.moemusic.ui.music;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
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
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.widgets.CircleImageView;
import com.cpacm.moemusic.ui.widgets.CircularSeekBar;
import com.cpacm.moemusic.ui.widgets.timely.TimelyView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author: cpacm
 * @date: 2016/8/24
 * @desciption: 播放器界面
 */
public class SongPlayerActivity extends AbstractAppActivity implements OnSongChangedListener, View.OnClickListener {

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

    private Subscription progressSub, timerSub;
    private int[] times = new int[]{-1, -1, -1, -1, -1};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicPlayerManager.get().registerListener(this);

        initData();
        initView();
        updateProgress();
        updateData();
    }

    private void initData() {
        String path = FileUtils.getSDCardFilePath("xiami/Heaven's Sky.mp3");
        song = MusicPlayerManager.get().getPlayingSong();
        if (song == null) {
            song = new Song(1L, "test", 2, "test", 3, "cpacm", Uri.parse(path), 100, 1000, 12512, "320k", 12344, "test", "http://moefou.90g.org/wiki_cover/000/04/02/000040276_192.jpg?v=1401731676", true);
            List<Song> songs = new ArrayList<>();
            songs.add(song);
            MusicPlaylist musicPlaylist = new MusicPlaylist();
            musicPlaylist.setQueue(songs);
            MusicPlayerManager.get().playQueue(musicPlaylist, 0);
            MoeLogger.e("没有找到正在播放的歌曲");
            //finish();
        }
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
        playBtn = (FloatingActionButton) findViewById(R.id.song_play);

        //seekbar
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerManager.get().seekTo(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        //背景频谱
        visualizationView = (GLAudioVisualizationView) findViewById(R.id.visualizer_view);
        visualizationView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, MusicPlayerManager.get().getMediaPlayer().getAudioSessionId()));

        randomImg.setOnClickListener(this);
        previousImg.setOnClickListener(this);
        nextImg.setOnClickListener(this);
        downloadImg.setOnClickListener(this);
        playBtn.setOnClickListener(this);
    }

    private void updateData() {
        //歌曲封面
        String coverUrl = song.getCoverUrl();
        Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.cover)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        circleCover.setImageDrawable(resource);
                    }
                });

        titleTv.setText(song.getTitle());
        updatePlayStatus();
    }

    private void updateProgress() {
        progressSub = Observable.interval(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        circularSeekBar.setMax(MusicPlayerManager.get().getCurrentMaxDuration());
                        circularSeekBar.setProgress(MusicPlayerManager.get().getCurrentPosition());
                    }
                });
        timerSub = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        setTime(MusicPlayerManager.get().getCurrentPosition());
                    }
                });
    }

    public void setTime(int progress) {
        String time = DateUtils.getTime(progress).toString();
        int length = time.length();
        if (time.length() == 5) {
            hourTv.setVisibility(View.GONE);
            hourColon.setVisibility(View.GONE);
            changeDigit(min1Tv, 1, time.charAt(0) - '0');
            changeDigit(min2Tv, 2, time.charAt(1) - '0');
            changeDigit(sec1Tv, 3, time.charAt(3) - '0');
            changeDigit(sec2Tv, 4, time.charAt(4) - '0');
        } else if (time.length() > 5) {
            hourColon.setVisibility(View.VISIBLE);
            hourTv.setVisibility(View.VISIBLE);
            changeDigit(sec2Tv, 4, time.charAt(length - 1) - '0');
            changeDigit(sec1Tv, 3, time.charAt(length - 2) - '0');
            changeDigit(min2Tv, 2, time.charAt(length - 4) - '0');
            changeDigit(min1Tv, 1, time.charAt(length - 5) - '0');
            changeDigit(hourTv, 0, time.charAt(length - 7) - '0');
        }
    }

    public void changeDigit(TimelyView tv, int position, int end) {
        int digit = times[position];
        if (digit == end)
            return;
        try {
            ObjectAnimator obja = tv.animate(digit, end);
            obja.setDuration(400);
            obja.start();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        times[position] = end;
    }

    @Override
    public void onSongChanged(Song song) {

    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.song_mode:
                int playMode = MusicPlayerManager.get().switchPlayMode();
                if (playMode == MusicPlayerManager.CYCLETYPE) {
                    showToast(R.string.music_mode_cycle);
                    randomImg.setImageResource(R.drawable.ic_play_repeat);
                } else if (playMode == MusicPlayerManager.SINGLETYPE) {
                    randomImg.setImageResource(R.drawable.ic_play_repeat_one);
                    showToast(R.string.music_mode_single);
                } else if (playMode == MusicPlayerManager.RANDOMTYPE) {
                    randomImg.setImageResource(R.drawable.ic_play_shuffle);
                    showToast(R.string.music_mode_random);
                }
                break;
            case R.id.song_previous:
                MusicPlayerManager.get().playPrev();
                break;
            case R.id.song_next:
                MusicPlayerManager.get().playNext();
                break;
            case R.id.song_download:
                break;
            case R.id.song_play:
                if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    MusicPlayerManager.get().pause();
                } else if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    MusicPlayerManager.get().play();
                }
                updatePlayStatus();
                break;
        }
    }

    private void updatePlayStatus() {
        if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
            playBtn.setImageResource(R.drawable.ic_play);
        } else if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PAUSED) {
            playBtn.setImageResource(R.drawable.ic_pause);
        }
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
        MusicPlayerManager.get().unregisterListener(this);
        progressSub.unsubscribe();
        timerSub.unsubscribe();
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
