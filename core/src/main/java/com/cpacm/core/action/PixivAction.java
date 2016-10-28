package com.cpacm.core.action;

import com.cpacm.core.bean.data.PixivData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.PixivIPresenter;

import retrofit2.http.GET;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public class PixivAction extends BaseAction {

    private PixivIPresenter pixivPresenter;
    private PixivDailyService pixivDailyService;

    public PixivAction(PixivIPresenter pixivIPresenter) {
        super(HttpUtil.PIXIV_DAILY_URL);
        pixivPresenter = pixivIPresenter;
        pixivDailyService = retrofit.create(PixivDailyService.class);
    }

    public void requestPixivDaily() {
        pixivDailyService.getPixivDaily()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PixivData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pixivPresenter.fail(e.getMessage());
                    }

                    @Override
                    public void onNext(PixivData pixivData) {
                        pixivPresenter.getPixivPics(pixivData.getContents());
                    }
                });

    }


    interface PixivDailyService {

        @GET(HttpUtil.PIXIV_DAILY_URL)
        Observable<PixivData> getPixivDaily();

    }
}
