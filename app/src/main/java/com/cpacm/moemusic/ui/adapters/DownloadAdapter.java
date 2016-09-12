package com.cpacm.moemusic.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: cpacm
 * @date: 2016/9/12
 * @desciption: 下载管理适配器
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {

        public DownloadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
