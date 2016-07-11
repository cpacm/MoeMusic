package com.cpacm.moemusic.ui.widgets;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: cpacm
 * @date: 2016/7/11
 * @desciption:
 */
public abstract class RefreshRecycleAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private View headerView;
    private View moreView;

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int count = getDataCount();
        if (headerView != null)
            count++;
        if (moreView != null)
            count++;
        return count;
    }

    public abstract int getDataCount();


    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public void setMoreView(View moreView) {
        this.moreView = moreView;
    }
}
