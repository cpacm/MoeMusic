package com.cpacm.moemusic.ui.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.BannerBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.http.RxBus;
import com.cpacm.core.mvp.views.AlbumIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.core.bean.event.FavEvent;
import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.adapters.AlbumAdapter;
import com.cpacm.moemusic.ui.beats.SearchResultActivity;
import com.cpacm.moemusic.ui.widgets.recyclerview.RefreshRecyclerView;

import net.cpacm.library.SimpleSliderLayout;
import net.cpacm.library.indicator.ViewpagerIndicator.CirclePageIndicator;
import net.cpacm.library.slider.BaseSliderView;
import net.cpacm.library.slider.ImageSliderView;
import net.cpacm.library.slider.OnSliderClickListener;

import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 音乐界面
 */
public class AlbumFragment extends BaseFragment implements RefreshRecyclerView.RefreshListener, AlbumIView {
    public static final String TITLE = MoeApplication.getInstance().getString(R.string.album);

    private RefreshRecyclerView refreshView;
    private View headerView;
    private AlbumAdapter albumAdapter;
    private SimpleSliderLayout sliderLayout;
    private CirclePageIndicator circlePageIndicator;
    private GridLayoutManager gridLayoutManager;

    private CompositeSubscription allSubscription = new CompositeSubscription();
    private AlbumPresenter albumPresenter;

    public static AlbumFragment newInstance() {
        AlbumFragment fragment = new AlbumFragment();
        return fragment;
    }

    public AlbumFragment() {
        albumPresenter = new AlbumPresenter(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSubscription.add(RxBus.getDefault()
                .toObservable(FavEvent.class).subscribe(new Action1<FavEvent>() {
                    @Override
                    public void call(FavEvent favEvent) {
                        onEvent(favEvent);
                    }
                }));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_album, container, false);

        refreshView = (RefreshRecyclerView) parentView.findViewById(R.id.refresh_view);
        headerView = inflater.inflate(R.layout.recycler_album_header, container, false);

        initRefreshView();
        sliderLayout = (SimpleSliderLayout) headerView.findViewById(R.id.simple_slider);
        circlePageIndicator = (CirclePageIndicator) headerView.findViewById(R.id.circle_indicator);
        return parentView;
    }

    private void initRefreshView() {
        albumAdapter = new AlbumAdapter(getActivity());
        refreshView.setAdapter(albumAdapter);
        refreshView.setHeaderView(headerView);
        refreshView.setRefreshListener(this);
        refreshView.setLoadEnable(false);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        refreshView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (refreshView.getRefreshRecycleAdapter().getItemViewType(position)) {
                    case RefreshRecyclerView.RefreshRecycleAdapter.HEADER:
                    case RefreshRecyclerView.RefreshRecycleAdapter.LOADMORE:
                    case AlbumAdapter.ALBUM_TYPE_NEW:
                    case AlbumAdapter.ALBUM_TYPE_HOT:
                        return gridLayoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });
        refreshView.startSwipeAfterViewCreate();

    }

    private void initSlider(List<BannerBean> been) {
        sliderLayout.removeAllSlider();
        for (int i = 0; i < been.size(); i++) {
            final BannerBean bean = been.get(i);
            ImageSliderView sliderView = new ImageSliderView(getActivity());
            sliderView.empty(R.drawable.image_empty);
            Glide.with(this).load(bean.getBanner()).crossFade().into(sliderView.getImageView());
            sliderView.setPageTitle(bean.getName());
            sliderLayout.addSlider(sliderView);
            sliderView.setOnSliderClickListener(new OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    SearchResultActivity.open(getActivity(), bean.getKeyword());
                }
            });
        }
        sliderLayout.setViewPagerIndicator(circlePageIndicator);//为viewpager设置指示器
        circlePageIndicator.requestLayout();
        sliderLayout.setCycling(true);
    }

    @Override
    public void onSwipeRefresh() {
        albumPresenter.requestBanner();
        albumPresenter.requestAlbumIndex();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void getMusics(List<WikiBean> newMusics, List<WikiBean> hotMusics) {
        albumAdapter.setNewMusics(newMusics);
        albumAdapter.setHotMusics(hotMusics);
        refreshView.notifySwipeFinish();
    }

    @Override
    public void getBanner(List<BannerBean> been) {
        initSlider(been);
    }

    public void onEvent(FavEvent favEvent) {
        albumAdapter.updateWikiFav(favEvent.getWikiId(), favEvent.isFav());
    }

    @Override
    public void loadFail(String msg) {
        refreshView.notifySwipeFinish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!allSubscription.isUnsubscribed()) {
            allSubscription.unsubscribe();
        }
    }
}
