package com.cpacm.core.cache;

import com.cpacm.core.bean.Song;

/**
 * @author: cpacm
 * @date: 2016/9/19
 * @desciption: 歌曲下载监听器
 */
public interface SongDownloadListener {
    void onDownloadProgress(Song song, int soFarBytes, int totalBytes);

    void onError(Song song, Throwable e);

    void onCompleted(Song song);

    void onWarn(Song song);
}
