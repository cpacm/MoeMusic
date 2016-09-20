package com.cpacm.moemusic.ui.beats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;

/**
 * @author: cpacm
 * @date: 2016/9/20
 * @desciption: 下载完成fragment界面
 */

public class DownloadedFragment extends BaseFragment {
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
        final View parentView = inflater.inflate(R.layout.fragment_downloading, container, false);
        //initRecyclerView(parentView);

        return parentView;
    }
}
