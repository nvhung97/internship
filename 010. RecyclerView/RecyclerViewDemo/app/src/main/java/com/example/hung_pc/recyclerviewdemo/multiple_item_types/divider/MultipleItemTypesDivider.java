package com.example.hung_pc.recyclerviewdemo.multiple_item_types.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.ItemType;

/**
 * Created by Hung-pc on 8/3/2018.
 */

public class MultipleItemTypesDivider extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private int      dividerPaddingLeft;
    private int      specialDividerHeight;

    public MultipleItemTypesDivider(Drawable divider,
                                    int dividerPaddingLeft,
                                    int specialDividerHeight) {
        this.divider                = divider;
        this.dividerPaddingLeft     = dividerPaddingLeft;
        this.specialDividerHeight   = specialDividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (viewHolder.getAdapterPosition() == 0) {
            outRect.set(0, 0, 0, 0);
        }
        else {
            switch (viewHolder.getItemViewType()) {
                case ItemType.TYPE_GROUP:
                    outRect.set(0, specialDividerHeight, 0, 0);
                    break;
                case ItemType.TYPE_ITEM:
                    outRect.set(0, divider.getIntrinsicHeight(), 0, 0);
                    break;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount; ++i) {
            if (i == 0) continue;
            View                    child       = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder  = parent.getChildViewHolder(child);
            switch (viewHolder.getItemViewType()) {
                case ItemType.TYPE_GROUP:
                    divider.setBounds(
                            child.getLeft(),
                            child.getTop() - specialDividerHeight,
                            child.getRight(),
                            child.getTop()
                    );
                    break;
                case ItemType.TYPE_ITEM:
                    divider.setBounds(
                            dividerPaddingLeft,
                            child.getTop() - divider.getIntrinsicHeight(),
                            child.getRight(),
                            child.getTop()
                    );
                    break;
            }
            divider.draw(c);
        }
    }
}
