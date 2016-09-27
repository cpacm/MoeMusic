package com.cpacm.core.action;

import com.cpacm.core.bean.FavBean;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.bean.data.FavouriteData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.FavouriteIPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/9/27
 * @desciption: 获取用户的收藏
 */

public class FavouriteAction extends BaseAction {

    private FavouriteIPresenter favouritePresenter;
    private FavouriteService favService;

    public FavouriteAction(FavouriteIPresenter favouritePresenter) {
        super(HttpUtil.USER_FAVOURITE);
        this.favouritePresenter = favouritePresenter;
        favService = retrofit.create(FavouriteService.class);
    }

    public void getFavWikis(int page) {
        authorization = getOauthHeader(url + "?page=" + page);
        favService.getFavouriteWikis(authorization, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<FavouriteData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        favouritePresenter.wikiFail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<FavouriteData> favouriteDataApiResponse) {
                        if (!favouriteDataApiResponse.getResponse().getInformation().isHas_error()) {
                            int curPage = favouriteDataApiResponse.getResponse().getInformation().getPage();
                            int count = favouriteDataApiResponse.getResponse().getInformation().getCount();
                            int perPage = favouriteDataApiResponse.getResponse().getInformation().getPerpage();
                            favouritePresenter.updateCount(curPage, perPage, count);
                            List<WikiBean> wikis = new ArrayList<>();
                            List<FavBean> favs = favouriteDataApiResponse.getResponse().getFavs();
                            for (FavBean fav : favs) {
                                wikis.add(fav.getObj());
                            }
                            favouritePresenter.getWikis(wikis);
                        } else {
                            favouritePresenter.wikiFail(favouriteDataApiResponse.getResponse().getInformation().getMsg().toString());
                        }
                    }
                });
    }

    public interface FavouriteService {

        @GET(HttpUtil.USER_FAVOURITE)
        Observable<ApiResponse<FavouriteData>> getFavouriteWikis(
                @Header("Authorization") String authorization,
                @Query("page") int page);
    }
}
