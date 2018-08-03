package com.example.hung_pc.recyclerviewdemo.swipe_delete_item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.utils.DataSource;

public class SwipeDeleteItemActivity extends AppCompatActivity implements SwipeDeleteItemCallback.SwipeDeleteItemListener {

    RecyclerView            recyclerView;
    SwipeDeleteItemAdapter  swipeDeleteItemAdapter;
    LinearLayoutManager     linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_delete_item);

        recyclerView = findViewById(R.id.rv_swipe_delete_item);

        linearLayoutManager     = new LinearLayoutManager(this);
        swipeDeleteItemAdapter  = new SwipeDeleteItemAdapter(DataSource.getDataSource());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SwipeDeleteItemDivider(getDrawable(R.drawable.divider)));
        recyclerView.setAdapter(swipeDeleteItemAdapter);
        new ItemTouchHelper(new SwipeDeleteItemCallback(0, ItemTouchHelper.LEFT, this))
                .attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof SwipeDeleteItemAdapter.SwipeDeleteItemViewHolder) {
            swipeDeleteItemAdapter.removeAt(position);
        }
    }
}
