package com.cpacm.moemusic.ui.comic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 漫画分类fragment
 */
public class ComicFragment extends BaseFragment{

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.comic);

    public static ComicFragment newInstance() {
        ComicFragment fragment = new ComicFragment();
        return fragment;
    }

    public ComicFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_comic, container, false);
        return parentView;
    }

}
