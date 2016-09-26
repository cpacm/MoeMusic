package com.cpacm.moemusic.ui.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.BaseFragment;

/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 精选集分类界面
 */
public class CollectFragment extends BaseFragment implements View.OnClickListener {

    public static final String TITLE = MoeApplication.getInstance().getString(R.string.collect);

    private View recentLayout, favLayout;
    private RecyclerView recyclerView;

    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();
        return fragment;
    }

    public CollectFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_collect, container, false);
        recentLayout = parentView.findViewById(R.id.recently_layout);
        recentLayout.setOnClickListener(this);
        favLayout = parentView.findViewById(R.id.favourite_layout);
        favLayout.setOnClickListener(this);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        return parentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recently_layout:
                RecentPlaylistActivity.open(getActivity());
                break;
            case R.id.favourite_layout:
                break;

        }
    }
}
