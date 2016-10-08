package com.cpacm.moemusic.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpacm.moemusic.ui.beats.LocalAlbumFragment;
import com.cpacm.moemusic.ui.beats.LocalArtistFragment;
import com.cpacm.moemusic.ui.beats.LocalMusicFragment;

/**
 * @Author: cpacm
 * @Date: 2016/9/21.
 * @description: 本地歌曲fragment适配器
 */

public class LocalFragmentAdapter extends FragmentPagerAdapter {

    private LocalMusicFragment localMusicFragment;
    private LocalAlbumFragment localAlbumFragment;
    private LocalArtistFragment localArtistFragment;

    private final String[] titles;

    public LocalFragmentAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{LocalMusicFragment.TITLE, LocalAlbumFragment.TITLE, LocalArtistFragment.TITLE};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (localMusicFragment == null) {
                    localMusicFragment = LocalMusicFragment.newInstance();
                }
                return localMusicFragment;
            case 1:
                if (localAlbumFragment == null) {
                    localAlbumFragment = LocalAlbumFragment.newInstance();
                }
                return localAlbumFragment;
            case 2:
                if (localArtistFragment == null) {
                    localArtistFragment = LocalArtistFragment.newInstance();
                }
                return localArtistFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length - 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
