package com.cpacm.core.bean;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public class PixivBean {
    /**
     * illust_id : 59640232
     * title : 艦これまとめ11
     * width : 1000
     * height : 685
     * date : 2016年10月26日 01:08
     * tags : ["艦隊これくしょん","提督LOVE","柔らか深雪(手のりサイズ)","提督殿と将校殿","腰布","大天使時雨","深雪スペシャル(提督は萌え死ぬ)","艦これアーケード","山元提督","艦これ5000users入り"]
     * url : http://i1.pixiv.net/c/240x480/img-master/img/2016/10/26/01/08/49/59640232_p0_master1200.jpg
     * illust_type : 0
     * illust_book_style : 0
     * illust_page_count : 37
     * illust_upload_timestamp : 1477411729
     * user_id : 1227869
     * user_name : deco
     * profile_img : http://i3.pixiv.net/user-profile/img/2010/07/09/15/47/20/1943362_c29a20e7c61bf55a64d28bebd98edadd_50.jpg
     * rank : 1
     * yes_rank : 2
     * total_score : 23209
     * view_count : 82971
     * illust_content_type : {"sexual":0,"lo":false,"grotesque":false,"violent":false,"homosexual":false,"drug":false,"thoughts":false,"antisocial":false,"religion":false,"original":false,"furry":false,"bl":false,"yuri":false}
     * attr :
     */

    private int illust_id;
    private String title;
    private String date;
    private String url;
    private String attr;
    private List<String> tags;

    public int getIllust_id() {
        return illust_id;
    }

    public void setIllust_id(int illust_id) {
        this.illust_id = illust_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
