package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption: 电台关联实体
 */
public class RelationshipBean {

    /**
     * wr_id : 146413
     * wr_obj1 : 44722
     * wr_obj1_type : radio
     * wr_obj2 : 57486
     * wr_obj2_type : song
     * wr_order : 1
     * wr_about : 入宅作。命运石之门op。开门第一首歌，就从这里开始吧。El Psy Congroo
     * obj : {"sub_id":57486,"sub_parent_wiki":7397,"sub_parent":0,"sub_title":"Hacking to the Gate","sub_title_encode":"hackingtothegate","sub_type":"song","sub_order":"1","sub_meta":null,"sub_about":"","sub_comment_count":"0","sub_data":null,"sub_date":1303833600,"sub_modified":1318691430,"sub_url":"http://moefou.org/sub/song/57486","sub_fm_url":"http://moe.fm/song/57486","sub_view_title":"song.01 Hacking to the Gate","sub_upload":[{"up_id":35,"up_uid":1,"up_obj_id":57486,"up_obj_type":"song","up_uri":"/4/5/4c/b0349232084f046b1e7ccc3c5a805c69.mp3","up_type":"mp3","up_md5":"454cfb045e59eadfaba17f824d27e2fe","up_size":2005,"up_quality":"64k","up_data":{"bitrate":64,"length":277.914,"time":"04:38","filesize":2052288},"up_date":1318777849,"up_url":"http://nyan.90g.org/4/5/4c/b0349232084f046b1e7ccc3c5a805c69.mp3"}]}
     */

    private int wr_id;
    private int wr_obj1;
    private String wr_obj1_type;
    private int wr_obj2;
    private String wr_obj2_type;
    private int wr_order;
    private String wr_about;
    private WikiSubBean obj;

    public int getWr_id() {
        return wr_id;
    }

    public void setWr_id(int wr_id) {
        this.wr_id = wr_id;
    }

    public int getWr_obj1() {
        return wr_obj1;
    }

    public void setWr_obj1(int wr_obj1) {
        this.wr_obj1 = wr_obj1;
    }

    public String getWr_obj1_type() {
        return wr_obj1_type;
    }

    public void setWr_obj1_type(String wr_obj1_type) {
        this.wr_obj1_type = wr_obj1_type;
    }

    public int getWr_obj2() {
        return wr_obj2;
    }

    public void setWr_obj2(int wr_obj2) {
        this.wr_obj2 = wr_obj2;
    }

    public String getWr_obj2_type() {
        return wr_obj2_type;
    }

    public void setWr_obj2_type(String wr_obj2_type) {
        this.wr_obj2_type = wr_obj2_type;
    }

    public int getWr_order() {
        return wr_order;
    }

    public void setWr_order(int wr_order) {
        this.wr_order = wr_order;
    }

    public String getWr_about() {
        return wr_about;
    }

    public void setWr_about(String wr_about) {
        this.wr_about = wr_about;
    }

    public WikiSubBean getObj() {
        return obj;
    }

    public void setObj(WikiSubBean obj) {
        this.obj = obj;
    }
}
