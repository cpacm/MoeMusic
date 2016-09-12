package com.cpacm.moemusic.ui.web;

import android.webkit.JavascriptInterface;

/**
 * @author: cpacm
 * @date: 2016/7/5
 * @desciption:
 */
public class WebAppBridge {

    private OauthLoginImpl oauthLogin;

    public WebAppBridge(OauthLoginImpl oauthLogin) {
        this.oauthLogin = oauthLogin;
    }

    @JavascriptInterface
    public void getResult(String str) {
        if (oauthLogin != null)
            oauthLogin.getResult(str);
    }

    public interface OauthLoginImpl {
        void getResult(String s);
    }

}
