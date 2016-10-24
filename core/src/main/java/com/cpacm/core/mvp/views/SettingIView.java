package com.cpacm.core.mvp.views;

/**
 * @author: cpacm
 * @date: 2016/10/24
 * @desciption: 设置界面接口
 */

public interface SettingIView {

    void setting(boolean notify, boolean wifi);

    void updateApk(boolean update, String apkUrl, String description);

    void downloadProgress(int progress);

}
