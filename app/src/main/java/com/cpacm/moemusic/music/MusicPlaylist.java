package com.cpacm.moemusic.music;


import android.database.DataSetObserver;

import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.ACache;
import com.cpacm.moemusic.MoeApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 播放列表
 */
public class MusicPlaylist {

    private List<Song> queue;
    private Song curSong;
    private long albumId;
    private String title;

    public MusicPlaylist(List<Song> queue) {
        setQueue(queue);
        albumId = -1;
    }

    public MusicPlaylist() {
        queue = new ArrayList<>();
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

    /**
     * 获取队列的上一首歌
     * @return
     */
    public Song getPreSong() {
        int currentPos = queue.indexOf(curSong);
        int mode = MusicPlayerManager.get().getPlayMode();
        if(mode == MusicPlayerManager.SINGLETYPE || mode == MusicPlayerManager.CYCLETYPE){
            if (--currentPos < 0)
                currentPos = 0;
        }else{
            currentPos = new Random().nextInt(queue.size());
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    /**
     * 获取队列的下一首歌
     * @return
     */
    public Song getNextSong() {
        int currentPos = queue.indexOf(curSong);
        int mode = MusicPlayerManager.get().getPlayMode();
        if(mode == MusicPlayerManager.SINGLETYPE || mode == MusicPlayerManager.CYCLETYPE){
            if (++currentPos >= queue.size())
                currentPos = 0;
        }else{
            currentPos = new Random().nextInt(queue.size());
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
