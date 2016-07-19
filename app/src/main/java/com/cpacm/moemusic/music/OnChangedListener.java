package com.cpacm.moemusic.music;

import android.support.v4.media.session.PlaybackStateCompat;

/**
 * @author: cpacm
 * @date: 2016/7/19
 * @desciption: 歌曲变化监听器
 */
public interface OnChangedListener {
    void onSongChanged(Song song);

    void onPlayBackStateChanged(PlaybackStateCompat state);
}
