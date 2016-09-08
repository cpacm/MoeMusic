package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.Song;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/9/8.
 * @description:
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListHolder> {

    private Context context;
    private List<Song> songs;
    private OnSongClickListener songClickListener;

    public PlayListAdapter(Context context) {
        this.context = context;
        songs = new ArrayList<>();
    }

    public void setData(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @Override
    public PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_playlist_item, parent, false);
        return new PlayListHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayListHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        if (TextUtils.isEmpty(song.getDescription())) {
            holder.detail.setVisibility(View.GONE);
        } else {
            holder.detail.setVisibility(View.VISIBLE);
            holder.detail.setText(song.getDescription());
        }
        if (MusicPlayerManager.get().getPlayingSong() != null && song.getId() == MusicPlayerManager.get().getPlayingSong().getId()) {
            holder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.title.setTextColor(context.getResources().getColor(R.color.black_normal));
        }
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
        holder.musicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onSongClick(song, position);
                }
            }
        });
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onSongSettingClick(holder.setting, song, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public OnSongClickListener getSongClickListener() {
        return songClickListener;
    }

    public void setSongClickListener(OnSongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    public class PlayListHolder extends RecyclerView.ViewHolder {
        public View musicLayout;
        public TextView title, detail;
        public ImageView cover;
        public AppCompatImageView setting;

        public PlayListHolder(View itemView) {
            super(itemView);
            musicLayout = itemView.findViewById(R.id.playlist_song_item);
            title = (TextView) itemView.findViewById(R.id.playlist_song_title);
            detail = (TextView) itemView.findViewById(R.id.playlist_song_detail);
            cover = (ImageView) itemView.findViewById(R.id.playlist_song_cover);
            setting = (AppCompatImageView) itemView.findViewById(R.id.playlist_song_setting);
        }
    }
}
