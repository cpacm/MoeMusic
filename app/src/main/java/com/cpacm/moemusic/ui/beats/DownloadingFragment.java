package com.cpacm.moemusic.ui.beats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.DownloadAdapter;
import com.cpacm.moemusic.ui.widgets.recyclerview.SimpleItemTouchHelperCallback;

/**
 * @author: cpacm
 * @date: 2016/9/20
 * @desciption: 正在下载中Fragment
 */
public class DownloadingFragment extends BaseFragment {
    public static final String TITLE = MoeApplication.getInstance().getString(R.string.downloading_fragment_title);

    private RecyclerView recyclerView;
    private DownloadAdapter downloadAdapter;

    public static DownloadingFragment newInstance() {
        DownloadingFragment fragment = new DownloadingFragment();
        return fragment;
    }

    public DownloadingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_downloading, container, false);
        initRecyclerView(parentView);
        return parentView;
    }

    private void initRecyclerView(View rootView) {
        downloadAdapter = new DownloadAdapter(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(downloadAdapter);
        recyclerView.setItemAnimator(null);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(downloadAdapter);
        callback.setLongProgressDragEnabled(false);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        downloadAdapter.onDestroy();
    }

}
