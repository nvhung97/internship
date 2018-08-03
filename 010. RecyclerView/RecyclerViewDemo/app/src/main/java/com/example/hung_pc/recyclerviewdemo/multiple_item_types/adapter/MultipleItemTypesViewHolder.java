package com.example.hung_pc.recyclerviewdemo.multiple_item_types.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.ItemType;

/**
 * Created by Hung-pc on 8/3/2018.
 */

public abstract class MultipleItemTypesViewHolder extends RecyclerView.ViewHolder{

    public MultipleItemTypesViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindType(ItemType itemType);
}
