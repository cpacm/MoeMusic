package com.cpacm.core.bean;

import java.io.Serializable;

/**
 * @author: cpacm
 * @date: 2016/7/15
 * @desciption: meta标签
 */
public class MetaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * meta_key : 录音
     * meta_value : ERJ
     * meta_type : 1
     */

    private String meta_key;
    private Object meta_value;
    private int meta_type;

    public String getMeta_key() {
        return meta_key;
    }

    public void setMeta_key(String meta_key) {
        this.meta_key = meta_key;
    }

    public Object getMeta_value() {
        return meta_value;
    }

    public void setMeta_value(Object meta_value) {
        this.meta_value = meta_value;
    }

    public int getMeta_type() {
        return meta_type;
    }

    public void setMeta_type(int meta_type) {
        this.meta_type = meta_type;
    }
}
