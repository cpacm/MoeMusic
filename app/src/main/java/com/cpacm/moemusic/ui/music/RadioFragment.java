package com.cpacm.moemusic.ui.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.RadioIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.RadioAdapter;
import com.cpacm.moemusic.ui.widgets.recyclerview.RefreshRecyclerView;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/7/9.
 * @description: 电台界面
 */
public class RadioFragment extends BaseFragment implements RefreshRecyclerView.RefreshListener, RadioIView {

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.radio);

    private RefreshRecyclerView refreshView;
    private RadioAdapter radioAdapter;
    private RadioPresenter radioPresenter;

    public static RadioFragment newInstance() {
        RadioFragment fragment = new RadioFragment();
        return fragment;
    }

    public RadioFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioPresenter = new RadioPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_radio, container, false);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshView = (RefreshRecyclerView) view.findViewById(R.id.refresh_view);
        radioAdapter = new RadioAdapter(getActivity());
        refreshView.setAdapter(radioAdapter);
        refreshView.setLoadEnable(false);
        refreshView.startSwipeAfterViewCreate();
        refreshView.setRefreshListener(this);
    }

    @Override
    public void onSwipeRefresh() {
        radioPresenter.requestRadios();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void getMusics(List<WikiBean> hotRadios, List<WikiBean> newRadios) {
        if (hotRadios != null) {
            radioAdapter.setHotRadios(hotRadios);
        }
        if (newRadios != null) {
            radioAdapter.setNewRadios(newRadios);
        }
        refreshView.notifySwipeFinish();
    }

    @Override
    public void loadMusicFail(String msg) {
        refreshView.notifySwipeFinish();
    }

}
