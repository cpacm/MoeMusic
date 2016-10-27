package com.cpacm.core.Pixiv;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author: cpacm
 * @date: 2016/10/27
 * @desciption: cookie保存器
 */

public class CookieStore implements CookieJar {

    private Map<HttpUrl, List<Cookie>> cookieMap;

    public CookieStore() {
        cookieMap = new ConcurrentHashMap<>();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieMap.put(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieMap.get(url);
    }
}
