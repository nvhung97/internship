package com.example.hung_pc.recyclerviewdemo.diff_util;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback{

    private List<String> listOldItem;
    private List<String> listNewItem;

    public DiffUtilCallback(List<String> listOldItem, List<String> listNewItem) {
        this.listOldItem = listOldItem;
        this.listNewItem = listNewItem;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return listNewItem.get(newItemPosition);
    }

    @Override
    public int getOldListSize() {
        return listOldItem != null ? listOldItem.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return listNewItem != null ? listNewItem.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return listOldItem.get(oldItemPosition).substring(0, 1)
                .equals(listNewItem.get(newItemPosition).substring(0, 1));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return listOldItem.get(oldItemPosition).equals(listNewItem.get(newItemPosition));
    }
}
