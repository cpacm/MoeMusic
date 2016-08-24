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
 * @date: 2016/8/24
 * @desciption: 专题列表
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ListViewHolder> {

    private List<WikiBean> wikis;
    private Context context;

    public MusicListAdapter(Context context) {
        this.context = context;
        wikis = new ArrayList<>();
    }

    public void setData(List<WikiBean> wikis) {
        this.wikis = wikis;
        notifyDataSetChanged();
    }

    public void addData(List<WikiBean> wikiBeanList){
        this.wikis.addAll(wikiBeanList);
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_music_content_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final WikiBean wiki = wikis.get(position);
        holder.title.setText(Html.fromHtml(wiki.getWiki_title()));
        holder.detail.setText(wiki.getDetail());
        if (wiki.getWikiDescription() == null) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(wiki.getWikiDescription());
        }
        Glide.with(context)
                .load(wiki.getWiki_cover().getLarge())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
        holder.layout.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return wikis.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public ImageView cover;
        public TextView title, description, detail;

        public ListViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.music_layout);
            cover = (ImageView) itemView.findViewById(R.id.music_cover);
            title = (TextView) itemView.findViewById(R.id.music_title);
            description = (TextView) itemView.findViewById(R.id.music_description);
            detail = (TextView) itemView.findViewById(R.id.music_detail);
        }
    }
}
