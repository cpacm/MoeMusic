package com.cpacm.moemusic.music;


import com.cpacm.core.bean.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 播放列表
 */
public class MusicPlaylist {

    private List<Song> queue = new ArrayList<>();
    private Song curSong;
    private long albumId;
    private String title;

    public MusicPlaylist(List<Song> queue) {
        this.queue = queue;
        albumId = -1;
    }

    public MusicPlaylist() {
        albumId = -1;
    }

    public Song getCurrentPlay() {
        if (curSong == null) {
            setCurrentPlay(0);
        }
        return curSong;
    }

    public long setCurrentPlay(int position) {
        if (queue.size() > position && position >= 0) {
            curSong = queue.get(position);
            return curSong.getId();
        }
        return -1;
    }

    public Song getPreSong() {
        int currentPos = queue.indexOf(curSong);
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
        int currentPos = queue.indexOf(curSong);
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

    public void addSong(Song song, int position) {
        queue.add(position, song);
    }

    public void addSong(Song song) {
        queue.add(song);
    }

    public void setQueue(List<Song> queue) {
        this.queue = queue;
        setCurrentPlay(0);
    }

    public void clear() {
        queue.clear();
        curSong = null;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
