package com.cpacm.core.action;

import com.cpacm.core.bean.data.AlbumData;
import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.AlbumIPresenter;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/8/1
 * @desciption:
 */
public class AlbumAction extends BaseFMAction {

    private AlbumIPresenter presenter;
    private AlbumService albumService;

    public AlbumAction(AlbumIPresenter presenter) {
        super(HttpUtil.EXPLORE);
        this.presenter = presenter;
        albumService = retrofit.create(AlbumService.class);
    }

    public void getAlbumIndex() {
        authorization = getOauthHeader(url + "?api=json&hot_musics=1&musics=1");
        albumService.getAlbumIndex(authorization, "json", 1, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ApiResponse<AlbumData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.loadMusicFail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<AlbumData> response) {
                        if (response.getResponse().getInformation().isHas_error()) {
                            presenter.loadMusicFail((response.getResponse().getInformation().getMsg()));
                        } else {
                            presenter.getMusics(response.getResponse().getMusics(), response.getResponse().getHot_musics());
                        }
                    }
                });
    }


    interface AlbumService {

        //http://moe.fm/explore?api=json&hot_musics=1&musics=1
        @GET(HttpUtil.EXPLORE)
        Observable<ApiResponse<AlbumData>> getAlbumIndex(
                @Header("Authorization") String authorization,
                @Query("api") String api,
                @Query("hot_musics") int hotMusicEnable,
                @Query("musics") int musicEnable
        );
    }
}
