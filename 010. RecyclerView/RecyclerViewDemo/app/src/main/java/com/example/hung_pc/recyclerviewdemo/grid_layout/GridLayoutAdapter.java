package com.example.hung_pc.recyclerviewdemo.grid_layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hung_pc.recyclerviewdemo.R;
import java.util.List;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class GridLayoutAdapter extends RecyclerView.Adapter<GridLayoutAdapter.GridLayoutViewHolder> {

    private List<String> listItem;

    public class GridLayoutViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public GridLayoutViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_item);
        }
    }

    public GridLayoutAdapter(List<String> listItem) {
        this.listItem = listItem;
    }

    @Override
    public GridLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridLayoutViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_row, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(GridLayoutViewHolder holder, int position) {
        holder.textView.setText(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
