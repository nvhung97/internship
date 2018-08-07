package com.example.hung_pc.recyclerviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.hung_pc.recyclerviewdemo.diff_util.DiffUtilActivity;
import com.example.hung_pc.recyclerviewdemo.grid_layout.GridLayoutActivity;
import com.example.hung_pc.recyclerviewdemo.linear_layout.LinearLayoutActivity;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.MultipleItemTypesActivity;
import com.example.hung_pc.recyclerviewdemo.staggered_grid_layout.StaggeredGridLayoutActivity;
import com.example.hung_pc.recyclerviewdemo.swipe_delete_item.SwipeDeleteItemActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_linear_layout).setOnClickListener(v -> startActivity(new Intent(this, LinearLayoutActivity.class)));
        findViewById(R.id.btn_grid_layout).setOnClickListener(v -> startActivity(new Intent(this, GridLayoutActivity.class)));
        findViewById(R.id.btn_staggerd_grid_layout).setOnClickListener(v -> startActivity(new Intent(this, StaggeredGridLayoutActivity.class)));
        findViewById(R.id.btn_multiple_item_types).setOnClickListener(v -> startActivity(new Intent(this, MultipleItemTypesActivity.class)));
        findViewById(R.id.btn_swipe_delete_item).setOnClickListener(v -> startActivity(new Intent(this, SwipeDeleteItemActivity.class)));
        findViewById(R.id.btn_diff_util).setOnClickListener(v -> startActivity(new Intent(this, DiffUtilActivity.class)));
    }
}