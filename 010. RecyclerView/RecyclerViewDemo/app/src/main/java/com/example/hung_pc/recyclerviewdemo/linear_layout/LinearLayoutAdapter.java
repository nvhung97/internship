package com.example.hung_pc.recyclerviewdemo.linear_layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hung_pc.recyclerviewdemo.R;
import java.util.List;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class LinearLayoutAdapter extends RecyclerView.Adapter<LinearLayoutAdapter.VerticalLayoutViewHolder> {

    private List<String> listItem;

    public class VerticalLayoutViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public VerticalLayoutViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_item);
        }
    }

    public LinearLayoutAdapter(List<String> listItem) {
        this.listItem = listItem;
    }

    @Override
    public VerticalLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerticalLayoutViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_row, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(VerticalLayoutViewHolder holder, int position) {
        holder.textView.setText(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
