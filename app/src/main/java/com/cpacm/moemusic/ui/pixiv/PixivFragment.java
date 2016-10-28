package com.cpacm.moemusic.ui.pixiv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.core.bean.PixivBean;
import com.cpacm.core.mvp.views.PixivIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.PixivAdapter;
import com.cpacm.moemusic.ui.widgets.recyclerview.RefreshRecyclerView;

import java.util.List;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 分区界面
 */
public class PixivFragment extends BaseFragment implements PixivIView, RefreshRecyclerView.RefreshListener {

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.pixiv);
    private RefreshRecyclerView refreshView;

    private PixivPresenter pixivPresenter;
    private PixivAdapter pixivAdapter;

    public static PixivFragment newInstance() {
        PixivFragment fragment = new PixivFragment();
        return fragment;
    }


    public PixivFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pixivPresenter = new PixivPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_area, container, false);
        refreshView = (RefreshRecyclerView) parentView.findViewById(R.id.refresh_view);

        pixivAdapter = new PixivAdapter(getActivity());
        refreshView.setRefreshListener(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        refreshView.setLayoutManager(staggeredGridLayoutManager);
        refreshView.setAdapter(pixivAdapter);
        refreshView.setLoadEnable(false);
        refreshView.startSwipeAfterViewCreate();
        return parentView;
    }


    @Override
    public void getPixivPicture(List<PixivBean> pixivBeen) {
        pixivAdapter.setData(pixivBeen);
        refreshView.notifySwipeFinish();
    }

    @Override
    public void fail(String msg) {
        refreshView.notifySwipeFinish();
    }

    @Override
    public void onSwipeRefresh() {
        pixivPresenter.requestPixiv();
    }

    @Override
    public void onLoadMore() {

    }
}
