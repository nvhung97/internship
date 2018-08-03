package com.example.hung_pc.recyclerviewdemo.multiple_item_types;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.adapter.MultipleItemTypesAdapter;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.divider.MultipleItemTypesDivider;
import com.example.hung_pc.recyclerviewdemo.utils.DataSource;

public class MultipleItemTypesActivity extends AppCompatActivity {

    RecyclerView             recyclerView;
    MultipleItemTypesAdapter multipleItemTypesAdapter;
    LinearLayoutManager      linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_item_types);

        recyclerView = findViewById(R.id.rv_multiple_item_types);

        linearLayoutManager      = new LinearLayoutManager(this);
        multipleItemTypesAdapter = new MultipleItemTypesAdapter(DataSource.getDataSourceWithType());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(
                new MultipleItemTypesDivider(
                        getDrawable(R.drawable.divider),
                        getResources().getDimensionPixelSize(R.dimen.divider_padding_left),
                        getResources().getDimensionPixelSize(R.dimen.special_divider_height)
                )
        );
        recyclerView.setAdapter(multipleItemTypesAdapter);
    }
}
