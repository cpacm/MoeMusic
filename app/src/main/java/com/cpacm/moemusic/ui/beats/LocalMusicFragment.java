package com.cpacm.moemusic.ui.beats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.cpacm.core.bean.Song;
import com.cpacm.core.mvp.views.LocalIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.LocalMusicAdapter;
import com.cpacm.moemusic.ui.adapters.OnItemClickListener;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/9/21.
 * @description: 本地歌曲界面
 */

public class LocalMusicFragment extends BaseFragment implements LocalIView.LocalMusic {

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.local_music_fragment_title);

    private RecyclerView recyclerView;
    private LocalMusicAdapter localMusicAdapter;
    private LocalLibraryPresenter libraryPresenter;
    private MusicPlaylist musicPlaylist;

    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    public LocalMusicFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libraryPresenter = new LocalLibraryPresenter(this, getActivity());
        musicPlaylist = new MusicPlaylist();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_local_music, container, false);
        initRecyclerView(parentView);
        return parentView;
    }

    private void initRecyclerView(View rootView) {
        localMusicAdapter = new LocalMusicAdapter(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(localMusicAdapter);
        localMusicAdapter.setItemClickListener(new OnItemClickListener<Song>() {

            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayerManager.get().playQueue(musicPlaylist, position);
                gotoSongPlayerActivity();
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });
        libraryPresenter.requestMusic();
    }

    private void showPopupMenu(View v, final Song song, final int position) {

        final PopupMenu menu = new PopupMenu(getActivity(), v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_addto_playlist:
                        MusicPlaylist mp = MusicPlayerManager.get().getMusicPlaylist();
                        if (mp == null) {
                            mp = new MusicPlaylist();
                            MusicPlayerManager.get().setMusicPlaylist(mp);
                        }
                        mp.addSong(song);
                        break;
                    case R.id.popup_song_fav:
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_downloaded_setting);
        menu.show();
    }


    @Override
    public void getLocalMusic(List<Song> songs) {
        musicPlaylist.setQueue(songs);
        musicPlaylist.setTitle(getString(R.string.local_library));
        localMusicAdapter.setData(songs);
    }
}
