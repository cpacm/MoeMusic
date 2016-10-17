package com.cpacm.core.action;

import com.cpacm.core.bean.data.ApiResponse;
import com.cpacm.core.bean.data.WikiData;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.SearchIPresenter;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/10/17
 * @desciption: 搜索
 */

public class SearchAction extends BaseAction {

    private SearchIPresenter searchPresenter;
    private SearchService searchService;

    public SearchAction(SearchIPresenter searchIPresenter) {
        super(HttpUtil.SEARCH);
        this.searchPresenter = searchIPresenter;
        searchService = retrofit.create(SearchService.class);
    }

    public void search(String keyword, String wiki_type, int page) {
        authorization = getOauthHeader(url + "?keyword=" + keyword + "&wiki_type=" + wiki_type + "&page=" + page);
        searchService
                .search(authorization, keyword, wiki_type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<WikiData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        searchPresenter.wikiFail(parseThrowable(e));
                    }

                    @Override
                    public void onNext(ApiResponse<WikiData> wikiDataApiResponse) {
                        if (!wikiDataApiResponse.getResponse().getInformation().isHas_error()) {
                            int curPage = wikiDataApiResponse.getResponse().getInformation().getPage();
                            int count = wikiDataApiResponse.getResponse().getInformation().getCount();
                            int perPage = wikiDataApiResponse.getResponse().getInformation().getPerpage();
                            searchPresenter.updateCount(curPage, perPage, count);
                            searchPresenter.getWikis(wikiDataApiResponse.getResponse().getWikis());
                        } else {
                            searchPresenter.wikiFail(wikiDataApiResponse.getResponse().getInformation().getMsg().toString());
                        }
                    }
                });
    }

    interface SearchService {

        //?keyword=lovelive&wiki_type=music&page=3
        @GET(HttpUtil.SEARCH)
        Observable<ApiResponse<WikiData>> search(
                @Header("Authorization") String authorization,
                @Query("keyword") String keyword,
                @Query("wiki_type") String wiki_type,
                @Query("page") int page
        );

    }
}
