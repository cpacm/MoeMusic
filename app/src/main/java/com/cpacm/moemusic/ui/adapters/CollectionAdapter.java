package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.moemusic.R;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/27
 * @desciption: 收藏夹适配器
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private List<CollectionBean> collectionList;
    private Context context;

    public CollectionAdapter(Context context) {
        this.context = context;
        update();
    }

    public void update() {
        collectionList = CollectionManager.getInstance().getCollectionList();
        collectionList.add(0, createDefault());
        notifyDataSetChanged();
    }

    private CollectionBean createDefault() {
        CollectionBean bean = new CollectionBean();
        bean.setId(-1);
        return bean;
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_collection_listitem, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        CollectionBean bean = collectionList.get(position);
        if (bean.getId() == -1) {
            holder.setting.setVisibility(View.GONE);
            holder.count.setVisibility(View.GONE);
            holder.title.setText(context.getString(R.string.collection_create));
            holder.cover.setImageResource(R.drawable.create_collection);
        } else {
            holder.setting.setVisibility(View.VISIBLE);
            holder.count.setVisibility(View.VISIBLE);
            holder.title.setText(bean.getTitle());
            Glide.with(context)
                    .load(bean.getCoverUrl())
                    .placeholder(R.drawable.collection_cover)
                    .into(holder.cover);
        }
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        private View collectLayout;
        private ImageView cover;
        private TextView title, count;
        private AppCompatImageView setting;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            collectLayout = itemView.findViewById(R.id.collection_item);
            cover = (ImageView) itemView.findViewById(R.id.collection_cover);
            count = (TextView) itemView.findViewById(R.id.collection_count);
            title = (TextView) itemView.findViewById(R.id.collection_title);
            setting = (AppCompatImageView) itemView.findViewById(R.id.collection_setting);
        }
    }
}
