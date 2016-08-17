package com.cpacm.core.bean.data;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/17
 * @desciption: 发现流行电台
 */
public class RadioData extends ResponseData<Object, String> {

    private List<WikiBean> hot_radios;

    public List<WikiBean> getHot_radios() {
        return hot_radios;
    }

    public void setHot_radios(List<WikiBean> hot_radios) {
        this.hot_radios = hot_radios;
    }
}
