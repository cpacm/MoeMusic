package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SongDownloadListener;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.ui.widgets.recyclerview.ItemTouchHelperAdapter;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.List;
import java.util.Map;

/**
 * @author: cpacm
 * @date: 2016/9/12
 * @desciption: 下载管理适配器
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> implements ItemTouchHelperAdapter {

    private Map<Long, BaseDownloadTask> taskMap;
    private List<Song> downloadingSongs;
    private Context context;

    public DownloadAdapter(Context context) {
        this.context = context;
        downloadingSongs = SongManager.getInstance().getDownloadingSongs();
        taskMap = SongManager.getInstance().getTaskMap();
        SongManager.getInstance().registerDownloadListener(downloadListener);
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_download_listitem, parent, false);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DownloadViewHolder holder, int position) {
        final Song song = downloadingSongs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
        if (taskMap.containsKey(song.getId())) {
            BaseDownloadTask task = taskMap.get(song.getId());
            holder.setProgress(FileUtils.getProgress(task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes()),
                    FileUtils.getProgressSize(task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes()));
            if (task.isRunning()) {
                holder.downloadBtn.setImageResource(R.drawable.ic_download_pend);
                holder.isDownloading = true;
            }
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.downloadClick(song);
            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadingSongs.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        showBasicDialog(position);
    }

    private void showBasicDialog(final int position) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.download_dialog_title)
                .content(R.string.download_dialog_description)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Song song = downloadingSongs.get(position);
                        MoeLogger.d("delete download:" + song.getTitle());
                        SongManager.getInstance().deleteSong(song);
                        downloadingSongs.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void onDestroy() {
        SongManager.getInstance().unRegisterDownloadListener(downloadListener);
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private ImageView cover, downloadBtn;
        private TextView title, progress;
        private boolean isDownloading;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.download_song_cover);
            title = (TextView) itemView.findViewById(R.id.download_song_title);
            progress = (TextView) itemView.findViewById(R.id.download_song_size);
            downloadBtn = (ImageView) itemView.findViewById(R.id.download_btn);
            progressBar = (ProgressBar) itemView.findViewById(R.id.download_pregress);
            initView();
        }

        public void initView() {
            isDownloading = false;
            progress.setText("");
            downloadBtn.setImageResource(R.drawable.ic_download_start);
            progressBar.setMax(100);
        }

        public void setProgress(int progress, String size) {
            if (progress > 0) {
                progressBar.setIndeterminate(false);
                progressBar.setProgress(progress);
                if (!TextUtils.isEmpty(size)) {
                    this.progress.setText(size);
                }
            }
        }

        public void downloadClick(Song song) {
            if (isDownloading) {
                isDownloading = false;
                progressBar.setIndeterminate(false);
                downloadBtn.setImageResource(R.drawable.ic_download_start);
                if (taskMap.containsKey(song.getId())) {
                    BaseDownloadTask task = taskMap.get(song.getId());
                    task.pause();
                }
            } else {
                isDownloading = true;
                progressBar.setIndeterminate(true);
                downloadBtn.setImageResource(R.drawable.ic_download_pend);
                int status = SongManager.getInstance().download(song);
                if (status == Song.DOWNLOAD_DISABLE) {
                    Toast.makeText(context, R.string.song_download_disable, Toast.LENGTH_SHORT).show();
                } else if (status == Song.DOWNLOAD_WITH_WIFI) {
                    Toast.makeText(context, R.string.song_download_wifi, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private SongDownloadListener downloadListener = new SongDownloadListener() {
        @Override
        public void onDownloadProgress(Song song, int soFarBytes, int totalBytes) {
            int position = downloadingSongs.indexOf(song);
            if (position >= 0 && position < getItemCount()) {
                notifyItemChanged(position);
            }
        }

        @Override
        public void onError(Song song, Throwable e) {
            int position = downloadingSongs.indexOf(song);
            if (position >= 0 && position < getItemCount()) {
                notifyItemChanged(position);
            }
        }

        @Override
        public void onCompleted(Song song) {
            int position = downloadingSongs.indexOf(song);
            if (position >= 0 && position < getItemCount()) {
                downloadingSongs.remove(position);
                notifyItemRangeRemoved(position, 1);
            }
        }

        @Override
        public void onWarn(Song song) {

        }
    };
}
