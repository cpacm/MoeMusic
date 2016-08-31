package com.cpacm.core.action;

import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.SongBean;
import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.bean.data.PlayListData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.PlaylistIPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/8/31
 * @desciption: 从服务器获取随机的播放列表
 */
public class PlayListAction extends BaseFMAction {

    private PlaylistIPresenter playlistPresenter;
    private PlayListService playListService;

    public PlayListAction(PlaylistIPresenter playlistPresenter) {
        super(HttpUtil.PLAYLIST);
        this.playlistPresenter = playlistPresenter;
        playListService = retrofit.create(PlayListService.class);
    }

    public void getPlayList() {
        authorization = getOauthHeader(url + "?api=json&perpage=20");
        playListService.getPlayList(authorization, "json", 20)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ApiResponse<PlayListData>, List<Song>>() {
                    @Override
                    public List<Song> call(ApiResponse<PlayListData> playListDataApiResponse) {
                        List<Song> songs = new ArrayList<>();
                        if (!playListDataApiResponse.getResponse().getInformation().isHas_error()) {
                            for (SongBean songBean : playListDataApiResponse.getResponse().getPlaylist()) {
                                songs.add(songBean.parseSong());
                            }
                        }
                        return songs;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Song>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        playlistPresenter.getPlaylistFail(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Song> songs) {
                        playlistPresenter.getPlayList(songs);
                    }
                });
    }

    public interface PlayListService {

        @GET(HttpUtil.PLAYLIST)
        Observable<ApiResponse<PlayListData>> getPlayList(@Header("Authorization") String authorization, @Query("api") String api, @Query("perpage") int perpage);
    }
}
