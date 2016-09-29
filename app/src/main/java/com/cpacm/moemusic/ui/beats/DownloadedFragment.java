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
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.DownloadCompleteAdapter;
import com.cpacm.moemusic.ui.adapters.OnItemClickListener;

/**
 * @author: cpacm
 * @date: 2016/9/20
 * @desciption: 下载完成fragment界面
 */

public class DownloadedFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private DownloadCompleteAdapter downloadAdapter;

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.downloaded_fragment_title);

    public static DownloadedFragment newInstance() {
        DownloadedFragment fragment = new DownloadedFragment();
        return fragment;
    }

    public DownloadedFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_download, container, false);
        initRecyclerView(parentView);
        return parentView;
    }

    private void initRecyclerView(View rootView) {
        downloadAdapter = new DownloadCompleteAdapter(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(downloadAdapter);
        recyclerView.setItemAnimator(null);

        downloadAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayerManager.get().playQueueItem(position);
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });
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
                    case R.id.popup_song_delete:
                        downloadAdapter.deleteSong(position);
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_downloaded_setting);
        menu.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        downloadAdapter.onDestroy();
    }
}
