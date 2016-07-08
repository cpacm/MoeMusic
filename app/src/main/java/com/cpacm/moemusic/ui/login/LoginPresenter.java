package com.cpacm.moemusic.ui.login;

import com.cpacm.core.action.account.OauthAction;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.db.dao.AccountDao;
import com.cpacm.core.http.RetrofitManager;
import com.cpacm.core.mvp.presenters.LoginIPresenter;
import com.cpacm.core.mvp.views.LoginIView;
import com.github.scribejava.core.model.OAuth1AccessToken;

/**
 * @author: cpacm
 * @date: 2016/7/5
 * @desciption: 登录逻辑处理
 */
public class LoginPresenter implements LoginIPresenter {

    private LoginIView loginIView;
    private OauthAction oauthAction;

    public LoginPresenter(LoginIView loginIView) {
        this.loginIView = loginIView;
        oauthAction = new OauthAction(this);
    }

    public void login() {
        oauthAction.startOauth();
    }

    public void getAccessToken(String verifier) {
        oauthAction.getAccessToken(verifier);
    }

    @Override
    public void OauthRedirect(String url) {
        loginIView.OauthRedirect(url);
    }

    public void LoginFailed(String s) {
        loginIView.LoginFailed(s);
    }

    @Override
    public void LoginSuccess(OAuth1AccessToken accessToken) {
        SettingManager.getInstance().setSetting(SettingManager.ACCESS_TOKEN, accessToken.getToken());
        SettingManager.getInstance().setSetting(SettingManager.ACCESS_TOKEN_SECRET, accessToken.getTokenSecret());
        RetrofitManager.getInstance().build();
        loginIView.LoginSuccess();
    }

    @Override
    public void LoginFailed(Throwable e) {
        loginIView.LoginFailed();
    }
}
