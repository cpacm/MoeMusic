package com.cpacm.core.action;

import android.util.Log;

import com.cpacm.core.mvp.presenters.LoginIPresenter;
import com.cpacm.core.oauth.MoefouApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @auther: cpacm
 * @date: 2016/6/30
 * @desciption: oauth验证
 */
public class OauthAction {

    OAuth10aService service;
    OAuth1RequestToken requestToken;
    private LoginIPresenter presenter;

    public OauthAction(LoginIPresenter presenter) {
        this.presenter = presenter;
    }

    public void startOauth() {
        Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        service = new ServiceBuilder()
                                .apiKey(MoefouApi.CONSUMERKEY)
                                .apiSecret(MoefouApi.CONSUMERSECRET)
                                .build(MoefouApi.instance());
                        try {
                            requestToken = service.getRequestToken();
                            String authUrl = service.getAuthorizationUrl(requestToken);
                            subscriber.onNext(authUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.LoginFailed(e);
                    }

                    @Override
                    public void onNext(String s) {
                        presenter.OauthRedirect(s);
                    }
                });
    }

    public void getAccessToken(final String verifier) {
        Observable.create(
                new Observable.OnSubscribe<OAuth1AccessToken>() {
                    @Override
                    public void call(Subscriber<? super OAuth1AccessToken> subscriber) {
                        final OAuth1AccessToken accessToken;
                        try {
                            accessToken = service.getAccessToken(requestToken, verifier);
                            subscriber.onNext(accessToken);

                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OAuth1AccessToken>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.LoginFailed(e);
                    }

                    @Override
                    public void onNext(OAuth1AccessToken token) {
                        presenter.LoginSuccess(token);
                    }
                });
    }

}
