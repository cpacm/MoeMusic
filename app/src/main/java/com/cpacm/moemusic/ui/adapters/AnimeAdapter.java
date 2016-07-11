package com.cpacm.moemusic.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: cpacm
 * @date: 2016/7/11
 * @desciption: 动画页面数据适配器
 */
public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder {

        public AnimeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
