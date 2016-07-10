package com.cpacm.moemusic.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpacm.moemusic.ui.BaseFragment;
import com.cpacm.moemusic.ui.air.AirFragment;
import com.cpacm.moemusic.ui.anime.AnimeFragment;
import com.cpacm.moemusic.ui.area.AreaFragment;
import com.cpacm.moemusic.ui.comic.ComicFragment;
import com.cpacm.moemusic.ui.music.MusicFragment;
import com.cpacm.moemusic.ui.radio.RadioFragment;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 主页viewpager适配器
 */
public class BeatsFragmentAdapter extends FragmentPagerAdapter {

    private AirFragment airFragment;
    private AnimeFragment animeFragment;
    private AreaFragment areaFragment;
    private ComicFragment comicFragment;
    private MusicFragment musicFragment;
    private RadioFragment radioFragment;

    private final String[] titles;

    public BeatsFragmentAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{MusicFragment.TITLE, AnimeFragment.TITLE, ComicFragment.TITLE, RadioFragment.TITLE, AreaFragment.TITLE, AirFragment.TITLE};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (musicFragment == null)
                    musicFragment = MusicFragment.newInstance();
                return musicFragment;
            case 1:
                if (animeFragment == null)
                    animeFragment = AnimeFragment.newInstance();
                return animeFragment;
            case 2:
                if (comicFragment == null)
                    comicFragment = ComicFragment.newInstance();
                return comicFragment;
            case 3:
                if (radioFragment == null)
                    radioFragment = RadioFragment.newInstance();
                return radioFragment;
            case 4:
                if (areaFragment == null)
                    areaFragment = AreaFragment.newInstance();
                return areaFragment;
            case 5:
                if (airFragment == null)
                    airFragment = AirFragment.newInstance();
                return airFragment;
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
