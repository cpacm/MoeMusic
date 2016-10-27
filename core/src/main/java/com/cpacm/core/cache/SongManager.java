package com.cpacm.core.cache;

import android.text.TextUtils;

import com.cpacm.core.CoreApplication;
import com.cpacm.core.bean.Song;
import com.cpacm.core.db.dao.SongDao;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.core.utils.SystemParamsUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/9/11
 * @desciption: 歌曲管理器，单例
 */
public class SongManager {
    private static SongManager instance;
    private Map<Long, Song> songLibrary;
    private SongDao songDao;
    private List<SongDownloadListener> listeners;
    private Map<Long, BaseDownloadTask> taskMap;

    public static SongManager getInstance() {
        if (instance == null) {
            instance = new SongManager();
        }
        return instance;
    }

    public SongManager() {
        songDao = new SongDao();
        songLibrary = new LinkedHashMap<>();
        updateSongLibrary();
        listeners = new ArrayList<>();
        taskMap = new HashMap<>();
    }

    /**
     * 异步更新歌曲信息
     */
    private void updateSongLibrary() {
        Observable.create(
                new Observable.OnSubscribe<List<Song>>() {
                    @Override
                    public void call(Subscriber<? super List<Song>> subscriber) {
                        subscriber.onNext(songDao.queryAll());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Func1<List<Song>, Observable<Song>>() {
                    @Override
                    public Observable<Song> call(List<Song> songs) {
                        return Observable.from(songs);
                    }
                })
                .map(new Func1<Song, Song>() {
                    @Override
                    public Song call(Song song) {
                        if (song.getDownload() == Song.DOWNLOAD_COMPLETE && !TextUtils.isEmpty(song.getPath())) {
                            if (!FileUtils.existFile(song.getPath())) {
                                song.setDownload(Song.DOWNLOAD_NONE);
                                insertOrUpdateSong(song);
                            }
                        }
                        return song;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Song>() {
                    @Override
                    public void call(Song song) {
                        songLibrary.put(song.getId(), song);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MoeLogger.e(throwable.toString());
                    }
                });
    }

    public Song querySong(long sid) {
        if (songLibrary.containsKey(sid)) {
            return songLibrary.get(sid);
        }
        return null;
    }

    public void updateSongFromLibrary(Song song) {
        if (songLibrary.containsKey(song.getId())) {
            Song cacheSong = songLibrary.get(song.getId());
            song.setDownload(cacheSong.getDownload());
            song.setPath(cacheSong.getPath());
        }
    }

    public void insertOrUpdateSong(final Song song) {
        Observable.create(
                new Observable.OnSubscribe<Song>() {
                    @Override
                    public void call(Subscriber<? super Song> subscriber) {
                        updateSongFromLibrary(song);
                        songDao.insertOrUpdateSong(song);
                        subscriber.onNext(song);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Song>() {
                    @Override
                    public void call(Song song) {
                        songLibrary.put(song.getId(), song);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MoeLogger.e(throwable.toString());
                    }
                });
    }

    /**
     * 删除数据库的歌曲信息，包括下载中的temp缓存和已经下载完的歌曲
     *
     * @param song 歌曲信息
     */
    public void deleteSong(final Song song) {
        Observable.create(
                new Observable.OnSubscribe<Song>() {
                    @Override
                    public void call(Subscriber<? super Song> subscriber) {
                        if (taskMap.containsKey(song.getId())) {
                            BaseDownloadTask task = taskMap.get(song.getId());
                            FileDownloader.getImpl().clear(task.getId(), task.getPath());
                        } else {
                            FileUtils.deleteFile(song.getPath());
                        }
                        if (songLibrary.containsKey(song.getId())) {
                            songDao.deleteSong(song);
                        }
                        subscriber.onNext(song);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Song>() {
                    @Override
                    public void call(Song song) {
                        taskMap.remove(song.getId());
                        songLibrary.remove(song.getId());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MoeLogger.e(throwable.toString());
                    }
                });
    }


    /*############################# 下载管理 #############################*/

    /**
     * 获取下载中歌曲
     *
     * @return
     */
    public List<Song> getDownloadingSongs() {
        List<Song> downLoadingSongs = new ArrayList<>();
        Set<Long> keys = songLibrary.keySet();
        for (Long key : keys) {
            Song song = songLibrary.get(key);
            if (song.getDownload() == Song.DOWNLOAD_ING) {
                downLoadingSongs.add(song);
            }
        }
        return downLoadingSongs;
    }

    /**
     * 获取下载完成歌曲
     *
     * @return
     */
    public List<Song> getDownloadedSongs() {
        List<Song> downLoadingSongs = new ArrayList<>();
        Set<Long> keys = songLibrary.keySet();
        for (Long key : keys) {
            Song song = songLibrary.get(key);
            if (song.getDownload() == Song.DOWNLOAD_COMPLETE) {
                downLoadingSongs.add(song);
            }
        }
        return downLoadingSongs;
    }

    public Map<Long, BaseDownloadTask> getTaskMap() {
        return taskMap;
    }

    /**
     * 歌曲下载
     *
     * @param song
     */
    public int download(Song song) {
        if (song == null || song.getUri() == null) {
            return Song.DOWNLOAD_NONE;
        }

        boolean wifiEnable = SettingManager.getInstance().getSetting(SettingManager.SETTING_WIFI, false);
        if (!SystemParamsUtils.isNetworkConnected(CoreApplication.getInstance())) {
            return Song.DOWNLOAD_DISABLE;
        }
        if (wifiEnable && !SystemParamsUtils.isWIFIConnected(CoreApplication.getInstance())) {
            return Song.DOWNLOAD_WITH_WIFI;
        }
        MoeLogger.d(song.getUri().toString());
        songLibrary.put(song.getId(), song);
        String path = FileUtils.getSongDir() + File.separator + FileUtils.filenameFilter(song.getTitle()) + ".mp3";
        song.setPath(path);
        if (FileUtils.existFile(path)) {
            song.setDownload(Song.DOWNLOAD_COMPLETE);
            insertOrUpdateSong(song);
            return Song.DOWNLOAD_COMPLETE;
        }
        if (taskMap.containsKey(song.getId())) {
            BaseDownloadTask task = taskMap.get(song.getId());
            task.pause();
            taskMap.remove(song.getId());
        }
        BaseDownloadTask task = FileDownloader.getImpl().create(song.getUri().toString());
        task.setTag(0, song);
        task.setAutoRetryTimes(1);
        task.setPath(path);
        task.setListener(downloadListener);
        task.start();
        taskMap.put(song.getId(), task);
        song.setDownload(Song.DOWNLOAD_ING);
        insertOrUpdateSong(song);
        return Song.DOWNLOAD_ING;
    }

    private final FileDownloadListener downloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            MoeLogger.d("pending:" + task.getPath());
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            MoeLogger.d("download:" + soFarBytes * 100 / totalBytes);
            for (SongDownloadListener listener : listeners) {
                listener.onDownloadProgress((Song) task.getTag(0), soFarBytes, totalBytes);
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            MoeLogger.d("path:" + task.getPath());
            FileUtils.mp3Scanner(task.getPath());
            Song song = (Song) task.getTag(0);
            song.setDownload(Song.DOWNLOAD_COMPLETE);
            songLibrary.put(song.getId(), song);
            taskMap.remove(song.getId());
            insertOrUpdateSong(song);
            for (SongDownloadListener listener : listeners) {
                listener.onCompleted(song);
            }
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            MoeLogger.d("paused:" + task.getPath());
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            MoeLogger.d("error:" + task.getPath());
            taskMap.remove(((Song) task.getTag(0)).getId());
            for (SongDownloadListener listener : listeners) {
                listener.onError((Song) task.getTag(0), e);
            }
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            MoeLogger.d("warn:" + task.getPath());
            //sdcard中已经存在该歌曲，更新数据库
            if (FileUtils.existFile(task.getPath())) {
                Song song = (Song) task.getTag(0);
                song.setDownload(Song.DOWNLOAD_COMPLETE);
                songLibrary.put(song.getId(), song);
                taskMap.remove(song.getId());
                insertOrUpdateSong(song);
            }
            for (SongDownloadListener listener : listeners) {
                listener.onWarn((Song) task.getTag(0));
            }
        }
    };

    public void registerDownloadListener(SongDownloadListener listener) {
        if (listener == null) return;
        listeners.add(listener);
    }

    public void unRegisterDownloadListener(SongDownloadListener listener) {
        if (listener == null) return;
        listeners.remove(listener);
    }

}
