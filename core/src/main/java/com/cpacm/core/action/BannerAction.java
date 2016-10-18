package com.cpacm.core.action;

import com.cpacm.core.bean.BannerBean;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.BannerIPresenter;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: 轮播图数据请求
 */

public class BannerAction extends BaseAction {

    private BannerIPresenter bannerPresenter;
    private BannerService bannerService;


    public BannerAction(BannerIPresenter bannerPresenter) {
        super(HttpUtil.BANNER_URL);
        this.bannerPresenter = bannerPresenter;
        bannerService = retrofit.create(BannerService.class);
    }

    /**
     * 从github上获取banner的配置
     */
    public void getBanners() {
        bannerService.getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BannerBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        bannerPresenter.fail(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BannerBean> been) {
                        bannerPresenter.getBanners(been);
                    }
                });
    }

    public interface BannerService {

        @GET(HttpUtil.BANNER_URL)
        Observable<List<BannerBean>> getBanners();
    }
}
