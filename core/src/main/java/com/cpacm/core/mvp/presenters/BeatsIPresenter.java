package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.AccountBean;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 首页p接口
 */
public interface BeatsIPresenter {
    void setUserDetail(AccountBean accountBean);
    void getUserFail(String msg);
}
