package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.RelationshipBean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption: 电台列表接口
 */
public interface RadioSubIPresenter {

    void getRadioSubs(List<RelationshipBean> subs);

    void fail(String msg);
}
