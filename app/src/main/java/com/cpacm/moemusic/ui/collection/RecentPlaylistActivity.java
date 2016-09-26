package com.cpacm.moemusic.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SongManager;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicRecentPlaylist;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.OnSongClickListener;
import com.cpacm.moemusic.ui.adapters.RecentPlayAdapter;

/**
 * @author: cpacm
 * @date: 2016/9/26
 * @desciption: 最近播放界面
 */

public class RecentPlaylistActivity extends AbstractAppActivity implements OnSongChangedListener {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecentPlaylistActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecentPlayAdapter recentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recently_playlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicPlayerManager.get().registerListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        recentAdapter = new RecentPlayAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recentAdapter);
        recentAdapter.setData(MusicRecentPlaylist.getInstance().getQueue());
        recentAdapter.setSongClickListener(new OnSongClickListener() {
            @Override
            public void onSongClick(Song song, int position) {
                MusicPlayerManager.get().playQueueItem(position);
            }

            @Override
            public void onSongSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });
    }

    private void showPopupMenu(View v, final Song song, final int position) {

        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        MusicPlayerManager.get().playQueueItem(position);
                        break;
                    case R.id.popup_song_fav:
                        break;
                    case R.id.popup_song_goto_album:
                        break;
                    case R.id.popup_song_download:
                        showSnackBar(getString(R.string.song_add_download));
                        SongManager.getInstance().download(song);
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_playlist_setting);
        menu.show();
    }

    @Override
    public void onSongChanged(Song song) {
        recentAdapter.setData(MusicRecentPlaylist.getInstance().getQueue());
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
    }
}
