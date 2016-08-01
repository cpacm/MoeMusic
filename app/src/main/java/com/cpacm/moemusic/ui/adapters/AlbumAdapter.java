package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.utils.DateUtils;
import com.cpacm.moemusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/7/10.
 * @description: 歌单适配器
 */
public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int ALBUM_TYPE_NEW = 1000;
    public final static int ALBUM_TYPE_HOT = 1001;
    public final static int ALBUM_MUSIC_NEW = 1002;
    public final static int ALBUM_MUSIC_HOT = 1003;

    private Context context;

    private AlbumListener albumListener;
    private List<WikiBean> newMusics;
    private List<WikiBean> hotMusics;

    public AlbumAdapter(Context context) {
        this.context = context;
        this.newMusics = new ArrayList<>();
        this.hotMusics = new ArrayList<>();
    }

    public List<WikiBean> getNewMusics() {
        return newMusics;
    }

    public void setNewMusics(List<WikiBean> newMusics) {
        this.newMusics = newMusics;
        notifyDataSetChanged();
    }

    public List<WikiBean> getHotMusics() {
        return hotMusics;
    }

    public void setHotMusics(List<WikiBean> hotMusics) {
        this.hotMusics = hotMusics;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ALBUM_TYPE_NEW || viewType == ALBUM_TYPE_HOT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_album_type, parent, false);
            return new AlbumTypeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_album_card, parent, false);
            return new AlbumCardViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ALBUM_TYPE_NEW && holder instanceof AlbumTypeViewHolder) {
            AlbumTypeViewHolder newTypeHolder = (AlbumTypeViewHolder) holder;
            newTypeHolder.albumType.setText(context.getString(R.string.album_new_title));
            newTypeHolder.albumSubtype.setText(context.getString(R.string.album_new_subtitle));
        } else if (getItemViewType(position) == ALBUM_TYPE_HOT && holder instanceof AlbumTypeViewHolder) {
            AlbumTypeViewHolder newTypeHolder = (AlbumTypeViewHolder) holder;
            newTypeHolder.albumType.setText(context.getString(R.string.album_hot_title));
            newTypeHolder.albumSubtype.setText(context.getString(R.string.album_hot_subtitle));
        } else if (getItemViewType(position) == ALBUM_MUSIC_NEW && holder instanceof AlbumCardViewHolder) {
            AlbumCardViewHolder cardHolder = (AlbumCardViewHolder) holder;
            WikiBean wiki = newMusics.get(position - 1);
            Glide.with(context)
                    .load(wiki.getWiki_cover().getMedium())
                    .placeholder(R.drawable.cover)
                    .into(cardHolder.albumCover);
            cardHolder.albumTitle.setText(wiki.getWiki_title());
            cardHolder.albumDate.setText(DateUtils.convertTimeToFormat(wiki.getWiki_date()));
            cardHolder.favLayout.setVisibility(View.GONE);
            cardHolder.subLayout.setVisibility(View.VISIBLE);
            cardHolder.subCount.setText(wiki.getWiki_sub_count());
        } else if (getItemViewType(position) == ALBUM_MUSIC_HOT && holder instanceof AlbumCardViewHolder) {
            AlbumCardViewHolder cardHolder = (AlbumCardViewHolder) holder;
            WikiBean wiki = hotMusics.get(position - newMusics.size() - 2);
            Glide.with(context)
                    .load(wiki.getWiki_cover().getMedium())
                    .placeholder(R.drawable.cover)
                    .into(cardHolder.albumCover);
            cardHolder.albumTitle.setText(wiki.getWiki_title());
            cardHolder.albumDate.setText(DateUtils.convertTimeToFormat(wiki.getWiki_date()));
            cardHolder.subLayout.setVisibility(View.GONE);
            cardHolder.favLayout.setVisibility(View.VISIBLE);
            cardHolder.favCount.setText(wiki.getFav_count());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ALBUM_TYPE_NEW;
        }
        if (position > 0 && position <= newMusics.size()) {
            return ALBUM_MUSIC_NEW;
        }
        if (position == newMusics.size() + 1) {
            return ALBUM_TYPE_HOT;
        }
        if (position > newMusics.size() + 1 && position < getItemCount()) {
            return ALBUM_MUSIC_HOT;
        }
        return super.getItemViewType(position);
    }

    public void setAlbumListener(AlbumListener albumListener) {
        this.albumListener = albumListener;
    }

    @Override
    public int getItemCount() {
        int newCount = newMusics == null ? 0 : newMusics.size() + 1;
        int hotCount = hotMusics == null ? 0 : hotMusics.size() + 1;
        return newCount + hotCount;
    }

    public interface AlbumListener {
        void onAlbumClick(String s);
    }

    public class AlbumCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumCover;
        private TextView albumTitle, albumDate;
        private TextView subCount, favCount;
        private View favLayout, subLayout;

        public AlbumCardViewHolder(View itemView) {
            super(itemView);
            albumCover = (ImageView) itemView.findViewById(R.id.album_cover);
            albumTitle = (TextView) itemView.findViewById(R.id.album_title);
            albumDate = (TextView) itemView.findViewById(R.id.album_date);

            subLayout = itemView.findViewById(R.id.album_sub_count_layout);
            subCount = (TextView) itemView.findViewById(R.id.album_sub_count);
            favLayout = itemView.findViewById(R.id.album_fav_layout);
            favCount = (TextView) itemView.findViewById(R.id.album_fav);
        }
    }

    public class AlbumTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView albumType, albumSubtype;
        private Button moreBtn;

        public AlbumTypeViewHolder(View itemView) {
            super(itemView);
            albumType = (TextView) itemView.findViewById(R.id.album_type_title);
            albumSubtype = (TextView) itemView.findViewById(R.id.album_type_subtitle);
            moreBtn = (Button) itemView.findViewById(R.id.album_more);
        }
    }
}
