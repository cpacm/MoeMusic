package com.cpacm.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * 萌否 oauth 验证
 * Auther: cpacm
 * Date: 2016/6/24.
 */
public class MoefouApi extends DefaultApi10a {

    //api参考
    //http://moefou.herokuapp.com/

    private static final String CONSUMERID = "280";
    public static final String CONSUMERKEY = "d7aa3a9ab98e4bd388f28df27bf57f970576bab43";
    public static final String CONSUMERSECRET = "fbcf68cb04e4ebee661b733601165437";

    private static final String REQUEST_TOKEN_URL = "http://api.moefou.org/oauth/request_token";//获取request_token
    private static final String AUTHORIZE_URL = "http://api.moefou.org/oauth/authorize";//获取用户授权
    private static final String ACCESS_TOKEN = "http://api.moefou.org/oauth/access_token";//获取access_token

    public MoefouApi() {
    }

    private static class InstanceHolder {
        private static final MoefouApi INSTANCE = new MoefouApi();
    }

    public static MoefouApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return AUTHORIZE_URL + "?oauth_token=" + requestToken.getToken() + "&oauth_token_secret=" + requestToken.getTokenSecret();
    }
}
