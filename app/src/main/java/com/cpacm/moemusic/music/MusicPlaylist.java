package com.cpacm.moemusic.music;


import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.SongBean;
import com.cpacm.core.bean.WikiBean;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 播放列表
 */
public class MusicPlaylist {

    private List<Song> queue = Collections.emptyList();
    private int currentPos = 0;
    private Song curSong;

    public MusicPlaylist(List<Song> queue) {
        this.queue = queue;
    }

    public MusicPlaylist() {
    }

    public Song getCurrentPlay() {
        return curSong;
    }

    public long setCurrentPlay(int position) {
        currentPos = position;
        if (queue.size() > position && position >= 0)
            curSong = queue.get(position);
        return curSong.getId();
    }

    public Song getPreSong() {
        switch (MusicPlayerManager.get().getPlayMode()) {
            case MusicPlayerManager.SINGLETYPE:
            case MusicPlayerManager.CYCLETYPE:
                if (--currentPos < 0)
                    currentPos = 0;
                break;
            case MusicPlayerManager.RANDOMTYPE:
                currentPos = new Random().nextInt(queue.size());
                break;
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    public Song getNextSong() {
        switch (MusicPlayerManager.get().getPlayMode()) {
            case MusicPlayerManager.SINGLETYPE:
            case MusicPlayerManager.CYCLETYPE:
                if (++currentPos >= queue.size())
                    currentPos = 0;
                break;
            case MusicPlayerManager.RANDOMTYPE:
                currentPos = new Random().nextInt(queue.size());
                break;
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    public List<Song> getQueue() {
        return queue;
    }

    public void addQueue(List<Song> songs, boolean head) {
        if (!head) queue.addAll(songs);
        else queue.addAll(0, songs);
    }

    public void setQueue(List<Song> queue) {
        this.queue = queue;
    }

}
