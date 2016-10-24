package com.cpacm.core.mvp.presenters;

import com.cpacm.core.bean.VersionBean;

/**
 * @author: cpacm
 * @date: 2016/10/24
 * @desciption: 版本更新
 */

public interface VersionIPresenter {

    void getVersion(VersionBean versionBean);

    void fail(String msg);
}
