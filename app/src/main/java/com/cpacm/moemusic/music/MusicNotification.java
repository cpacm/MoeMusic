package com.cpacm.moemusic.music;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_STOPPED;
import static android.support.v7.app.NotificationCompat.CATEGORY_TRANSPORT;
import static android.support.v7.app.NotificationCompat.PRIORITY_MAX;
import static android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.utils.BitmapUtils;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.music.SongPlayerActivity;
import com.cpacm.moemusic.utils.DrawableUtil;


/**
 * @author: cpacm
 * @date: 2016/7/19
 * @desciption: 音乐播放通知栏
 */
public class MusicNotification {

    public final static int NOTIFICATION_ID = 1101013;
    public final static int REQ_CODE = 200;

    public static final String ACTION_PLAY = "com.cpacm.moemusic.play";
    public static final String ACTION_PAUSE = "com.cpacm.moemusic.pause";
    public static final String ACTION_NEXT = "com.cpacm.moemusic.next";
    public static final String ACTION_PREV = "com.cpacm.moemusic.prev";
    public static final String ACTION_STOP = "com.cpacm.moemusic.stop";

    private static volatile NotificationCompat.Builder builder;
    private static volatile MusicService musicService;

    public static final BroadcastReceiver commandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            switch (s) {
                case ACTION_PLAY:
                    MusicPlayerManager.get().play();
                    break;
                case ACTION_PAUSE:
                    MusicPlayerManager.get().pause();
                    break;
                case ACTION_NEXT:
                    MusicPlayerManager.get().playNext();
                    break;
                case ACTION_PREV:
                    MusicPlayerManager.get().playPrev();
                    break;
                case ACTION_STOP:
                    MusicPlayerManager.get().pause();
                    if (musicService != null) {
                        musicService.removeForeground(true);
                    }
                    break;
            }
        }
    };

    public static void init(final MusicService service) {
        musicService = service;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PREV);
        filter.addAction(ACTION_STOP);
        musicService.registerReceiver(commandReceiver, filter);
        MusicPlayerManager.get().registerListener(new OnSongChangedListener() {
            @Override
            public void onSongChanged(Song song) {
            }

            @Override
            public void onPlayBackStateChanged(PlaybackStateCompat newState) {
                if (newState.getState() != STATE_NONE && newState.getState() != STATE_STOPPED) {
                    update();
                }
            }
        });
    }

    private static void setUp() {
        PendingIntent stopServiceIntent = PendingIntent.getBroadcast(musicService, REQ_CODE, new Intent(ACTION_STOP), PendingIntent.FLAG_CANCEL_CURRENT);
        builder = new NotificationCompat.Builder(musicService);
        builder.setStyle(
                new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3, 4)
                        .setMediaSession(musicService.getMediaSession().getSessionToken()).setShowCancelButton(true).setCancelButtonIntent(stopServiceIntent))
                .setSmallIcon(R.drawable.music)
                .setCategory(CATEGORY_TRANSPORT)
                .setVisibility(VISIBILITY_PUBLIC)
                .setDeleteIntent(stopServiceIntent)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getActivity(musicService, REQ_CODE,
                        new Intent(musicService, SongPlayerActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .setPriority(PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setShowWhen(false);
        }
    }

    private static void update() {
        boolean notify = SettingManager.getInstance().getSetting(SettingManager.SETTING_NOTIFY, true);
        if (!notify) {
            return;
        }
        if (builder == null) {
            setUp();
        }
        Song song = MusicPlayerManager.get().getPlayingSong();
        builder.setContentTitle(song.getTitle())
                .setContentText(song.getAlbumName())
                .setSubText(song.getArtistName());
        Glide.with(MoeApplication.getInstance())
                .load(song.getCoverUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            builder.setLargeIcon(Bitmap.createScaledBitmap(resource, BitmapUtils.dp2px(96), BitmapUtils.dp2px(96), false));
                            if (musicService.getMediaSession().isActive()) {
                                NotificationManagerCompat.from(musicService).notify(NOTIFICATION_ID, getNotification());
                            }
                        } catch (Exception e) {
                            MoeLogger.e(e.getMessage());
                        }
                    }
                });
        builder.mActions.clear();
        //The Actions
        builder.addAction(R.drawable.ic_play_skip_previous, musicService.getString(R.string.music_previous), PendingIntent.getBroadcast(musicService, REQ_CODE,
                new Intent(ACTION_PREV), PendingIntent.FLAG_CANCEL_CURRENT));

        if (musicService.getState() == STATE_PLAYING) {
            builder.addAction(R.drawable.ic_play, musicService.getString(R.string.music_pause), PendingIntent.getBroadcast(musicService, REQ_CODE,
                    new Intent(ACTION_PAUSE), PendingIntent.FLAG_CANCEL_CURRENT));
        } else {
            builder.addAction(R.drawable.ic_pause, musicService.getString(R.string.music_play), PendingIntent.getBroadcast(musicService, REQ_CODE,
                    new Intent(ACTION_PLAY), PendingIntent.FLAG_CANCEL_CURRENT));
        }

        builder.addAction(R.drawable.ic_play_skip_next, musicService.getString(R.string.music_next), PendingIntent.getBroadcast(musicService, REQ_CODE,
                new Intent(ACTION_NEXT), PendingIntent.FLAG_CANCEL_CURRENT));

        if (musicService.getMediaSession().isActive()) {
            NotificationManagerCompat.from(musicService).notify(NOTIFICATION_ID, getNotification());
        }

    }

    static Notification getNotification() {
        boolean notify = SettingManager.getInstance().getSetting(SettingManager.SETTING_NOTIFY, true);
        if (!notify) {
            return null;
        }
        return builder.build();
    }
}
