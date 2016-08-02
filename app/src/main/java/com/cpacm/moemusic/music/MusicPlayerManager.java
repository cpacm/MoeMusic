package com.cpacm.moemusic.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.cpacm.core.utils.MoeLogger;

import java.io.IOException;
import java.util.ArrayList;

import static android.media.AudioManager.OnAudioFocusChangeListener;
import static android.media.MediaPlayer.OnCompletionListener;
import static android.media.MediaPlayer.OnErrorListener;
import static android.media.MediaPlayer.OnPreparedListener;
import static android.media.MediaPlayer.OnSeekCompleteListener;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_FAST_FORWARDING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_REWINDING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_STOPPED;


/**
 * @author: cpacm
 * @date: 2016/7/19
 * @desciption: 播放管理器
 */
public class MusicPlayerManager implements OnAudioFocusChangeListener, OnPreparedListener, OnCompletionListener, OnErrorListener, OnSeekCompleteListener {

    private static final String TAG = "MusicPlayer";
    /**
     * The volume we set the media player to when GEM loses audio focus, but is allowed to reduce the volume instead of stopping playback.
     */
    public static final float VOLUME_DUCK = 0.2f;
    /**
     * The volume we set the media player when GEM has audio focus.
     */
    public static final float VOLUME_NORMAL = 1.0f;
    /**
     * This is the value you would use for tis situation:
     * The user presses "Previous". if the current position in the song is greater then this value,
     * pressing "Previous" will restart the song, if not it will play the previous song
     */
    public static final int MAX_DURATION_FOR_REPEAT = 3000;

    /**
     * GEM doesn't have audio focus, and can't duck (play at a low volume)
     */
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    /**
     * GEM doesn't have focus, but can duck (play at a low volume)
     */
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    /**
     * GEM has full audio focus
     */
    private static final int AUDIO_FOCUSED = 2;

    private final static MusicPlayerManager instance = new MusicPlayerManager();

    private Context mContext;

    private MusicService musicService;
    private MusicNotification musicNotification;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private MusicPlaylist musicPlaylist;
    private MediaControllerCompat mediaController;

    private boolean playFocusGain;
    private int audioFocus = AUDIO_NO_FOCUS_NO_DUCK;
    private long currentMediaId = -1;
    private int currentProgress;
    private int currentMaxDuration = MAX_DURATION_FOR_REPEAT;

    private ArrayList<OnChangedListener> changedListeners = new ArrayList<>();


    private MusicPlayerManager() {
    }

    public static MusicPlayerManager get() {
        return instance;
    }

    public static MusicPlayerManager from(MusicService service) {
        return MusicPlayerManager.get().setContext(service).setService(service);
    }

    public static void startServiceIfNecessary(Context c) {
        if (get().musicService == null) {
            MediaServiceHelper.get(c).initService();
            get().musicService = MediaServiceHelper.get(c).getService();
        }
    }

    public MusicPlayerManager setContext(Context c) {
        this.mContext = c;
        audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        return this;
    }

    public MusicPlayerManager setService(MusicService service) {
        this.musicService = service;
        return this;
    }


    /**
     * set a new playlist
     *
     * @param musicPlaylist
     * @param position      first play position
     */
    public void playQueue(MusicPlaylist musicPlaylist, int position) {
        this.musicPlaylist = musicPlaylist;
        musicPlaylist.setCurrentPlay(position);
        play(musicPlaylist.getCurrentPlay());
    }


    /**
     * change music in the queue
     *
     * @param position
     */
    public void playQueueItem(int position) {
        musicPlaylist.setCurrentPlay(position);
        play(musicPlaylist.getCurrentPlay());
    }

    /**
     * play music from item
     *
     * @param song
     */
    public void play(Song song) {
        playFocusGain = true;
        tryToGetAudioFocus();
        boolean mediaHasChanged = !(song.getSongId() == currentMediaId);
        if (mediaHasChanged) {
            currentProgress = 0;
            currentMediaId = song.getSongId();
        }
        if (musicService.getState() == STATE_PAUSED && !mediaHasChanged && mediaPlayer != null) {
            configMediaPlayerState();
        } else {
            musicService.setState(STATE_STOPPED);
            relaxResources(false);

            try {
                createMediaPlayerIfNeeded();
                musicService.setState(STATE_PLAYING);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(mContext, song.getUri());
                mediaPlayer.prepareAsync();


                for (OnChangedListener l : changedListeners) {
                    l.onSongChanged(song);
                }

                musicService.setAsForeground();
            } catch (Exception e) {
                Log.e(TAG, "playing song:", e);
            }
        }
    }

    public void play() {
        play(musicPlaylist.getCurrentPlay());
    }

    /**
     * next song
     */
    public void playNext() {
        play(musicPlaylist.getNextSong());
    }

    /**
     * plays the previous song in the queue
     */
    public void playPrev() {
        play(musicPlaylist.getPreSong());
    }

    /**
     * pauses the playback
     */
    public void pause() {
        if (musicService.getState() == STATE_PLAYING) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                currentProgress = mediaPlayer.getCurrentPosition();
            }
            relaxResources(false);
            giveUpAudioFocus();
            musicService.removeForeground(false);
        }
        musicService.setState(STATE_PAUSED);
    }

    /**
     * resumes the playback
     */
    public void resume() {
        if (musicService.getState() == STATE_PAUSED && mediaPlayer != null) {
            mediaPlayer.start();
            musicService.setState(STATE_PLAYING);
            tryToGetAudioFocus();
            musicService.setAsForeground();
        } else {
            Log.d(TAG, "Not paused or MediaPlayer is null. Player is null: " + (mediaPlayer == null));
        }
    }

    /**
     * this will be called to stop the playback
     */
    public void stop() {
        musicService.setState(STATE_STOPPED);
        currentProgress = getCurrentProgressInSong();
        giveUpAudioFocus();
        relaxResources(true);
        musicService.removeForeground(true);
        musicService.stopService();
    }

    public void seekTo(int progress) {
        if (mediaPlayer == null)
            currentProgress = progress;
        else {
            if (getCurrentProgressInSong() > progress) {
                musicService.setState(STATE_REWINDING);
            } else {
                musicService.setState(STATE_FAST_FORWARDING);
            }
            currentProgress = progress;
            mediaPlayer.seekTo(currentProgress);

        }
    }

    public void setVolume(int mediaVolume) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediaVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void setPlayMode(int type) {
        musicPlaylist.setPlayType(type);
        if (type == MusicPlaylist.SINGLETYPE)
            mediaPlayer.setLooping(true);
        else
            mediaPlayer.setLooping(false);
    }


    /**
     * Makes sure the media player exists and has been reset. This will create
     * the media player if needed, or reset the existing media player if one
     * already exists.
     */
    private void createMediaPlayerIfNeeded() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            // Make sure the media player will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            mediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);

            // we want the media player to notify us when it's ready preparing,
            // and when it's done playing:
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
        } else {
            mediaPlayer.reset();
        }
    }

    /**
     * Releases resources used by the service for playback. This includes the
     * "foreground service" status, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also
     *                           be released or not
     */
    private void relaxResources(boolean releaseMediaPlayer) {

        musicService.removeForeground(false);

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Try to get the system audio focus.
     */
    private void tryToGetAudioFocus() {
        Log.d(TAG, "tryToGetAudioFocus");
        if (audioFocus != AUDIO_FOCUSED) {
            int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                audioFocus = AUDIO_FOCUSED;
            }
        }
    }

    /**
     * Give up the audio focus.
     */
    private void giveUpAudioFocus() {
        Log.d(TAG, "giveUpAudioFocus");
        if (audioFocus == AUDIO_FOCUSED) {
            if (audioManager.abandonAudioFocus(this) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                audioFocus = AUDIO_NO_FOCUS_NO_DUCK;
            }
        }
    }

    /**
     * Reconfigures MediaPlayer according to audio focus settings and
     * starts/restarts it. This method starts/restarts the MediaPlayer
     * respecting the current audio focus state. So if we have focus, it will
     * play normally; if we don't have focus, it will either leave the
     * MediaPlayer paused or set it to a low volume, depending on what is
     * allowed by the current focus settings. This method assumes mPlayer !=
     * null, so if you are calling it, you have to do so from a context where
     * you are sure this is the case.
     */
    private void configMediaPlayerState() {
        Log.d(TAG, "configMediaPlayerState. mAudioFocus=" + audioFocus);
        if (audioFocus == AUDIO_NO_FOCUS_NO_DUCK) {
            // If we don't have audio focus and can't duck, we have to pause,
            if (musicService.getState() == STATE_PLAYING) {
                pause();
            }
        } else {  // we have audio focus:
            if (audioFocus == AUDIO_NO_FOCUS_CAN_DUCK) {
                mediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK); // we'll be relatively quiet
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL); // we can be loud again
                } // else do something for remote client.
            }
            // If we were playing when we lost focus, we need to resume playing.
            if (playFocusGain) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    Log.d(TAG, "configMediaPlayerState startMediaPlayer. seeking to " + currentProgress);
                    if (currentProgress == mediaPlayer.getCurrentPosition()) {
                        mediaPlayer.start();
                        musicService.setState(STATE_PLAYING);
                    } else {
                        mediaPlayer.seekTo(currentProgress);
                        mediaPlayer.start();
                        musicService.setState(STATE_PLAYING);
                    }
                }
                playFocusGain = false;
            }
        }
    }

    public int getCurrentProgressInSong() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : currentProgress;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listeners
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.d(TAG, "onAudioFocusChange. focusChange=" + focusChange);
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // We have gained focus:
            audioFocus = AUDIO_FOCUSED;

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // We have lost focus. If we can duck (low playback volume), we can keep playing.
            // Otherwise, we need to pause the playback.
            boolean canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
            audioFocus = canDuck ? AUDIO_NO_FOCUS_CAN_DUCK : AUDIO_NO_FOCUS_NO_DUCK;

            // If we are playing, we need to reset media player by calling configMediaPlayerState
            // with mAudioFocus properly set.
            if (musicService.getState() == STATE_PLAYING && !canDuck) {
                // If we don't have audio focus and can't duck, we save the information that
                // we were playing, so that we can resume playback once we get the focus back.
                //todo setting
                playFocusGain = true;
            }
        } else {
            Log.e(TAG, "onAudioFocusChange: Ignoring unsupported focusChange: " + focusChange);
        }
        configMediaPlayerState();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion from MediaPlayer");
        if (!mp.isLooping()) {
            // The media player finished playing the current song, so we go ahead and start the next.
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "Media player error: what=" + what + ", extra=" + extra);
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        currentMaxDuration = mediaPlayer.getDuration();
        configMediaPlayerState();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d(TAG, "onSeekComplete from MediaPlayer:" + mp.getCurrentPosition());
        currentProgress = mp.getCurrentPosition();
        if (musicService.getState() == STATE_REWINDING || musicService.getState() == STATE_FAST_FORWARDING) {
            mediaPlayer.start();
            musicService.setState(STATE_PLAYING);
        }
    }

    public MediaSessionCompat.Token getServiceToken() {
        return musicService.getMediaSession().getSessionToken();
    }

    public void registerListener(OnChangedListener l) {
        changedListeners.add(l);
    }

    public void unregisterListener(OnChangedListener l) {
        changedListeners.remove(l);
    }

    public ArrayList<OnChangedListener> getChangedListeners() {
        return changedListeners;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getState() {
        return musicService.getState();
    }

    public int getCurrentMaxDuration() {
        return currentMaxDuration;
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        return 0;
    }
}
