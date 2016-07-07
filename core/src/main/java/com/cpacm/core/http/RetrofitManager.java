package com.cpacm.core.http;


import com.cpacm.core.cache.SettingManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @auther: cpacm
 * @date: 2016/6/24
 * @desciption: Retrofit初始化
 */
public class RetrofitManager {

    private static RetrofitManager ourInstance;
    private Retrofit retrofit;
    private String accessToken;
    private String accessTokenSecret;
    private String baseUrl = HttpUtil.BASE_URL;

    public static RetrofitManager getInstance() {
        if (ourInstance == null)
            ourInstance = new RetrofitManager();
        return ourInstance;
    }

    private RetrofitManager() {
    }

    public void build() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(HttpUtil.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public Retrofit getRetrofit() {
        if (retrofit == null)
            build();
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public String getAccessToken() {
        if (accessToken == null)
            accessToken = SettingManager.getInstance().getSetting(SettingManager.ACCESS_TOKEN);
        return accessToken;
    }

    public String getAccessTokenSecret() {
        if (accessTokenSecret == null) {
            accessTokenSecret = SettingManager.getInstance().getSetting(SettingManager.ACCESS_TOKEN_SECRET);
        }
        return accessTokenSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
