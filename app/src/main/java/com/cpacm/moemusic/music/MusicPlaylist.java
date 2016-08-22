package com.cpacm.moemusic.music;


import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.SongBean;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 播放列表
 */
public class MusicPlaylist {

    public final static int SINGLETYPE = 0;//单曲循环
    public final static int CYCLETYPE = 1;//列表循环
    public final static int RANDOMTYPE = 2;//随机播放


    private List<Song> queue = Collections.emptyList();
    private int currentPos = 0;
    private int playType = CYCLETYPE;
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
        switch (playType) {
            case SINGLETYPE:
            case CYCLETYPE:
                if (--currentPos < 0)
                    currentPos = 0;
                break;
            case RANDOMTYPE:
                currentPos = new Random().nextInt(queue.size());
                break;
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    public Song getNextSong() {
        switch (playType) {
            case SINGLETYPE:
            case CYCLETYPE:
                if (++currentPos >= queue.size())
                    currentPos = 0;
                break;
            case RANDOMTYPE:
                currentPos = new Random().nextInt(queue.size());
                break;
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    public Song autoNextSong() {
        switch (playType) {
            case SINGLETYPE:
                break;
            case CYCLETYPE:
                if (++currentPos >= queue.size())
                    currentPos = 0;
                break;
            case RANDOMTYPE:
                currentPos = new Random().nextInt(queue.size());
                break;
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    public void setPlayType(int type) {
        if (type < 0 || type > 2)
            throw new IllegalArgumentException("incorrect type");
        this.playType = type;
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
