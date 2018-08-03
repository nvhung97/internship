package com.example.hung_pc.recyclerviewdemo.multiple_item_types.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.ItemType;
import java.util.List;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class MultipleItemTypesAdapter extends RecyclerView.Adapter<MultipleItemTypesViewHolder>{

    private List<ItemType> listItem;

    public MultipleItemTypesAdapter(List<ItemType> listItem) {
        this.listItem = listItem;
    }

    @Override
    public int getItemViewType(int position) {
        return listItem.get(position).getItemType();
    }

    @Override
    public MultipleItemTypesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemType.TYPE_GROUP:
                return new MultipleItemTypesGroupViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.item_group, parent, false)
                );
            case ItemType.TYPE_ITEM:
                return new MultipleItemTypesItemViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.item_row, parent, false)

                );
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MultipleItemTypesViewHolder holder, int position) {
        holder.bindType(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
