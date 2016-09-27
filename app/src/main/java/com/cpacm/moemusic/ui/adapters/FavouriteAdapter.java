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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.music.MusicPlayActivity;
import com.cpacm.moemusic.utils.TransitionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/9/27
 * @desciption: 收藏适配器，可以分为全部，专辑，电台三个模式显示
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.CardViewHolder> {

    public static final int ALL_MODE = 0;
    public static final int ALBUM_MODE = 1;
    public static final int RADIO_MODE = 2;

    private List<WikiBean> favWikis;
    private List<WikiBean> albumWikis;
    private List<WikiBean> radioWikis;
    private int mode = ALL_MODE;
    private Context context;

    public FavouriteAdapter(Context context) {
        this.context = context;
        favWikis = new ArrayList<>();
        albumWikis = new ArrayList<>();
        radioWikis = new ArrayList<>();
    }

    private void updateWikis() {
        albumWikis.clear();
        radioWikis.clear();
        for (WikiBean wikiBean : favWikis) {
            if (wikiBean.getWiki_type().equals(WikiBean.WIKI_MUSIC)) {
                albumWikis.add(wikiBean);
            }
            if (wikiBean.getWiki_type().equals(WikiBean.WIKI_RADIO)) {
                radioWikis.add(wikiBean);
            }
        }
    }

    public void setData(List<WikiBean> newList) {
        if (newList != null) {
            this.favWikis = newList;
            updateWikis();
            notifyDataSetChanged();
        }
    }

    public void addData(List<WikiBean> addList) {
        if (addList != null) {
            favWikis.addAll(addList);
            updateWikis();
            notifyDataSetChanged();
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_favourite_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        final WikiBean wiki = getWikiFromMode(position);
        if (wiki == null) return;
        Glide.with(context)
                .load(wiki.getWiki_cover().getLarge())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
        holder.title.setText(Html.fromHtml(wiki.getWiki_title()));
        holder.type.setText(wiki.getDetail());
        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    MusicPlayActivity.open(context, wiki);
                    return;
                }
                Intent intent = MusicPlayActivity.getIntent(context, wiki);
                final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants((Activity) context, false,
                        new Pair<>(holder.cover, context.getString(R.string.music_share_cover)));
                TransitionHelper.startSharedElementActivity((Activity) context, intent, pairs);
            }
        });
    }

    private WikiBean getWikiFromMode(int position) {
        switch (mode) {
            case ALL_MODE:
                return favWikis.get(position);
            case ALBUM_MODE:
                if (position >= 0 && position < albumWikis.size()) {
                    return albumWikis.get(position);
                } else return null;
            case RADIO_MODE:
                if (position >= 0 && position < radioWikis.size()) {
                    return radioWikis.get(position);
                } else return null;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        switch (mode) {
            case ALL_MODE:
                return favWikis.size();
            case ALBUM_MODE:
                return albumWikis.size();
            case RADIO_MODE:
                return radioWikis.size();
        }
        return 0;
    }

    public void setMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private View cardLayout;
        private ImageView cover;
        private TextView title, type;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardLayout = itemView.findViewById(R.id.card_view);
            cover = (ImageView) itemView.findViewById(R.id.card_cover);
            title = (TextView) itemView.findViewById(R.id.card_title);
            type = (TextView) itemView.findViewById(R.id.card_type);
        }
    }
}
