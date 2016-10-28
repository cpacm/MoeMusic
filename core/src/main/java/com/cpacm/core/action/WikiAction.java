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
 * @desciption: wiki网络请求
 */
public class WikiAction extends BaseAction {

    private WikiIPresenter wikiPresenter;
    private WikiService wikiService;

    public WikiAction(WikiIPresenter wikiPresenter) {
        super(HttpUtil.WIKIS);
        this.wikiPresenter = wikiPresenter;
        wikiService = retrofit.create(WikiService.class);
    }

    private Subscriber<ApiResponse<WikiData>> getWikiSubscriber() {
        return new Subscriber<ApiResponse<WikiData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                wikiPresenter.wikiFail(parseThrowable(e));
            }

            @Override
            public void onNext(ApiResponse<WikiData> wikiDataApiResponse) {
                if (!wikiDataApiResponse.getResponse().getInformation().isHas_error()) {
                    int curPage = wikiDataApiResponse.getResponse().getInformation().getPage();
                    int count = wikiDataApiResponse.getResponse().getInformation().getCount();
                    int perPage = wikiDataApiResponse.getResponse().getInformation().getPerpage();
                    wikiPresenter.updateCount(curPage, perPage, count);
                    wikiPresenter.getWikis(wikiDataApiResponse.getResponse().getWikis());
                } else {
                    wikiPresenter.wikiFail(wikiDataApiResponse.getResponse().getInformation().getMsg().toString());
                }
            }
        };
    }

    public void getWikis(String wikiType, int page) {
        authorization = getOauthHeader(url + "?wiki_type=" + wikiType + "&page=" + page);
        wikiService.getWikis(authorization, wikiType, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWikiSubscriber());
    }

    public void getWikisByInital(String wikiType, int page, String initial) {
        authorization = getOauthHeader(url + "?wiki_type=" + wikiType + "&page=" + page + "&initial=" + initial);
        wikiService.getWikisByInital(authorization, wikiType, page, initial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWikiSubscriber());
    }

    public void getWikisByDate(String wikiType, int page, String date) {
        authorization = getOauthHeader(url + "?wiki_type=" + wikiType + "&page=" + page + "&date=" + date);
        wikiService.getWikisByDate(authorization, wikiType, page, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWikiSubscriber());
    }

    public void getWikis(String wikiType, int page, String initial, String date) {
        authorization = getOauthHeader(url + "?wiki_type=" + wikiType + "&page=" + page + "&initial=" + initial + "&date=" + date);
        wikiService.getWikis(authorization, wikiType, page, initial, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWikiSubscriber());
    }

    public void getWikiById(String wikiType, long wikiId) {
        authorization = getOauthHeader(url + "?wiki_type=" + wikiType + "&wiki_id=" + wikiId);
        wikiService.getWikiById(authorization, wikiType, wikiId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWikiSubscriber());
    }

    public interface WikiService {
        @GET(HttpUtil.WIKIS)
        Observable<ApiResponse<WikiData>> getWikis(
                @Header("Authorization") String authorization,
                @Query("wiki_type") String wiki_type,
                @Query("page") int page);

        @GET(HttpUtil.WIKIS)
        Observable<ApiResponse<WikiData>> getWikisByInital(
                @Header("Authorization") String authorization,
                @Query("wiki_type") String wiki_type,
                @Query("page") int page,
                @Query("initial") String initial);

        @GET(HttpUtil.WIKIS)
        Observable<ApiResponse<WikiData>> getWikisByDate(
                @Header("Authorization") String authorization,
                @Query("wiki_type") String wiki_type,
                @Query("page") int page,
                @Query("date") String date);

        @GET(HttpUtil.WIKIS)
        Observable<ApiResponse<WikiData>> getWikis(
                @Header("Authorization") String authorization,
                @Query("wiki_type") String wiki_type,
                @Query("page") int page,
                @Query("initial") String initial,
                @Query("date") String date);

        @GET(HttpUtil.WIKIS)
        Observable<ApiResponse<WikiData>> getWikiById(
                @Header("Authorization") String authorization,
                @Query("wiki_type") String wiki_type,
                @Query("wiki_id") long id);
    }
}
