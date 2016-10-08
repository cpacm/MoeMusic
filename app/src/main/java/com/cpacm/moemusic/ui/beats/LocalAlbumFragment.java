package com.cpacm.moemusic.ui.beats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Song;
import com.cpacm.core.mvp.views.LocalIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.LocalAlbumAdapter;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/9/21.
 * @description: 本地专辑界面
 */

public class LocalAlbumFragment extends BaseFragment implements LocalIView.LocalAlbum {

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.local_album_fragment_title);

    private RecyclerView recyclerView;
    private LocalAlbumAdapter albumAdapter;
    private LocalLibraryPresenter libraryPresenter;

    public static LocalAlbumFragment newInstance() {
        LocalAlbumFragment fragment = new LocalAlbumFragment();
        return fragment;
    }

    public LocalAlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libraryPresenter = new LocalLibraryPresenter(this, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_local_music, container, false);
        initRecyclerView(parentView);
        return parentView;
    }

    private void initRecyclerView(View rootView) {
        albumAdapter = new LocalAlbumAdapter(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(albumAdapter);

        libraryPresenter.requestAlbum();

    }


    @Override
    public void getLocalAlbum(List<Album> alba) {
        albumAdapter.setData(alba);
    }
}
