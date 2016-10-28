package com.cpacm.moemusic.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpacm.moemusic.ui.collection.CollectionFragment;
import com.cpacm.moemusic.ui.pixiv.PixivFragment;
import com.cpacm.moemusic.ui.music.AlbumFragment;
import com.cpacm.moemusic.ui.music.RadioFragment;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 主页viewpager适配器
 */
public class BeatsFragmentAdapter extends FragmentPagerAdapter {

    private CollectionFragment collectionFragment;
    private PixivFragment pixivFragment;
    private AlbumFragment albumFragment;
    private RadioFragment radioFragment;

    private final String[] titles;

    public BeatsFragmentAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{CollectionFragment.TITLE, AlbumFragment.TITLE, RadioFragment.TITLE, PixivFragment.TITLE};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (collectionFragment == null)
                    collectionFragment = CollectionFragment.newInstance();
                return collectionFragment;
            case 1:
                if (albumFragment == null)
                    albumFragment = AlbumFragment.newInstance();
                return albumFragment;
            case 2:
                if (radioFragment == null)
                    radioFragment = RadioFragment.newInstance();
                return radioFragment;
            case 3:
                if (pixivFragment == null)
                    pixivFragment = PixivFragment.newInstance();
                return pixivFragment;
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
