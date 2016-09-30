package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/9/30
 * @desciption: 收藏夹关系实体
 */

public class CollectionShipBean {

    private int id;
    private int cid;//收藏夹id
    private int sid;//歌曲id

    public CollectionShipBean(int id, int cid, int sid) {
        this.id = id;
        this.cid = cid;
        this.sid = sid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
