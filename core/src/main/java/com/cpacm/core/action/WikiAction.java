package com.cpacm.core.action;

import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.bean.data.WikiData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.WikiIPresenter;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/8/18
 * @desciption:
 */
public class WikiAction extends BaseAction {

    public final static String WIKI_ANIME = "anime";//动漫，可使用anime统一替代tv、ova、oad、movie使用
    public final static String WIKI_COMIC = "comic";//漫画
    public final static String WIKI_MUSIC = "music";//音乐专辑
    public final static String WIKI_RADIO = "radio";//音乐电台

    private WikiIPresenter wikiPresenter;
    private WikiService wikiService;

    public WikiAction(WikiIPresenter wikiPresenter) {
        super(HttpUtil.WIKIS);
        this.wikiPresenter = wikiPresenter;
        wikiService = retrofit.create(WikiService.class);
    }

    public void getWikis(String wikiType, int page, int perPage) {
        authorization = getOauthHeader(url + "?wiki_type=" + wikiType + "&page=" + page + "&perpage=" + perPage);
        wikiService.getWikis(authorization, wikiType, page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<WikiData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        wikiPresenter.wikiFail(HttpUtil.NETWORK_ERROR);
                    }

                    @Override
                    public void onNext(ApiResponse<WikiData> wikiDataApiResponse) {
                        if (!wikiDataApiResponse.getResponse().getInformation().isHas_error()) {
                            wikiPresenter.getWikis(wikiDataApiResponse.getResponse().getWikis());
                            int curPage = wikiDataApiResponse.getResponse().getInformation().getPage();
                            int count = wikiDataApiResponse.getResponse().getInformation().getCount();
                            wikiPresenter.updateCount(curPage, count);
                        } else {
                            wikiPresenter.wikiFail(wikiDataApiResponse.getResponse().getInformation().getMsg().toString());
                        }
                    }
                });
    }

    public interface WikiService {
        @GET(HttpUtil.WIKIS)
        Observable<ApiResponse<WikiData>> getWikis(
                @Header("Authorization") String authorization,
                @Query("wiki_type") String wiki_type,
                @Query("page") int page,
                @Query("perpage") int perpage);

    }
}
