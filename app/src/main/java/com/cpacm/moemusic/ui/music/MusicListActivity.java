package com.cpacm.moemusic.ui.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.mvp.views.MusicListIView;
import com.cpacm.core.utils.DateUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.DropMenuAdapter;
import com.cpacm.moemusic.ui.adapters.MusicListAdapter;
import com.cpacm.moemusic.ui.widgets.DropDownMenu;
import com.cpacm.moemusic.ui.widgets.recyclerview.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption: 专辑，电台列表界面
 */
public class MusicListActivity extends AbstractAppActivity implements RefreshRecyclerView.RefreshListener, MusicListIView {

    public static void open(Context context, String musicType) {
        Intent intent = new Intent(context, MusicListActivity.class);
        intent.putExtra("MusicType", musicType);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private DropDownMenu dropDownMenu;
    private List<View> popupViews = new ArrayList<>();
    private DropMenuAdapter typeMenuAdapter, dateMenuAdapter, letterMenuAdapter;
    private RefreshRecyclerView refreshView;
    private MusicListAdapter musicListAdapter;
    private MusicListPresenter musicPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_more);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        musicPresenter = new MusicListPresenter(this);
        initDropMenu();
    }

    private void initDropMenu() {

        View contentView = getLayoutInflater().inflate(R.layout.activity_music_content, null);
        musicListAdapter = new MusicListAdapter(this);
        refreshView = (RefreshRecyclerView) contentView.findViewById(R.id.refresh_view);
        refreshView.setRefreshListener(this);
        refreshView.setAdapter(musicListAdapter);

        dropDownMenu = (DropDownMenu) findViewById(R.id.drop_menu);
        String[] types = getResources().getStringArray(R.array.wikitype);
        String[] dates = DateUtils.getRecentYears();
        String[] alphabet = getResources().getStringArray(R.array.alphabet);
        final String[] headers = getResources().getStringArray(R.array.drop_header);
        typeMenuAdapter = new DropMenuAdapter(this, Arrays.asList(types));
        typeMenuAdapter.setItemClickListener(new DropMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String item) {
                String tab = position == 0 ? headers[0] : item;
                dropDownMenu.setTextAtPosition(0, tab);
                dropDownMenu.closeMenu();
                musicPresenter.setType(item);
                refreshView.startSwipeAfterViewCreate();
            }
        });
        dateMenuAdapter = new DropMenuAdapter(this, Arrays.asList(dates));
        dateMenuAdapter.setItemClickListener(new DropMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String item) {
                String tab = position == 0 ? headers[1] : item;
                dropDownMenu.setTabText(tab);
                dropDownMenu.closeMenu();
                musicPresenter.setDate(item);
                refreshView.startSwipeAfterViewCreate();
            }
        });
        letterMenuAdapter = new DropMenuAdapter(this, Arrays.asList(alphabet));
        letterMenuAdapter.setItemClickListener(new DropMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String item) {
                String tab = position == 0 ? headers[2] : item;
                dropDownMenu.setTabText(tab);
                dropDownMenu.closeMenu();
                musicPresenter.setInitial(item);
                refreshView.startSwipeAfterViewCreate();
            }
        });

        popupViews.add(getDropLayout(typeMenuAdapter));
        popupViews.add(getDropLayout(dateMenuAdapter));
        popupViews.add(getDropLayout(letterMenuAdapter));

        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);

        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("MusicType"))) {
            String type = getIntent().getStringExtra("MusicType");
            if (type.equals(WikiBean.WIKI_MUSIC)) {
                typeMenuAdapter.setSelectedPos(1);
            } else if (type.equals(WikiBean.WIKI_RADIO)) {
                typeMenuAdapter.setSelectedPos(2);
            }
        }
    }

    private View getDropLayout(DropMenuAdapter adapter) {
        View view = getLayoutInflater().inflate(R.layout.dropmenu_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setAdapter(adapter);
        return view;
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
    public void onSwipeRefresh() {
        musicPresenter.requestData();
    }

    @Override
    public void onLoadMore() {
        musicPresenter.loadMore();
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
