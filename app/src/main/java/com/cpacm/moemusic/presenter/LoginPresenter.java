package com.cpacm.moemusic.presenter;

import com.cpacm.core.action.OauthAction;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.mvp.presenters.LoginIPresenter;
import com.cpacm.core.mvp.views.LoginIView;

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

    public void getAccessToken(String verifier){
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
    public void LoginSuccess(String accessToken) {
        SettingManager.getInstance().setSetting(SettingManager.ACCESS_TOKEN,accessToken);
        loginIView.LoginSuccess();
    }

    @Override
    public void LoginFailed(Throwable e) {
        loginIView.LoginFailed();
    }
}
