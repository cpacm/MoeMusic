package com.cpacm.core.bean;

import java.io.Serializable;

/**
 * @author: cpacm
 * @date: 2016/7/15
 * @desciption: 收藏实体
 */
public class FavBean implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * fav_id : 162980
     * fav_obj_id : 9797
     * fav_obj_type : music
     * fav_uid : 54362
     * fav_date : 1468401778
     * fav_type : 1
     */

    private long fav_id;
    private long fav_obj_id;
    private String fav_obj_type;
    private long fav_uid;
    private long fav_date;
    private int fav_type;
    private WikiBean obj;

    public long getFav_id() {
        return fav_id;
    }

    public void setFav_id(long fav_id) {
        this.fav_id = fav_id;
    }

    public long getFav_obj_id() {
        return fav_obj_id;
    }

    public void setFav_obj_id(long fav_obj_id) {
        this.fav_obj_id = fav_obj_id;
    }

    public String getFav_obj_type() {
        return fav_obj_type;
    }

    public void setFav_obj_type(String fav_obj_type) {
        this.fav_obj_type = fav_obj_type;
    }

    public long getFav_uid() {
        return fav_uid;
    }

    public void setFav_uid(long fav_uid) {
        this.fav_uid = fav_uid;
    }

    public long getFav_date() {
        return fav_date;
    }

    public void setFav_date(long fav_date) {
        this.fav_date = fav_date;
    }

    public int getFav_type() {
        return fav_type;
    }

    public void setFav_type(int fav_type) {
        this.fav_type = fav_type;
    }

    public WikiBean getObj() {
        return obj;
    }

    public void setObj(WikiBean obj) {
        this.obj = obj;
    }
}
