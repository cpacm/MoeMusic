package com.cpacm.core.mvp.presenters;

/**
 * @author: cpacm
 * @date: 2016/7/5
 * @desciption: 登录的presenter接口
 */
public interface LoginIPresenter {
    void OauthRedirect(String url);

    void LoginSuccess(String accessToken);

    void LoginFailed(Throwable e);
}
