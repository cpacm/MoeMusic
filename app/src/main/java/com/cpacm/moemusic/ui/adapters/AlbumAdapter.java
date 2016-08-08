package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.utils.BitmapUtils;
import com.cpacm.core.utils.DateUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.album.MusicPlayActivity;

import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/7/10.
 * @description: 歌单适配器
 */
public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int ALBUM_TYPE_NEW = 1000;
    public final static int ALBUM_TYPE_HOT = 1001;
    public final static int ALBUM_MUSIC_LEFT = 1002;
    public final static int ALBUM_MUSIC_RIGHT = 1003;

    private Context context;

    private AlbumListener albumListener;
    private List<WikiBean> newMusics;
    private List<WikiBean> hotMusics;

    public AlbumAdapter(Context context) {
        this.context = context;
    }

    public List<WikiBean> getNewMusics() {
        return newMusics;
    }

    public void setNewMusics(List<WikiBean> newMusics) {
        this.newMusics = newMusics;
    }

    public List<WikiBean> getHotMusics() {
        return hotMusics;
    }

    public void setHotMusics(List<WikiBean> hotMusics) {
        this.hotMusics = hotMusics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ALBUM_TYPE_NEW || viewType == ALBUM_TYPE_HOT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_album_type, parent, false);
            return new AlbumTypeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_album_card, parent, false);
            if (viewType == ALBUM_MUSIC_LEFT)
                return new AlbumLeftCardViewHolder(view);
            else return new AlbumRightCardViewHolder(view);
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
        } else {
            BaseAlbumCardViewHolder cardHolder;
            WikiBean wiki;
            if (holder instanceof AlbumLeftCardViewHolder) {
                cardHolder = (AlbumLeftCardViewHolder) holder;
            } else {
                cardHolder = (AlbumRightCardViewHolder) holder;
            }
            if (position > newMusics.size() + 1) {
                wiki = hotMusics.get(position - newMusics.size() - 2);
                Glide.with(context)
                        .load(wiki.getWiki_cover().getLarge())
                        .placeholder(R.drawable.cover)
                        .into(cardHolder.albumCover);
                cardHolder.albumTitle.setText(wiki.getWiki_title());
                cardHolder.albumDate.setText(DateUtils.convertTimeToFormat(wiki.getWiki_date()));
                cardHolder.subLayout.setVisibility(View.GONE);
                cardHolder.favLayout.setVisibility(View.VISIBLE);
                cardHolder.favCount.setText(wiki.getWiki_fav_count() + "");
            } else {
                wiki = newMusics.get(position - 1);
                Glide.with(context)
                        .load(wiki.getWiki_cover().getLarge())
                        .placeholder(R.drawable.cover)
                        .into(cardHolder.albumCover);
                cardHolder.albumTitle.setText(wiki.getWiki_title());
                cardHolder.albumDate.setText(DateUtils.convertTimeToFormat(wiki.getWiki_date()));
                cardHolder.favLayout.setVisibility(View.GONE);
                cardHolder.subLayout.setVisibility(View.VISIBLE);
                cardHolder.subCount.setText(wiki.getWiki_sub_count() + "");
            }
            cardHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayActivity.open(context);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ALBUM_TYPE_NEW;
        } else if (position == newMusics.size() + 1) {
            return ALBUM_TYPE_HOT;
        } else {
            if (position > 0 && position <= newMusics.size()) {
                if ((position - 1) % 2 == 0)
                    return ALBUM_MUSIC_LEFT;
                else return ALBUM_MUSIC_RIGHT;
            }

            if (position > newMusics.size() + 1 && position < getItemCount()) {
                if ((position - newMusics.size() - 2) % 2 == 0)
                    return ALBUM_MUSIC_LEFT;
                else return ALBUM_MUSIC_RIGHT;
            }
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

    public class BaseAlbumCardViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView albumCover;
        public TextView albumTitle, albumDate;
        public TextView subCount, favCount;
        public View favLayout, subLayout;

        public BaseAlbumCardViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            albumCover = (ImageView) itemView.findViewById(R.id.album_cover);
            albumTitle = (TextView) itemView.findViewById(R.id.album_title);
            albumDate = (TextView) itemView.findViewById(R.id.album_date);

            subLayout = itemView.findViewById(R.id.album_sub_count_layout);
            subCount = (TextView) itemView.findViewById(R.id.album_sub_count);
            favLayout = itemView.findViewById(R.id.album_fav_layout);
            favCount = (TextView) itemView.findViewById(R.id.album_fav);
        }
    }

    public class AlbumLeftCardViewHolder extends BaseAlbumCardViewHolder {

        public AlbumLeftCardViewHolder(View itemView) {
            super(itemView);
            restoreLeftMargin();
        }

        public void restoreLeftMargin() {
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) cardView.getLayoutParams();
            params.leftMargin = BitmapUtils.dp2px(16);
            params.rightMargin = BitmapUtils.dp2px(8);
            cardView.setLayoutParams(params);
        }
    }

    public class AlbumRightCardViewHolder extends BaseAlbumCardViewHolder {

        public AlbumRightCardViewHolder(View itemView) {
            super(itemView);
            restoreLeftMargin();
        }

        public void restoreLeftMargin() {
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) cardView.getLayoutParams();
            params.rightMargin = BitmapUtils.dp2px(16);
            params.leftMargin = BitmapUtils.dp2px(8);
            cardView.setLayoutParams(params);
        }
    }

    public class AlbumTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView albumType, albumSubtype;
        public Button moreBtn;

        public AlbumTypeViewHolder(View itemView) {
            super(itemView);
            albumType = (TextView) itemView.findViewById(R.id.album_type_title);
            albumSubtype = (TextView) itemView.findViewById(R.id.album_type_subtitle);
            moreBtn = (Button) itemView.findViewById(R.id.album_more);
        }
    }
}
