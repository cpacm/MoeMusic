package com.cpacm.moemusic.ui.adapters;

import android.view.View;

import com.cpacm.core.bean.Song;

/**
 * @author: cpacm
 * @date: 2016/9/8
 * @desciption: 歌曲点击接口
 */
public interface OnSongClickListener {

    void onSongClick(Song song, int position);

    void onSongSettingClick(View v, Song song, int position);
}
