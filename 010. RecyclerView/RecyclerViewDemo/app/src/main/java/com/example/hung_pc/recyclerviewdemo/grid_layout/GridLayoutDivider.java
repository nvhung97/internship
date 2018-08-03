package com.example.hung_pc.recyclerviewdemo.grid_layout;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class GridLayoutDivider extends RecyclerView.ItemDecoration{

    private Drawable divider;

    public GridLayoutDivider(Drawable divider) {
        this.divider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childPosition = parent.getChildAdapterPosition(view);
        outRect.set(
                0,
                0,
                childPosition % 2 != 0 ? 0 : divider.getIntrinsicWidth(),
                divider.getIntrinsicHeight()
        );
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount; ++i) {
            View child  = parent.getChildAt(i);
            divider.setBounds(
                    child.getLeft(),
                    child.getBottom(),
                    child.getRight(),
                    child.getBottom() + divider.getIntrinsicHeight()
            );
            divider.draw(c);
            if (i % 2 == 0) {
                divider.setBounds(
                        child.getRight(),
                        child.getTop(),
                        child.getRight() + divider.getIntrinsicWidth(),
                        child.getBottom() + divider.getIntrinsicHeight()
                );
                divider.draw(c);
            }
        }
    }
}
