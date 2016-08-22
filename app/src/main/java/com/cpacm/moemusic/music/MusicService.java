package com.cpacm.moemusic.music;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.cpacm.core.bean.Song;
import com.cpacm.core.utils.MoeLogger;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP;
import static android.support.v4.media.session.PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_BUFFERING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_SKIPPING_TO_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_STOPPED;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 音乐播放服务
 */
public class MusicService extends Service implements OnChangedListener {

    private final IBinder mBinder = new MusicBinder();

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat mState;
    private MusicPlayerManager playerManager;
    private AudioManager audioManager;


    ///////////////////////////////////////////////////////////////////////////
    // Binding
    ///////////////////////////////////////////////////////////////////////////
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * KEYCODE_MEDIA_PLAY_PAUSE——Play/Pause media key
     * KEYCODE_MEDIA_STOP——Stop media key
     * KEYCODE_MEDIA_NEXT——Play Next media key
     * KEYCODE_MEDIA_PREVIOUS——Play Previous media key
     * KEYCODE_MEDIA_REWIND——Rewind media key
     * KEYCODE_MEDIA_FAST_FORWARD——KEYCODE_MEDIA_FAST_FORWARD
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSongChanged(Song song) {
        //mediaSession.setMetadata(song.data);
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void setUp() {
        playerManager = MusicPlayerManager.from(this);
        playerManager.registerListener(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setUpMediaSession();
    }

    /**
     * 线控
     * 使用 {@link MediaButtonReceiver} 来兼容 api21 之前的版本
     * 使用{@link MediaSessionCompat#setCallback}控制 api21 之后的版本
     */
    private void setUpMediaSession() {
        ComponentName mbr = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
        mediaSession = new MediaSessionCompat(this, "fd", mbr, null);
        /* set flags to handle media buttons */
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        /* this is need after Lolipop */
        mediaSession.setCallback(new MediaSessionCallback());
        setState(STATE_NONE);
    }

    public void setState(int state) {
        mState = new PlaybackStateCompat.Builder()
                .setActions(
                        ACTION_PLAY |
                                ACTION_PAUSE |
                                ACTION_PLAY_PAUSE |
                                ACTION_SKIP_TO_NEXT |
                                ACTION_SKIP_TO_PREVIOUS |
                                ACTION_STOP |
                                ACTION_PLAY_FROM_MEDIA_ID |
                                ACTION_PLAY_FROM_SEARCH |
                                ACTION_SKIP_TO_QUEUE_ITEM |
                                ACTION_SEEK_TO)
                .setState(state, PLAYBACK_POSITION_UNKNOWN, 1.0f, SystemClock.elapsedRealtime())
                .build();
        mediaSession.setPlaybackState(mState);
        mediaSession.setActive(state != PlaybackStateCompat.STATE_NONE && state != PlaybackStateCompat.STATE_STOPPED);
        for (OnChangedListener l : playerManager.getChangedListeners()) {
            l.onPlayBackStateChanged(mState);
        }
    }

    public int getState() {
        return mState.getState();
    }

    public void stopService() {
        stopSelf();
    }

    public void setAsForeground() {
        //startForeground(MediaNotification.NOTIFICATION_ID, mNotification.getNotification());
    }

    public void removeForeground(boolean removeNotification) {
        stopForeground(removeNotification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
    }

    public class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            MoeLogger.d("Play");
            playerManager.resume();
            setState(STATE_PLAYING);
        }

        @Override
        public void onPause() {
            MoeLogger.d("Pause");
            playerManager.pause();
            setState(STATE_PAUSED);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            playerManager.playNext();
            setState(STATE_SKIPPING_TO_NEXT);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            playerManager.playPrev();
            setState(STATE_SKIPPING_TO_PREVIOUS);
        }

        @Override
        public void onSeekTo(long pos) {
            playerManager.seekTo((int) pos);
            setState(STATE_BUFFERING);
        }

        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
            // playerManager.play(Library.findSongById(id));
            // setState(STATE_SKIPPING_TO_QUEUE_ITEM);
        }

        public void onTogglePlay() {
            if (mState.getState() == STATE_PLAYING) {
                onPause();
            } else {
                onPlay();
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            playerManager.stop();
            setState(STATE_STOPPED);
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            boolean handled = false;
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(mediaButtonEvent.getAction())) {
                if (mState.getState() == STATE_PLAYING) {
                    onPause();
                    handled = true;
                }
            }
            return handled;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // getter
    ///////////////////////////////////////////////////////////////////////////

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

}
