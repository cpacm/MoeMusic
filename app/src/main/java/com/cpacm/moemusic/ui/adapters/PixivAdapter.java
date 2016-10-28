package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.bean.PixivBean;
import com.cpacm.moemusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: cpacm
 * @Date: 2016/10/28.
 * @description:
 */

public class PixivAdapter extends RecyclerView.Adapter<PixivAdapter.PixivViewHolder> {

    private List<PixivBean> pixivBeen;
    private SparseIntArray heightMap;
    private Context context;

    public PixivAdapter(Context context) {
        this.context = context;
        pixivBeen = new ArrayList<>();
        heightMap = new SparseIntArray();
    }

    public void setData(List<PixivBean> been) {
        this.pixivBeen = been;
        heightMap.clear();
        notifyDataSetChanged();
    }

    @Override
    public PixivViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_pixiv_item, parent, false);
        return new PixivViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PixivViewHolder holder, final int position) {
        PixivBean pixivBean = pixivBeen.get(position);
        if (heightMap.get(position) != 0) {
            int value = heightMap.get(position);
            holder.setImageHeight(value);
        }
        Glide.with(context)
                .load(pixivBean.getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.imageView.setImageBitmap(resource);
                        float rate = holder.imageView.getLayoutParams().width *1.0f / resource.getWidth();
                        int height = (int)(resource.getHeight() * rate);
                        heightMap.put(position, height);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return pixivBeen.size();
    }

    public class PixivViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PixivViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.pixiv_img);
        }

        public void setImageHeight(int height) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = height;
            imageView.setLayoutParams(layoutParams);
        }
    }
}
