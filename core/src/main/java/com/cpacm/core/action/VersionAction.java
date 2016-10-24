package com.cpacm.core.action;

import com.cpacm.core.bean.VersionBean;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.VersionIPresenter;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/10/24
 * @desciption: 获取版本信息
 */

public class VersionAction extends BaseAction {

    private VersionIPresenter versionPresenter;
    private VersionService versionService;

    public VersionAction(VersionIPresenter versionPresenter) {
        super(HttpUtil.VERSION_URL);
        this.versionPresenter = versionPresenter;
        versionService = retrofit.create(VersionService.class);
    }

    /**
     * 从github上获取apk版本的信息
     */
    public void getVersion() {
        versionService.getVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VersionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        versionPresenter.fail(e.getMessage());
                    }

                    @Override
                    public void onNext(VersionBean bean) {
                        versionPresenter.getVersion(bean);
                    }
                });
    }


    public interface VersionService {
        @GET(HttpUtil.VERSION_URL)
        Observable<VersionBean> getVersion();
    }
}
