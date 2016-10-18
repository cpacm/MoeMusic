package com.cpacm.moemusic.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.util.Pair;
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
import com.cpacm.moemusic.ui.music.MoeDetailActivity;
import com.cpacm.moemusic.ui.music.MusicListActivity;
import com.cpacm.moemusic.utils.TransitionHelper;

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
            final RadioViewHolder radioViewHolder = (RadioViewHolder) holder;
            final WikiBean wiki;
            if (position < getHotCount()) {
                wiki = hotRadios.get(position - 1);
            } else {
                wiki = newRadios.get(position - getHotCount() - 1);
            }
            Glide.with(context)
                    .load(wiki.getWiki_cover().getLarge())
                    .placeholder(R.drawable.cover)
                    .into(radioViewHolder.cover);
            radioViewHolder.title.setText(Html.fromHtml(wiki.getWiki_title()));
            radioViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        MoeDetailActivity.open(context, wiki);
                        return;
                    }
                    Intent intent = MoeDetailActivity.getIntent(context, wiki);
                    final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants((Activity) context, false,
                            new Pair<>(radioViewHolder.cover, context.getString(R.string.music_share_cover)));
                    TransitionHelper.startSharedElementActivity((Activity) context, intent, pairs);
                }
            });
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

        public View layout;
        public ImageView cover;
        public TextView title;

        public RadioViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.radio_cover);
            title = (TextView) itemView.findViewById(R.id.radio_title);
            layout = itemView.findViewById(R.id.radio_layout);
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
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicListActivity.open(context, WikiBean.WIKI_RADIO);
                }
            });
        }
    }
}
