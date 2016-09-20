package com.cpacm.moemusic.ui.beats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.DownloadAdapter;
import com.cpacm.moemusic.ui.adapters.DownloadFragmentAdapter;

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
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DownloadFragmentAdapter downloadFragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        downloadFragmentAdapter = new DownloadFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(downloadFragmentAdapter);
        viewPager.setOffscreenPageLimit(downloadFragmentAdapter.getCount());
        tabLayout.setupWithViewPager(viewPager);
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
