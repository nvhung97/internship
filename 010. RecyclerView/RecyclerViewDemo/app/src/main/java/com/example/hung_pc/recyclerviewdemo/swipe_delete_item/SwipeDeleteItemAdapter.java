package com.example.hung_pc.recyclerviewdemo.swipe_delete_item;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.hung_pc.recyclerviewdemo.R;
import java.util.List;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class SwipeDeleteItemAdapter extends RecyclerView.Adapter<SwipeDeleteItemAdapter.SwipeDeleteItemViewHolder> {

    private List<String> listItem;

    public class SwipeDeleteItemViewHolder extends RecyclerView.ViewHolder {
        public TextView     textView;
        public FrameLayout  background;
        public SwipeDeleteItemViewHolder(View itemView) {
            super(itemView);
            textView    = itemView.findViewById(R.id.txt_item);
            background  = itemView.findViewById(R.id.view_background);
        }
    }

    public SwipeDeleteItemAdapter(List<String> listItem) {
        this.listItem = listItem;
    }

    @Override
    public SwipeDeleteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SwipeDeleteItemViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_row, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(SwipeDeleteItemViewHolder holder, int position) {
        holder.textView.setText(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public void removeAt(int position) {
        listItem.remove(position);
        notifyDataSetChanged();
    }
}
