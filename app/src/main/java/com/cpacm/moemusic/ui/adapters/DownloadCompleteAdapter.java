package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SongDownloadListener;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.moemusic.R;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/9/20.
 * @description: 下载完成适配器
 */

public class DownloadCompleteAdapter extends RecyclerView.Adapter<DownloadCompleteAdapter.DownloadedViewHolder>{

    private Context context;
    private List<Song> downloadedSongs;
    private OnSongClickListener songClickListener;

    public DownloadCompleteAdapter(Context context) {
        this.context = context;
        downloadedSongs = SongManager.getInstance().getDownloadedSongs();
        SongManager.getInstance().registerDownloadListener(downloadListener);
    }

    @Override
    public DownloadedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_downloaded_listitem, parent, false);
        return new DownloadedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DownloadedViewHolder holder, final int position) {
        final Song song = downloadedSongs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        if (TextUtils.isEmpty(song.getDescription())) {
            holder.detail.setText(R.string.music_unknown);
        } else {
            holder.detail.setText(song.getDescription());
        }
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onSongSettingClick(holder.setting, song, position);
                }
            }
        });
    }

    public OnSongClickListener getSongClickListener() {
        return songClickListener;
    }

    public void setSongClickListener(OnSongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    public void deleteSong(int position){
        Song song = downloadedSongs.get(position);
        MoeLogger.d("delete download:" + song.getTitle());
        SongManager.getInstance().deleteSong(song);
        downloadedSongs.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return downloadedSongs.size();
    }

    public void onDestroy() {
        SongManager.getInstance().unRegisterDownloadListener(downloadListener);
    }

    public class DownloadedViewHolder extends RecyclerView.ViewHolder{

        public View musicLayout;
        public TextView title, detail;
        public ImageView cover;
        public AppCompatImageView setting;

        public DownloadedViewHolder(View itemView) {
            super(itemView);
            musicLayout = itemView.findViewById(R.id.download_song_item);
            title = (TextView) itemView.findViewById(R.id.download_song_title);
            detail = (TextView) itemView.findViewById(R.id.download_song_detail);
            cover = (ImageView) itemView.findViewById(R.id.download_song_cover);
            setting = (AppCompatImageView) itemView.findViewById(R.id.download_song_setting);
        }
    }


    private SongDownloadListener downloadListener = new SongDownloadListener() {
        @Override
        public void onDownloadProgress(Song song, int soFarBytes, int totalBytes) {

        }

        @Override
        public void onError(Song song, Throwable e) {

        }

        @Override
        public void onCompleted(Song song) {
            downloadedSongs = SongManager.getInstance().getDownloadedSongs();
            notifyDataSetChanged();
        }

        @Override
        public void onWarn(Song song) {

        }
    };
}
