package com.cpacm.moemusic.ui.area;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.core.Pixiv.PixivGrab;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 分区界面
 */
public class AreaFragment extends BaseFragment {
    public static final String TITLE = MoeApplication.getInstance().getString(R.string.area);

    public static AreaFragment newInstance() {
        AreaFragment fragment = new AreaFragment();
        return fragment;
    }

    private PixivGrab pixivGrab;

    public AreaFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pixivGrab = new PixivGrab();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_area, container, false);
        parentView.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pixivGrab.login("cpacm", "8507721013");
            }
        });
        return parentView;
    }
}
