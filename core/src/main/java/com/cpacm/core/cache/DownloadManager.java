package com.cpacm.core.cache;

import com.cpacm.core.bean.Song;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.core.utils.MoeLogger;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/9/9.
 * @description: 下载管理器
 */
public class DownloadManager {
    private static DownloadManager instance;
    private final FileDownloadQueueSet queueSet;
    private final FileDownloadListener downloadListener;
    private final List<BaseDownloadTask> tasks;

    public DownloadManager() {
        downloadListener = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                MoeLogger.d("taskId: " + task.getTag(0) + " progress:" + soFarBytes * 100 / totalBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                MoeLogger.d("path:" + task.getPath());
                FileUtils.mp3Scanner(task.getPath());
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {

            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        };
        //FileDownloadUtils.setDefaultSaveRootPath(FileUtils.getSongDir());
        queueSet = new FileDownloadQueueSet(downloadListener);
        tasks = new ArrayList<>();
        queueSet.downloadSequentially(tasks);
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    public void download(Song song) {
        if (song == null) return;
        MoeLogger.d(song.getUri().toString());
        BaseDownloadTask task = FileDownloader.getImpl().create(song.getUri().toString());
        task.setTag(0, song.getId());
        task.setAutoRetryTimes(1);
        task.setPath(FileUtils.getSongDir() + File.separator + FileUtils.filenameFilter(song.getTitle()) + ".mp3");
        task.setListener(downloadListener);
        tasks.add(task);
        task.start();
    }
}
