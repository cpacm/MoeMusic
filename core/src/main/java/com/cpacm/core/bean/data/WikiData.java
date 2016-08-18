package com.cpacm.core.bean.data;

import com.cpacm.core.bean.WikiBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption: wiki数据
 */
public class WikiData extends ResponseData<Object, Object> {

    private List<WikiBean> wikis;

    public List<WikiBean> getWikis() {
        return wikis;
    }

    public void setWikis(List<WikiBean> wikis) {
        this.wikis = wikis;
    }
}
