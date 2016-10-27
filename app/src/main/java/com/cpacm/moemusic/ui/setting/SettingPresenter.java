package com.cpacm.moemusic.ui.setting;

import com.cpacm.core.CoreApplication;
import com.cpacm.core.action.VersionAction;
import com.cpacm.core.bean.VersionBean;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.http.RetrofitManager;
import com.cpacm.core.mvp.presenters.VersionIPresenter;
import com.cpacm.core.mvp.views.SettingIView;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.core.utils.MoeLogger;
import com.cpacm.core.utils.SystemParamsUtils;
import com.cpacm.moemusic.MoeApplication;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * @author: cpacm
 * @date: 2016/10/24
 * @desciption: 设置界面逻辑
 */

public class SettingPresenter implements VersionIPresenter {

    private SettingIView settingView;
    private VersionAction versionAction;

    public SettingPresenter(SettingIView settingIView) {
        this.settingView = settingIView;
        versionAction = new VersionAction(this);
    }

    public void initSetting() {
        boolean notify = SettingManager.getInstance().getSetting(SettingManager.SETTING_NOTIFY, true);
        boolean wifi = SettingManager.getInstance().getSetting(SettingManager.SETTING_WIFI, false);
        settingView.setting(notify, wifi);
    }

    public void setNotify(boolean notify) {
        SettingManager.getInstance().setSetting(SettingManager.SETTING_NOTIFY, notify);
    }

    public void setWifi(boolean wifi) {
        SettingManager.getInstance().setSetting(SettingManager.SETTING_WIFI, wifi);
    }

    public void update() {
        versionAction.getVersion();
    }

    public void logout(){
        //退出登录
        SettingManager.getInstance().clearAccount();
        RetrofitManager.getInstance().clear();
        MoeApplication.getInstance().closeAllActivity();
    }

    @Override
    public void getVersion(VersionBean versionBean) {
        int code = versionBean.getVersion_code();
        int curVersion = SystemParamsUtils.getAppVersionCode(CoreApplication.getInstance());
        if (code > curVersion) {
            settingView.updateApk(true, versionBean.getApk(), versionBean.getDescription());
        } else {
            settingView.updateApk(false, null, null);
        }
    }

    /**
     * 下载apk
     * @param apkUrl
     */
    public void apkDownload(String apkUrl) {
        BaseDownloadTask task = FileDownloader.getImpl().create(apkUrl);
        task.setPath(FileUtils.getApkPath());
        task.setListener(new FileDownloadSampleListener() {
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                MoeLogger.d("apk download:" + soFarBytes * 100 / totalBytes);
                int progress = soFarBytes * 100 / totalBytes;
                settingView.downloadProgress(progress);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                settingView.downloadProgress(100);
                File file = new File(task.getPath());
                FileUtils.openApk(CoreApplication.getInstance(), file);
            }
        });
        task.start();

    }

    @Override
    public void fail(String msg) {
        settingView.updateApk(false, null, null);
    }
}
