package com.cpacm.moemusic.ui.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.AlbumAdapter;
import com.cpacm.moemusic.ui.widgets.RefreshRecyclerView;

import net.cpacm.library.SimpleSliderLayout;
import net.cpacm.library.indicator.ViewpagerIndicator.CirclePageIndicator;
import net.cpacm.library.slider.ImageSliderView;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 音乐界面
 */
public class AlbumFragment extends BaseFragment implements RefreshRecyclerView.RefreshListener {
    public static final String TITLE = MoeApplication.getInstance().getString(R.string.album);

    private RefreshRecyclerView refreshView;
    private View headerView;
    private AlbumAdapter adapter;
    private SimpleSliderLayout sliderLayout;
    private CirclePageIndicator circlePageIndicator;


    private String[] strs = {"夜空", "车站", "夕阳", "世界", "神社", "碑"};
    private String[] urls = {
            "http://7xi4up.com1.z0.glb.clouddn.com/%E5%A3%81%E7%BA%B81.jpg",
            "http://7xi4up.com1.z0.glb.clouddn.com/%E5%A3%81%E7%BA%B82.jpg",
            "http://7xi4up.com1.z0.glb.clouddn.com/%E5%A3%81%E7%BA%B83.jpg",
            "http://7xi4up.com1.z0.glb.clouddn.com/%E5%A3%81%E7%BA%B84.jpg",
            "http://7xi4up.com1.z0.glb.clouddn.com/%E5%A3%81%E7%BA%B85.jpg",
            "http://7xi4up.com1.z0.glb.clouddn.com/%E5%A3%81%E7%BA%B86.jpg"
    };

    public static AlbumFragment newInstance() {
        AlbumFragment fragment = new AlbumFragment();
        return fragment;
    }

    public AlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_album, container, false);

        refreshView = (RefreshRecyclerView) parentView.findViewById(R.id.refresh_view);
        headerView = inflater.inflate(R.layout.fragment_album_header, container, false);
        adapter = new AlbumAdapter();
        refreshView.setRefreshListener(this);
        refreshView.setLoadEnable(false);
        refreshView.setHeaderView(headerView);
        refreshView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSlider();
        return parentView;
    }

    private void initSlider() {
        sliderLayout = (SimpleSliderLayout) headerView.findViewById(R.id.simple_slider);
        circlePageIndicator = (CirclePageIndicator) headerView.findViewById(R.id.circle_indicator);
        for (int i = 0; i < urls.length; i++) {
            ImageSliderView sliderView = new ImageSliderView(getActivity());
            sliderView.empty(R.drawable.image_empty);
            Glide.with(this).load(urls[i]).crossFade().into(sliderView.getImageView());
            sliderView.setPageTitle(strs[i]);
            sliderLayout.addSlider(sliderView);
        }
        sliderLayout.setViewPagerIndicator(circlePageIndicator);//为viewpager设置指示器
    }

    @Override
    public void onSwipeRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
