package com.example.hung_pc.recyclerviewdemo.staggered_grid_layout;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class StaggeredGridLayoutDivider extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public StaggeredGridLayoutDivider(Drawable divider) {
        this.divider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(
                0,
                0,
                ((StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanIndex() != 0
                        ? 0
                        : divider.getIntrinsicWidth(),
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
            if (((StaggeredGridLayoutManager.LayoutParams)child.getLayoutParams()).getSpanIndex() == 0) {
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
