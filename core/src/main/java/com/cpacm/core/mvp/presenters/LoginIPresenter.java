package com.cpacm.core.mvp.presenters;

import com.github.scribejava.core.model.OAuth1AccessToken;

/**
 * @author: cpacm
 * @date: 2016/7/5
 * @desciption: 登录的presenter接口
 */
public interface LoginIPresenter {
    void OauthRedirect(String url);

    void LoginSuccess(OAuth1AccessToken accessToken);

    void LoginFailed(Throwable e);
}
