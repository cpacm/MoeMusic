package com.cpacm.core.bean;

import com.cpacm.core.cache.SongManager;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: wiki sub
 */
public class WikiSubBean {
    /**
     * sub_id : 211361
     * sub_parent_wiki : 45917
     * sub_parent : 0
     * sub_title : Bravely You
     * sub_title_encode : bravelyyou
     * sub_type : song
     * sub_order : 1
     * sub_meta : null
     * sub_about :
     * sub_comment_count : 0
     * sub_data : null
     * sub_date : 1440518400
     * sub_modified : 1437219738
     * sub_url : http://moefou.org/sub/song/211361
     * sub_fm_url : http://moe.fm/song/211361
     * sub_view_title : song.01 Bravely You
     * sub_user_fav : null
     * sub_upload : [{}]
     */

    private long sub_id;
    private long sub_parent_wiki;
    private long sub_parent;
    private String sub_title;
    private String sub_title_encode;
    private String sub_type;
    private String sub_order;
    private Object sub_meta;
    private String sub_about;
    private String sub_comment_count;
    private Object sub_data;
    private int sub_date;
    private int sub_modified;
    private String sub_url;
    private String sub_fm_url;
    private String sub_view_title;
    private Object sub_user_fav;
    private List<UploadBean> sub_upload;

    public long getSub_id() {
        return sub_id;
    }

    public void setSub_id(long sub_id) {
        this.sub_id = sub_id;
    }

    public long getSub_parent_wiki() {
        return sub_parent_wiki;
    }

    public void setSub_parent_wiki(long sub_parent_wiki) {
        this.sub_parent_wiki = sub_parent_wiki;
    }

    public long getSub_parent() {
        return sub_parent;
    }

    public void setSub_parent(long sub_parent) {
        this.sub_parent = sub_parent;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getSub_title_encode() {
        return sub_title_encode;
    }

    public void setSub_title_encode(String sub_title_encode) {
        this.sub_title_encode = sub_title_encode;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getSub_order() {
        return sub_order;
    }

    public void setSub_order(String sub_order) {
        this.sub_order = sub_order;
    }

    public String getSub_about() {
        return sub_about;
    }

    public void setSub_about(String sub_about) {
        this.sub_about = sub_about;
    }

    public String getSub_comment_count() {
        return sub_comment_count;
    }

    public void setSub_comment_count(String sub_comment_count) {
        this.sub_comment_count = sub_comment_count;
    }

    public Object getSub_data() {
        return sub_data;
    }

    public void setSub_data(Object sub_data) {
        this.sub_data = sub_data;
    }

    public int getSub_date() {
        return sub_date;
    }

    public void setSub_date(int sub_date) {
        this.sub_date = sub_date;
    }

    public int getSub_modified() {
        return sub_modified;
    }

    public void setSub_modified(int sub_modified) {
        this.sub_modified = sub_modified;
    }

    public String getSub_url() {
        return sub_url;
    }

    public void setSub_url(String sub_url) {
        this.sub_url = sub_url;
    }

    public String getSub_fm_url() {
        return sub_fm_url;
    }

    public void setSub_fm_url(String sub_fm_url) {
        this.sub_fm_url = sub_fm_url;
    }

    public String getSub_view_title() {
        return sub_view_title;
    }

    public void setSub_view_title(String sub_view_title) {
        this.sub_view_title = sub_view_title;
    }

    public Object getSub_user_fav() {
        return sub_user_fav;
    }

    public void setSub_user_fav(Object sub_user_fav) {
        this.sub_user_fav = sub_user_fav;
    }

    public Object getSub_meta() {
        return sub_meta;
    }

    public void setSub_meta(Object sub_meta) {
        this.sub_meta = sub_meta;
    }

    public List<UploadBean> getSub_upload() {
        return sub_upload;
    }

    public void setSub_upload(List<UploadBean> sub_upload) {
        this.sub_upload = sub_upload;
    }

    public Song parseSong() {
        Song song = new Song();
        song.setId(sub_id);
        song.setAlbumId(sub_parent_wiki);
        song.setTitle(sub_title);
        song.setDate(sub_date);
        if (sub_upload != null && sub_upload.size() > 0) {
            UploadBean uploadBean = sub_upload.get(0);
            song.setStatus(true);
            song.setQuality(uploadBean.getUp_quality());
            song.setSize(uploadBean.getUp_size());
            song.setUrl(uploadBean.getUp_url());
        } else {
            song.setStatus(false);
        }
        SongManager.getInstance().updateSongFromLibrary(song);
        return song;
    }
}
