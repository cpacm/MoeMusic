package com.cpacm.moemusic.music;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 音乐播放服务
 */
public class MusicService extends Service {

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat state;

    ///////////////////////////////////////////////////////////////////////////
    // Binding
    ///////////////////////////////////////////////////////////////////////////
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent intent) {
                if (!Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                    return super.onMediaButtonEvent(intent);
                }

                KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (event == null || event.getAction() != KeyEvent.ACTION_UP) {
                    return super.onMediaButtonEvent(intent);
                }

                // do something

                return true;
            }




        });
        /* to make sure the media session is active */
        if (!mediaSession.isActive()) {
            mediaSession.setActive(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
    }

}
