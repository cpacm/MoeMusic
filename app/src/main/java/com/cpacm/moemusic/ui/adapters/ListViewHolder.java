package com.cpacm.moemusic.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cpacm.moemusic.R;

/**
 * @Author: cpacm
 * @Date: 2016/7/10.
 * @description:
 */
public class ListViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public ListViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.content_text);
    }

}
