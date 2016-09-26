package com.cpacm.moemusic.music;

import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.ACache;
import com.cpacm.moemusic.MoeApplication;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/9/26
 * @desciption: 最近播放列表，最多100首记录
 */

public class MusicRecentPlaylist {

    private static final String PLAY_QUEUE = "song_queue";
    private static final int RECENT_COUNT = 10;
    private static MusicRecentPlaylist instance;

    private ArrayList<Song> queue;


    public static MusicRecentPlaylist getInstance() {
        if (instance == null) {
            instance = new MusicRecentPlaylist();
        }
        return instance;
    }

    private MusicRecentPlaylist() {
        queue = readQueueFromFileCache();
    }

    public void addPlaySong(final Song song) {
        queue.add(0, song);
        for (int i = queue.size() - 1; i > 0; i--) {
            if (i >= RECENT_COUNT) {
                queue.remove(i);
                continue;
            }
            if (song.getId() == queue.get(i).getId()) {
                queue.remove(i);
                break;
            }
        }
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                addQueueToFileCache();
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }

    public ArrayList<Song> getQueue() {
        return queue;
    }

    /**
     * 将播放列表缓存到文件中，以便下次使用时直接读取
     */
    private void addQueueToFileCache() {
        ACache.get(MoeApplication.getInstance(), PLAY_QUEUE).put(PLAY_QUEUE, queue);
    }

    /**
     * 从文件缓存中读取上一次的播放列表
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Song> readQueueFromFileCache() {
        Object serializable = ACache.get(MoeApplication.getInstance(), PLAY_QUEUE).getAsObject(PLAY_QUEUE);
        if (serializable != null && serializable instanceof ArrayList) {
            return (ArrayList<Song>) serializable;
        }
        return new ArrayList<>();
    }
}
