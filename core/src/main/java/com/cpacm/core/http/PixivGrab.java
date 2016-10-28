package com.cpacm.core.http;

import android.net.Uri;
import android.text.TextUtils;

import com.cpacm.core.action.BaseAction;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.utils.MoeLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/10/27
 * @desciption: pixiv站 java 爬取工具
 */

public class PixivGrab {
    //http://i1.pixiv.net/c/240x480/img-master/img/2016/10/24/03/48/49/59615212_p0_master1200.jpg
    //http://i1.pixiv.net/img-original/img/2016/10/24/03/48/49/59615212_p0.jpg
    //http://i2.pixiv.net/c/240x480/img-master/img/2016/10/24/00/27/16/59612725_p0_master1200.jpg
    //http://i2.pixiv.net/img-original/img/2016/10/24/00/27/16/59612725_p0.png
    //http://i2.pixiv.net/c/240x480/img-master/img/2016/10/24/00/01/09/59612057_p0_master1200.jpg
    //http://i2.pixiv.net/img-original/img/2016/10/24/00/01/09/59612057_p0.png
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";

    private static final String BASE_URL = "https://www.secure.pixiv.net/";

    private static final String LOGIN_URL = "https://www.secure.pixiv.net/login.php";//mode=login skip=1 pixiv_id=user pass=123456

    private String cookie;

    public PixivGrab() {
    }

    private String loginPixiv(String pid, String pass) throws Exception {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");// 提交模式
        conn.setDoOutput(true);// 是否输入参数
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Language", "zh-cn");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Cache-Control", "no-cache");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mode=login&skip=1&pixiv_id=").append(URLEncoder.encode(pid, "UTF-8")).append("&pass=").append(pass);
        //conn.setRequestProperty("Cookie",cookies);
        byte[] bytes = stringBuilder.toString().getBytes();
        conn.getOutputStream().write(bytes);// 输入参数
        conn.getOutputStream().flush();
        conn.getOutputStream().close();

        String cookie = conn.getHeaderField("set-cookie");
        if (!TextUtils.isEmpty(cookie)) {
            this.cookie = cookie;
        }
        return cookie;

    }

    public void login(String pid, String pass) {
        Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            String cookie = loginPixiv("", "");
                            subscriber.onNext(cookie);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MoeLogger.e(e.toString());
                    }

                    @Override
                    public void onNext(String cookie) {
                        MoeLogger.e(cookie);
                    }
                });
    }


}
