package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 用户实体
 */
public class AccountBean {

    /**
     * uid : 1
     * user_name : ariagle
     * user_nickname : 园长A
     * user_registered : 1270089058
     * user_lastactivity : 1326963034
     * user_url : http://moefou.org/home/ariagle
     * user_fm_url : http://moe.fm/home/ariagle
     * user_avatar : {"small":"http://nyan.moefou.org/avatar/00/00/000001_48.jpg","medium":"http://nyan.moefou.org/avatar/00/00/000001_120.jpg","large":"http://nyan.moefou.org/avatar/00/00/000001.jpg"}
     * groups : 56,43,39,38,24,4,2,1
     * follower : 1686,1618,1498,38,1589,1131,17
     * following : 1740,1710,1467,1686
     * msg : 0
     * about : Hello World
     */

    private int uid;
    private String user_name;
    private String user_nickname;
    private long user_registered;
    private long user_lastactivity;
    private String user_url;
    private String user_fm_url;
    private CoverBean user_avatar;
    private String groups;
    private String follower;
    private String following;
    private int msg;
    private String about;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public long getUser_registered() {
        return user_registered;
    }

    public void setUser_registered(long user_registered) {
        this.user_registered = user_registered;
    }

    public long getUser_lastactivity() {
        return user_lastactivity;
    }

    public void setUser_lastactivity(long user_lastactivity) {
        this.user_lastactivity = user_lastactivity;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    public String getUser_fm_url() {
        return user_fm_url;
    }

    public void setUser_fm_url(String user_fm_url) {
        this.user_fm_url = user_fm_url;
    }

    public CoverBean getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(CoverBean user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
