package com.cpacm.moemusic.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpacm.moemusic.ui.beats.DownloadedFragment;
import com.cpacm.moemusic.ui.beats.DownloadingFragment;

/**
 * @author: cpacm
 * @date: 2016/9/20
 * @desciption: 下载管理fragment适配器
 */

public class DownloadFragmentAdapter extends FragmentPagerAdapter {

    private DownloadingFragment downloadingFragment;
    private DownloadedFragment downloadedFragment;

    private final String[] titles;

    public DownloadFragmentAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{DownloadingFragment.TITLE, DownloadedFragment.TITLE};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (downloadingFragment == null)
                    downloadingFragment = DownloadingFragment.newInstance();
                return downloadingFragment;
            case 1:
                if (downloadedFragment == null)
                    downloadedFragment = DownloadedFragment.newInstance();
                return downloadedFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
