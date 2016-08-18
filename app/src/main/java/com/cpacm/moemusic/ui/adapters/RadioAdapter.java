package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.moemusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/17
 * @desciption:
 */
public class RadioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int RADIO_ITEM = 100;
    public final static int RADIO_TYPE_HOT = 101;
    public final static int RADIO_TYPE_NEW = 102;

    private Context context;
    private List<WikiBean> hotRadios;
    private List<WikiBean> newRadios;


    public RadioAdapter(Context context) {
        this.context = context;
    }

    public void setHotRadios(List<WikiBean> radios) {
        this.hotRadios = radios;
        notifyDataSetChanged();
    }

    public void setNewRadios(List<WikiBean> radios) {
        this.newRadios = radios;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RADIO_TYPE_HOT || viewType == RADIO_TYPE_NEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_type, parent, false);
            return new TypeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_radio_item, parent, false);
            return new RadioViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == RADIO_TYPE_HOT && holder instanceof TypeViewHolder) {
            TypeViewHolder newTypeHolder = (TypeViewHolder) holder;
            newTypeHolder.albumType.setText(context.getString(R.string.radio_hot_title));
            newTypeHolder.albumSubtype.setText(context.getString(R.string.radio_hot_subtitle));
        } else if (getItemViewType(position) == RADIO_TYPE_NEW && holder instanceof TypeViewHolder) {
            TypeViewHolder newTypeHolder = (TypeViewHolder) holder;
            newTypeHolder.albumType.setText(context.getString(R.string.radio_new_title));
            newTypeHolder.albumSubtype.setText(context.getString(R.string.radio_new_subtitle));
        } else {
            RadioViewHolder radioViewHolder = (RadioViewHolder) holder;
            WikiBean wiki;
            if (position < getHotCount()) {
                wiki = hotRadios.get(position - 1);
            } else {
                wiki = newRadios.get(position - getHotCount() - 1);
            }
            Glide.with(context)
                    .load(wiki.getWiki_cover().getMedium())
                    .placeholder(R.drawable.cover)
                    .into(radioViewHolder.cover);
            radioViewHolder.title.setText(Html.fromHtml(wiki.getWiki_title()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return RADIO_TYPE_HOT;
        }
        if (position == getHotCount()) {
            return RADIO_TYPE_NEW;
        }
        return RADIO_ITEM;
    }

    @Override
    public int getItemCount() {
        return getNewCount() + getHotCount();
    }

    public int getNewCount() {
        return newRadios == null ? 0 : newRadios.size() + 1;
    }

    public int getHotCount() {
        return hotRadios == null ? 0 : hotRadios.size() + 1;
    }

    public class RadioViewHolder extends RecyclerView.ViewHolder {

        private ImageView cover;
        private TextView title;

        public RadioViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.radio_cover);
            title = (TextView) itemView.findViewById(R.id.radio_title);
        }
    }

    public class TypeViewHolder extends RecyclerView.ViewHolder {

        public TextView albumType, albumSubtype;
        public Button moreBtn;

        public TypeViewHolder(View itemView) {
            super(itemView);
            albumType = (TextView) itemView.findViewById(R.id.type_title);
            albumSubtype = (TextView) itemView.findViewById(R.id.type_subtitle);
            moreBtn = (Button) itemView.findViewById(R.id.more);
        }
    }
}
