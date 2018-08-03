package com.example.hung_pc.recyclerviewdemo.swipe_delete_item;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Hung-pc on 8/3/2018.
 */

public class SwipeDeleteItemCallback extends ItemTouchHelper.SimpleCallback {

    private SwipeDeleteItemListener swipeDeleteItemListener;

    public SwipeDeleteItemCallback(int dragDirs,
                                   int swipeDirs,
                                   SwipeDeleteItemListener swipeDeleteItemListener) {
        super(dragDirs, swipeDirs);
        this.swipeDeleteItemListener = swipeDeleteItemListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(
                    ((SwipeDeleteItemAdapter.SwipeDeleteItemViewHolder)viewHolder).textView
            );
        }
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                ((SwipeDeleteItemAdapter.SwipeDeleteItemViewHolder)viewHolder).textView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
        );
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(
                ((SwipeDeleteItemAdapter.SwipeDeleteItemViewHolder)viewHolder).textView
        );
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        swipeDeleteItemListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    public interface SwipeDeleteItemListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
