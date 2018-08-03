package com.example.hung_pc.recyclerviewdemo.multiple_item_types.adapter;

import android.view.View;
import android.widget.TextView;
import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.ItemType;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.TypeGroup;

/**
 * Created by Hung-pc on 8/3/2018.
 */

public class MultipleItemTypesGroupViewHolder extends MultipleItemTypesViewHolder {

    public TextView textView;

    public MultipleItemTypesGroupViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.txt_group);
    }

    @Override
    public void bindType(ItemType itemType) {
        textView.setText(((TypeGroup)itemType).getText());
    }
}
