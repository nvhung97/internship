package com.example.hung_pc.recyclerviewdemo.staggered_grid_layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.utils.DataSource;

public class StaggeredGridLayoutActivity extends AppCompatActivity {

    RecyclerView               recyclerView;
    StaggeredGridLayoutAdapter staggeredGridLayoutAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid_layout);

        recyclerView = findViewById(R.id.rv_staggered_grid_layout);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutAdapter = new StaggeredGridLayoutAdapter(DataSource.getDataSource());
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addItemDecoration(new StaggeredGridLayoutDivider(getDrawable(R.drawable.divider)));
        recyclerView.setAdapter(staggeredGridLayoutAdapter);
    }
}
