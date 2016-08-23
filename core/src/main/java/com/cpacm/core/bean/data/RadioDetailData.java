package com.cpacm.core.bean.data;

import com.cpacm.core.bean.RelationshipBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption: 电台数据
 */
public class RadioDetailData extends ResponseData<Object, Object> {

    private List<RelationshipBean> relationships;

    public List<RelationshipBean> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipBean> relationships) {
        this.relationships = relationships;
    }
}
