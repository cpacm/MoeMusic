package com.cpacm.core.action;

import com.cpacm.core.CoreApplication;
import com.cpacm.core.R;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.http.RetrofitManager;
import com.cpacm.core.oauth.MoefouApi;
import com.cpacm.core.utils.MoeLogger;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import retrofit2.Retrofit;

/**
 * @auther: cpacm
 * @date: 2016/6/24
 * @desciption: 基础action
 */
public abstract class BaseAction {

    protected Retrofit retrofit;
    protected String accessToken, accessTokenSecret;
    private String baseUrl;
    protected String url;
    protected String authorization;

    public BaseAction(String shortUrl) {
        this.retrofit = RetrofitManager.getInstance().getRetrofit();
        this.accessToken = RetrofitManager.getInstance().getAccessToken();
        this.accessTokenSecret = RetrofitManager.getInstance().getAccessTokenSecret();
        this.baseUrl = HttpUtil.BASE_URL;
        this.url = baseUrl + shortUrl;
        this.authorization = getOauthHeader(url);
    }

    public String getOauthHeader(String url) {
        MoeLogger.d(accessToken);
        MoeLogger.d(accessTokenSecret);
        OAuth1AccessToken oauthToken = new OAuth1AccessToken(accessToken, accessTokenSecret);
        OAuth10aService service = new ServiceBuilder()
                .apiKey(MoefouApi.CONSUMERKEY)
                .apiSecret(MoefouApi.CONSUMERSECRET)
                .build(MoefouApi.instance());
        final OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
        service.signRequest(oauthToken, request);
        StringBuilder header = new StringBuilder();
        header.append(request.getHeaders().get("Authorization"));
        return header.toString();
    }

    public String parseThrowable(Throwable e) {
        if (e != null && e.getMessage() != null && e.getMessage().equals("HTTP 401 Unauthorized")) {
            return HttpUtil.UNAUTHORIZED;
        }
        return HttpUtil.NETWORK_ERROR;
    }
}
