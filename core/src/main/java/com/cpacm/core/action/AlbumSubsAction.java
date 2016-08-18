package com.cpacm.core.action;

import com.cpacm.core.bean.data.AlbumDetailData;
import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.SubIPresenter;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption:
 */
public class AlbumSubsAction extends BaseAction {

    private SubIPresenter subPresenter;
    private AlbumSubService subService;

    public AlbumSubsAction(SubIPresenter presenter) {
        super(HttpUtil.SUBS);
        this.subPresenter = presenter;
        subService = retrofit.create(AlbumSubService.class);
    }

    public void getAlbumSubs(long wiki_id, int page, int perPage) {
        authorization = getOauthHeader(url + "?wiki_id=" + wiki_id + "&page=" + page + "&perpage=" + perPage);
        subService.getAlbumSubs(authorization, wiki_id, page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<AlbumDetailData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        subPresenter.fail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<AlbumDetailData> apiResponse) {
                        if (!apiResponse.getResponse().getInformation().isHas_error()) {
                            int curPage = apiResponse.getResponse().getInformation().getPage();
                            int count = apiResponse.getResponse().getInformation().getCount();
                            subPresenter.getSubs(apiResponse.getResponse().getSubs(), curPage, count);
                        }else{
                            subPresenter.fail(HttpUtil.NETWORK_ERROR);
                        }
                    }
                });
    }

    public interface AlbumSubService {

        @GET(HttpUtil.SUBS)
        Observable<ApiResponse<AlbumDetailData>> getAlbumSubs(
                @Header("Authorization") String authorization,
                @Query("wiki_id") long wiki_id,
                @Query("page") int page,
                @Query("perpage") int perpage
        );
    }
}
