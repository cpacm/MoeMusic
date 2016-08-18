package com.cpacm.core.bean;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: upload song
 */
public class UploadBean {

    /**
     * up_id : 82729
     * up_uid : 50611
     * up_obj_id : 211361
     * up_obj_type : song
     * up_uri : /1/d/16/8eeb5f7d902d53f146a4f8dd9b338d0f_128.mp3
     * up_type : mp3
     * up_md5 : 1d16674b1ed969c22ded41215e832aeb
     * up_size : 5137
     * up_quality : 128k
     * up_data : {"bitrate":320,"length":327.184,"time":"05:27","filesize":13151312}
     * up_date : 1442546460
     * up_url : http://nyan.90g.org/1/d/16/8eeb5f7d902d53f146a4f8dd9b338d0f_128.mp3
     */

    private long up_id;
    private long up_uid;
    private long up_obj_id;
    private String up_obj_type;
    private String up_uri;
    private String up_type;
    private String up_md5;
    private int up_size;
    private String up_quality;
    private Object up_data;
    private int up_date;
    private String up_url;

    public long getUp_id() {
        return up_id;
    }

    public void setUp_id(long up_id) {
        this.up_id = up_id;
    }

    public long getUp_uid() {
        return up_uid;
    }

    public void setUp_uid(long up_uid) {
        this.up_uid = up_uid;
    }

    public long getUp_obj_id() {
        return up_obj_id;
    }

    public void setUp_obj_id(long up_obj_id) {
        this.up_obj_id = up_obj_id;
    }

    public String getUp_obj_type() {
        return up_obj_type;
    }

    public void setUp_obj_type(String up_obj_type) {
        this.up_obj_type = up_obj_type;
    }

    public String getUp_uri() {
        return up_uri;
    }

    public void setUp_uri(String up_uri) {
        this.up_uri = up_uri;
    }

    public String getUp_type() {
        return up_type;
    }

    public void setUp_type(String up_type) {
        this.up_type = up_type;
    }

    public String getUp_md5() {
        return up_md5;
    }

    public void setUp_md5(String up_md5) {
        this.up_md5 = up_md5;
    }

    public int getUp_size() {
        return up_size;
    }

    public void setUp_size(int up_size) {
        this.up_size = up_size;
    }

    public String getUp_quality() {
        return up_quality;
    }

    public void setUp_quality(String up_quality) {
        this.up_quality = up_quality;
    }

    public Object getUp_data() {
        return up_data;
    }

    public void setUp_data(Object up_data) {
        this.up_data = up_data;
    }

    public int getUp_date() {
        return up_date;
    }

    public void setUp_date(int up_date) {
        this.up_date = up_date;
    }

    public String getUp_url() {
        return up_url;
    }

    public void setUp_url(String up_url) {
        this.up_url = up_url;
    }

}
