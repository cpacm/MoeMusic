package com.cpacm.moemusic.ui.beats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.DownloadAdapter;

/**
 * @author: cpacm
 * @date: 2016/9/12
 * @desciption: 下载管理界面
 */
public class DownloadActivity extends AbstractAppActivity {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DownloadActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DownloadAdapter downloadAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
    }

    private void initRecyclerView() {
        downloadAdapter = new DownloadAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(downloadAdapter);
        recyclerView.setItemAnimator(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadAdapter.onDestroy();
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
