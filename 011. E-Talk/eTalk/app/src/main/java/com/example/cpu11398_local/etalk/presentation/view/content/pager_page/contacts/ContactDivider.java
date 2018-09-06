package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ContactDivider extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private int      divider_padding_left;

    public ContactDivider(Drawable divider, int divider_padding_left) {
        this.divider = divider;
        this.divider_padding_left = divider_padding_left;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int itemVisible = parent.getChildCount();
        int itemCount   = parent.getAdapter().getItemCount();
        for (int i = 0; i < itemVisible; ++i) {
            View child = parent.getChildAt(i);
            if (parent.getChildViewHolder(child).getAdapterPosition() != itemCount - 1) {
                divider.setBounds(
                        child.getLeft() + divider_padding_left,
                        child.getBottom() - divider.getIntrinsicHeight(),
                        child.getRight(),
                        child.getBottom()
                );
                divider.draw(c);
            }
        }
    }
}
