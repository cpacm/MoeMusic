package com.cpacm.moemusic.ui.beats;

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
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.OnSongClickListener;
import com.cpacm.moemusic.ui.adapters.PlayListAdapter;
import com.cpacm.moemusic.ui.music.SongPlayerActivity;

/**
 * @author: cpacm
 * @date: 2016/8/31
 * @desciption: 播放列表
 */
public class PlayListActivity extends AbstractAppActivity implements OnSongChangedListener{

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PlayListActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private PlayListAdapter playListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicPlayerManager.get().registerListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        playListAdapter = new PlayListAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playListAdapter);
        if (MusicPlayerManager.get().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayerManager.get().getMusicPlaylist().getQueue());
        }
        playListAdapter.setSongClickListener(new OnSongClickListener() {
            @Override
            public void onSongClick(Song song, int position) {
                MusicPlayerManager.get().playQueueItem(position);
                gotoSongPlayerActivity();
            }

            @Override
            public void onSongSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });
    }

    public boolean gotoSongPlayerActivity() {
        if (MusicPlayerManager.get().getPlayingSong() == null) {
            showToast(R.string.music_playing_none);
            return false;
        }
        SongPlayerActivity.open(this);
        return true;
    }

    private void showPopupMenu(View v, final Song song, final int position) {

        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        MusicPlayerManager.get().playQueueItem(position);
                        gotoSongPlayerActivity();
                        break;
                    case R.id.popup_song_fav:
                        break;
                    case R.id.popup_song_goto_album:
                        break;
                    case R.id.popup_song_download:
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
        if (MusicPlayerManager.get().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayerManager.get().getMusicPlaylist().getQueue());
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
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
}
