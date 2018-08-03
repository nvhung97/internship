package com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type;

/**
 * Created by Hung-pc on 8/3/2018.
 */

public class TypeItem implements ItemType {
    private String text;

    public TypeItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getItemType() {
        return TYPE_ITEM;
    }
}