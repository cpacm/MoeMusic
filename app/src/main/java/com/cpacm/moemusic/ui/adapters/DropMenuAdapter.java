package com.cpacm.moemusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpacm.moemusic.R;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption:
 */
public class DropMenuAdapter extends RecyclerView.Adapter<DropMenuAdapter.DropViewHolder> {

    private List<String> items;
    private Context context;
    private OnItemClickListener itemClickListener;
    private int selectedPos = 0;

    public DropMenuAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public DropViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_drop_item, parent, false);
        return new DropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DropViewHolder holder, final int position) {
        holder.text.setText(items.get(position));
        if (position == selectedPos) {
            holder.text.setTextColor(context.getResources().getColor(R.color.white));
            holder.text.setBackgroundResource(R.drawable.background_theme_radius);
        } else {
            holder.text.setTextColor(context.getResources().getColor(R.color.black_normal));
            holder.text.setBackgroundDrawable(null);
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = position;
                notifyDataSetChanged();
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(position, items.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
        if (itemClickListener != null) {
            itemClickListener.onItemClickListener(selectedPos, items.get(selectedPos));
        }
    }

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class DropViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public DropViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.drop_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, String item);
    }
}
