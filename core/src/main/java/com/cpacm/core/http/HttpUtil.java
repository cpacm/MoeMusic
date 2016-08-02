package com.cpacm.core.http;

/**
 * @auther: cpacm
 * @date: 2016/6/23
 * @desciption: 请求链接
 */
public class HttpUtil {

    public final static int DEFAULT_TIMEOUT = 10; //超时时间
    public final static String BASE_URL = "http://api.moefou.org/";
    public final static String BASE_FM_URL = "http://moe.fm/";
    public final static String NETWORK_ERROR = "网络出错";

    public final static String HITOKOTO_RAND = "http://api.hitokoto.us/rand";//http://api.hitokoto.us/rand

    //萌否注册登入页面
    public final static String REGISTER_URL = "http://moefou.org/register?redirect=http%3A%2F%2Fmoe.fm%2Flogin";

    /*########### BASE api ###########*/
    //获取用户信息
    public final static String ACCOUNT_DETAIL = "user/detail.json";

    //获取wikis
    public final static String WIKIS = "wikis.json";

    /*########### FM api ###########*/
    //发现音乐
    public final static String EXPLORE = "explore"; //explore?api=json&hot_musics=1&musics=1



}
