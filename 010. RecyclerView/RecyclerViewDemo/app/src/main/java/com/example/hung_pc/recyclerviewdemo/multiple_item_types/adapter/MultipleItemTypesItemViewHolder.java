package com.example.hung_pc.recyclerviewdemo.multiple_item_types.adapter;

import android.view.View;
import android.widget.TextView;
import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.ItemType;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.TypeItem;

/**
 * Created by Hung-pc on 8/3/2018.
 */

public class MultipleItemTypesItemViewHolder extends MultipleItemTypesViewHolder{

    public TextView textView;

    public MultipleItemTypesItemViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.txt_item);
    }

    @Override
    public void bindType(ItemType itemType) {
        textView.setText(((TypeItem)itemType).getText());
    }
}
