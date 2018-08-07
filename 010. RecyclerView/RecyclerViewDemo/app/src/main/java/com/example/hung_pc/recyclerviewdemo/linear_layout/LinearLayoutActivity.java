package com.example.hung_pc.recyclerviewdemo.linear_layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.utils.DataSource;

public class LinearLayoutActivity extends AppCompatActivity {

    RecyclerView        recyclerView;
    LinearLayoutAdapter linearLayoutAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);

        recyclerView = findViewById(R.id.rv_vertical_layout);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutAdapter = new LinearLayoutAdapter(DataSource.getDataSource());
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new LinearLayoutDivider(getDrawable(R.drawable.divider)));
        recyclerView.setAdapter(linearLayoutAdapter);
    }
}