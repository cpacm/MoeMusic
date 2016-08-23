package com.cpacm.core.action;

import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.bean.data.RadioDetailData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.RadioSubIPresenter;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption: 请求电台的列表数据
 */
public class RadioSubsAction extends BaseAction {

    private RadioSubIPresenter radioPresenter;
    private RadioSubService radioSubService;

    public RadioSubsAction(RadioSubIPresenter radioPresenter) {
        super(HttpUtil.RADIO_SUBS);
        this.radioPresenter = radioPresenter;
        radioSubService = retrofit.create(RadioSubService.class);
    }

    public void getRadioSubs(long wiki_id) {
        authorization = getOauthHeader(url + "?wiki_id=" + wiki_id);
        radioSubService.getRadioSubs(authorization, wiki_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<RadioDetailData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        radioPresenter.fail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<RadioDetailData> apiResponse) {
                        if (!apiResponse.getResponse().getInformation().isHas_error()) {
                            radioPresenter.getRadioSubs(apiResponse.getResponse().getRelationships());
                        } else {
                            radioPresenter.fail(HttpUtil.NETWORK_ERROR);
                        }
                    }
                });
    }

    public interface RadioSubService {
        @GET(HttpUtil.RADIO_SUBS)
        Observable<ApiResponse<RadioDetailData>> getRadioSubs(
                @Header("Authorization") String authorization,
                @Query("wiki_id") long wiki_id
        );
    }
}
