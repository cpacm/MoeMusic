package com.cpacm.core.action;

import com.cpacm.core.bean.data.AccountData;
import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.BeatsIPresenter;

import java.util.concurrent.TimeUnit;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 用户信息
 */
public class AccountDetailAction extends BaseAction {

    public AccountDetailService detailService;
    private BeatsIPresenter beatsPresenter;

    public AccountDetailAction(BeatsIPresenter beatsPresenter) {
        super(HttpUtil.ACCOUNT_DETAIL);
        this.beatsPresenter = beatsPresenter;
        detailService = retrofit.create(AccountDetailService.class);
    }

    public Subscriber<ApiResponse<AccountData>> getSubscriber() {
        return new Subscriber<ApiResponse<AccountData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                beatsPresenter.getUserFail(parseThrowable(e));
            }

            @Override
            public void onNext(ApiResponse<AccountData> response) {
                if (response.getResponse().getInformation().isHas_error()) {
                    beatsPresenter.getUserFail(response.getResponse().getInformation().getMsg().get(0));
                } else {
                    beatsPresenter.setUserDetail(response.getResponse().getUser());
                }
            }
        };
    }

    public void getAccount() {
        detailService.getAccount(authorization)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getSubscriber());
    }

    public void getAccount(int uid) {
        authorization = getOauthHeader(url + "?uid=" + uid);
        detailService.getAccount(authorization, uid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getSubscriber());
    }

    public void getAccount(String username) {
        authorization = getOauthHeader(url + "?user_name=" + username);
        detailService.getAccount(authorization, username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getSubscriber());
    }

    interface AccountDetailService {
        @GET(HttpUtil.ACCOUNT_DETAIL)
        Observable<ApiResponse<AccountData>> getAccount(@Header("Authorization") String authorization);

        @GET(HttpUtil.ACCOUNT_DETAIL)
        Observable<ApiResponse<AccountData>> getAccount(@Header("Authorization") String authorization, @Query("uid") int uid);

        @GET(HttpUtil.ACCOUNT_DETAIL)
        Observable<ApiResponse<AccountData>> getAccount(@Header("Authorization") String authorization, @Query("user_name") String username);
    }
}
