package com.cpacm.core.action;

import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.bean.data.ResponseData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.FavIPresenter;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/8/22
 * @desciption: 音乐的收藏和取消收藏
 */
public class FavAction extends BaseAction {

    private FavService favService;
    private FavIPresenter favPresenter;

    public FavAction(FavIPresenter favPresenter) {
        super(HttpUtil.FAV);
        this.favPresenter = favPresenter;
        favService = retrofit.create(FavService.class);

    }

    public void fav(String wikiType, long wikiId, String content) {
        authorization = getOauthHeader(url + "?fav_obj_type=" + wikiType + "&fav_obj_id=" + wikiId + "&fav_type=1&save_status=1&status_content=" + content);
        favService.addFav(authorization, wikiType, wikiId, 1, 1, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<ResponseData<Object, Object>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        favPresenter.fail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<ResponseData<Object, Object>> responseData) {
                        if (!responseData.getResponse().getInformation().isHas_error()) {
                            favPresenter.favSuccess();
                        } else {
                            favPresenter.fail(HttpUtil.NETWORK_ERROR);
                        }
                    }
                });
    }

    public void unFav(String wikiType, long wikiId) {
        authorization = getOauthHeader(HttpUtil.BASE_URL + HttpUtil.UNFAV + "?fav_obj_type=" + wikiType + "&fav_obj_id=" + wikiId);
        favService.deleteFav(authorization, wikiType, wikiId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<ResponseData<Object, Object>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        favPresenter.fail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<ResponseData<Object, Object>> responseData) {
                        if (!responseData.getResponse().getInformation().isHas_error()) {
                            favPresenter.unFavSuccess();
                        } else {
                            favPresenter.fail(HttpUtil.NETWORK_ERROR);
                        }
                    }
                });
    }

    public interface FavService {
        @GET(HttpUtil.FAV)
        Observable<ApiResponse<ResponseData<Object, Object>>> addFav(@Header("Authorization") String authorization,
                                                                     @Query("fav_obj_type") String wikiType,
                                                                     @Query("fav_obj_id") long wikiId,
                                                                     @Query("fav_type") int type,
                                                                     @Query("save_status") int saveStatus,
                                                                     @Query("status_content") String content);

        @GET(HttpUtil.UNFAV)
        Observable<ApiResponse<ResponseData<Object, Object>>> deleteFav(@Header("Authorization") String authorization,
                                                                        @Query("fav_obj_type") String wikiType,
                                                                        @Query("fav_obj_id") long wikiId);
    }
}
