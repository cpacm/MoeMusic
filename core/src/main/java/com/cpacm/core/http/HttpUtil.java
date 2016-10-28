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
    public final static String UNAUTHORIZED = "用户授权认证失败";

    //萌否注册登入页面
    public final static String REGISTER_URL = "http://moefou.org/register?redirect=http%3A%2F%2Fmoe.fm%2Flogin";
    //banner
    public final static String BANNER_URL = "https://raw.githubusercontent.com/cpacm/MoeMusic/master/beats_banner.json";
    //version
    public final static String VERSION_URL = "https://raw.githubusercontent.com/cpacm/MoeMusic/master/beats_version.json";
    //pixiv每日排行榜
    public final static String PIXIV_DAILY_URL = "http://www.pixiv.net/ranking.php?mode=daily&format=json";//&p=1

    /*########### BASE api ###########*/

    public final static String ACCOUNT_DETAIL = "user/detail.json";//获取用户信息
    public final static String WIKIS = "wikis.json";//获取wikis
    public final static String ALBUM_SUBS = "music/subs.json";//获取专辑的歌曲信息
    public final static String RADIO_SUBS = "radio/relationships.json";//获取电台歌曲 wiki_id=44722
    public final static String FAV = "fav/add.json";//添加收藏
    public final static String UNFAV = "fav/delete.json";//取消收藏
    public final static String USER_FAVOURITE = "user/favs/wiki.json";//用户收藏
    public final static String SEARCH = "search/wiki.json";//搜索
    /*########### FM api ###########*/

    public final static String EXPLORE = "explore"; //发现音乐 explore?api=json&hot_musics=1&musics=1
    public final static String PLAYLIST = "listen/playlist";//获取播放列表  listen/playlist?api=json

/*    @GET("/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("/group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("/users/new")
    Call<User> createUser(@Body User user);*/

}
