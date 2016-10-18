package com.cpacm.moemusic.ui.beats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.SearchIView;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.adapters.MusicListAdapter;
import com.cpacm.moemusic.ui.widgets.recyclerview.RefreshRecyclerView;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/16.
 * @description: 搜索结果
 */

public class SearchResultActivity extends SearchActivity implements RefreshRecyclerView.RefreshListener, SearchIView {

    public static void open(Context context, String keyword) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
        intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
        intent.putExtra(EXTRA_KEY_TEXT, keyword);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RefreshRecyclerView refreshView;
    private FloatingActionButton searchFab;

    private MusicListAdapter musicListAdapter;
    private SearchPresenter searchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchPresenter = new SearchPresenter(this);

        initRefreshView();
        initSearch();
    }

    private void initRefreshView() {
        musicListAdapter = new MusicListAdapter(this);
        refreshView = (RefreshRecyclerView) findViewById(R.id.refresh_view);
        refreshView.setRefreshListener(this);
        refreshView.setAdapter(musicListAdapter);
    }

    private void initSearch() {
        setSearchView();
        customSearchView(true);
        if (getIntent() != null) {
            String key = getIntent().getStringExtra((EXTRA_KEY_TEXT));
            search(key);
        }
        searchView.setNavigationIcon(R.drawable.ic_search);

        searchFab = (FloatingActionButton) findViewById(R.id.search_fab);
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearch();
            }
        });

        searchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public void onOpen() {
                if (searchFab != null) {
                    searchFab.hide();
                }
            }

            @Override
            public void onClose() {
                if (searchFab != null) {
                    searchFab.show();
                }
            }
        });
    }

    private void toggleSearch() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
        } else {
            searchView.open(true);
        }
    }

    @CallSuper
    @Override
    protected void getData(String text, int position) {
        historyTable.addItem(new SearchItem(text));
        search(text);
    }

    private void search(String text) {
        searchView.setTextInput(text);
        searchPresenter.setKeyword(text);
        refreshView.startSwipeAfterViewCreate();
        String title = String.format(getString(R.string.search_format_title), text);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onSwipeRefresh() {
        searchPresenter.requestData();
    }

    @Override
    public void onLoadMore() {
        searchPresenter.loadMore();
    }

    @Override
    public void getWikiBean(List<WikiBean> wikis, boolean add, boolean hasMore) {
        if (add) {
            musicListAdapter.addData(wikis);
        } else {
            musicListAdapter.setData(wikis);
        }
        refreshView.notifySwipeFinish();
        refreshView.notifyLoadMoreFinish(hasMore);
    }

    @Override
    public void fail(String msg) {
        refreshView.notifySwipeFinish();
        refreshView.notifyLoadMoreFinish(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
