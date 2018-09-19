package com.example.cpu11398_local.etalk.presentation.view.chat.person;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ChatDivider extends RecyclerView.ItemDecoration {

    private int divider_space_same;
    private int divider_space_diff;

    public ChatDivider(int divider_space_same, int divider_space_diff) {
        this.divider_space_same = divider_space_same;
        this.divider_space_diff = divider_space_diff;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = state.getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == itemCount - 1) {
            outRect.set(0, 0, 0, divider_space_diff);
        } else {
            outRect.set(0, 0, 0, divider_space_same);
        }
    }
}