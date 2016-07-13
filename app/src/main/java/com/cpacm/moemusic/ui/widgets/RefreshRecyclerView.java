package com.cpacm.moemusic.ui.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cpacm.moemusic.R;

/**
 * @author: cpacm
 * @date: 2016/7/11
 * @desciption: 可上拉下拉加载的RecycleView
 */
public class RefreshRecyclerView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener{

    private Context mContext;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View headerView;
    private View loadView;
    private int loadMorePosition;
    private boolean isLoadingMore = false;
    private boolean isLoadEnable = true;
    private RefreshListener refreshListener;
    private RefreshRecycleAdapter refreshRecycleAdapter;
    private boolean isHeaderEnable;

    public RefreshRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        isHeaderEnable = false;
        View parentView = LayoutInflater.from(context).inflate(R.layout.refresh_recycleview_layout, null, false);
        swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycle_view);
        setLayoutManager(new LinearLayoutManager(context));
        if (loadView == null) {
            loadView = LayoutInflater.from(context).inflate(R.layout.refresh_loadmore_layout, this, false);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != refreshListener && isLoadEnable && !isLoadingMore && dy > 0) {
                    int lastVisiblePosition = getLastPosition();
                    if (lastVisiblePosition + 1 == refreshRecycleAdapter.getItemCount()) {
                        setLoadingMore(true);
                        refreshListener.onLoadMore();
                    }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        addView(parentView);
    }

    @Override
    public void onRefresh() {
        if (null != refreshListener)
            refreshListener.onSwipeRefresh();
    }

    public void startSwipeAfterViewCreate() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (refreshRecycleAdapter == null) {
            refreshRecycleAdapter = new RefreshRecycleAdapter(adapter);
            recyclerView.setAdapter(refreshRecycleAdapter);
        } else
            refreshRecycleAdapter.setInternalAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (refreshRecycleAdapter.getItemViewType(position)) {
                        case RefreshRecycleAdapter.HEADER:
                            return gridLayoutManager.getSpanCount();
                        case RefreshRecycleAdapter.LOADMORE:
                            return gridLayoutManager.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    public int getLastPosition() {
        int lastPosition = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPoasitions = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            lastPosition = getMaxPosition(lastPoasitions);
        } else {
            lastPosition = layoutManager.getItemCount() - 1;
        }
        loadMorePosition = lastPosition;
        return lastPosition;
    }

    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    public void setLoadingMore(boolean loadingMore) {
        isLoadingMore = loadingMore;
    }

    public void notifyLoadMoreFinish(boolean hasMore) {
        setLoadEnable(hasMore);
        refreshRecycleAdapter.notifyDataSetChanged();
        refreshRecycleAdapter.notifyItemRemoved(loadMorePosition);
        isLoadingMore = false;
    }

    public void notifySwipeFinish() {
        refreshRecycleAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setLoadEnable(boolean loadEnable) {
        isLoadEnable = loadEnable;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        setHeaderEnable(true);
    }

    public void setHeaderView(int resId) {
        headerView = LayoutInflater.from(mContext).inflate(resId, null);
        setHeaderEnable(true);
    }

    public void setHeaderEnable(boolean headerEnable) {
        this.isHeaderEnable = headerEnable;
    }

    public interface RefreshListener {
        void onSwipeRefresh();

        void onLoadMore();
    }

    public class RefreshRecycleAdapter extends RecyclerView.Adapter {

        public final static int HEADER = 0;
        public final static int LOADMORE = 1;
        public final static int NORMAL = 2;

        private RecyclerView.Adapter internalAdapter;

        public RefreshRecycleAdapter(RecyclerView.Adapter internalAdapter) {
            this.internalAdapter = internalAdapter;
            isHeaderEnable = false;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) return new HeaderViewHolder(headerView);
            if (viewType == LOADMORE) return new LoadMoreViewHolder(loadView);
            return internalAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder || holder instanceof LoadMoreViewHolder)
                return;
            if (isHeaderEnable)
                internalAdapter.onBindViewHolder(holder, position - 1);
            else internalAdapter.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 && isHeaderEnable && headerView != null)
                return HEADER;
            if (position == getItemCount() - 1 && isLoadEnable)
                return LOADMORE;
            return NORMAL;
        }

        @Override
        public int getItemCount() {
            int count = internalAdapter.getItemCount();
            if (isHeaderEnable) count++;
            if (isLoadEnable) count++;
            return count;
        }

        public RecyclerView.Adapter getInternalAdapter() {
            return internalAdapter;
        }

        public void setInternalAdapter(RecyclerView.Adapter internalAdapter) {
            this.internalAdapter = internalAdapter;
        }

        public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
            public LoadMoreViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}
