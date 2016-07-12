package com.cpacm.moemusic.ui.music;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.RecyclerViewListAdapter;
import com.cpacm.moemusic.ui.widgets.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 音乐界面
 */
public class MusicFragment extends BaseFragment implements RefreshRecyclerView.RefreshListener {
    public static final String TITLE = MoeApplication.getInstance().getString(R.string.music);

    private RefreshRecyclerView refreshView;
    private static final String itemData = "This is some dummy text for shown in list view, every single word will be treated as an item";
    private RecyclerViewListAdapter adapter;

    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
        return fragment;
    }

    public MusicFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_music, container, false);
        refreshView = (RefreshRecyclerView) parentView.findViewById(R.id.refresh_view);
        String[] listItems = itemData.split(" ");

        List<String> list = new ArrayList<>();
        Collections.addAll(list, listItems);
        adapter = new RecyclerViewListAdapter(list);
        adapter.setTestListener(new RecyclerViewListAdapter.TestListener() {
            @Override
            public void onClick(String s) {
                Snackbar.make(parentView, s, Snackbar.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager g = new LinearLayoutManager(getActivity());
        refreshView.setAdapter(adapter);
        refreshView.setHeaderView(R.layout.fragment_music_header);
        refreshView.setLayoutManager(g);
        refreshView.setRefreshListener(this);

        return parentView;
    }

    @Override
    public void onSwipeRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] listItems = itemData.split(" ");
                List<String> list = new ArrayList<>();
                Collections.addAll(list, listItems);
                adapter.addData(list);
                refreshView.notifySwipeFinish();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] listItems = itemData.split(" ");
                List<String> list = new ArrayList<>();
                Collections.addAll(list, listItems);
                adapter.addData(list);
                refreshView.notifyLoadMoreFinish(false);
            }
        }, 2000);
    }
}
