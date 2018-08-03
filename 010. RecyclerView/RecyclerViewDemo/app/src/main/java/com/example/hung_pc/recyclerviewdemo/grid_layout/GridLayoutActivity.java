package com.example.hung_pc.recyclerviewdemo.grid_layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.utils.DataSource;

public class GridLayoutActivity extends AppCompatActivity {

    RecyclerView      recyclerView;
    GridLayoutAdapter gridLayoutAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_layout);

        recyclerView = findViewById(R.id.rv_grid_layout);

        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutAdapter = new GridLayoutAdapter(DataSource.getDataSource());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridLayoutDivider(getDrawable(R.drawable.divider)));
        recyclerView.setAdapter(gridLayoutAdapter);
    }
}
