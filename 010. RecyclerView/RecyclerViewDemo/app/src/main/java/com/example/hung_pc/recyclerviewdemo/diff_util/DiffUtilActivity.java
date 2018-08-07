package com.example.hung_pc.recyclerviewdemo.diff_util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hung_pc.recyclerviewdemo.R;
import com.example.hung_pc.recyclerviewdemo.utils.DataSource;

public class DiffUtilActivity extends AppCompatActivity {

    RecyclerView        recyclerView;
    DiffUtilAdapter     diffUtilAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_util);

        recyclerView = findViewById(R.id.rv_diff_util);
        findViewById(R.id.btn_get_new_data).setOnClickListener(v -> {
            diffUtilAdapter.onNewData(DataSource.getNewDataSource());
        });

        linearLayoutManager = new LinearLayoutManager(this);
        diffUtilAdapter     = new DiffUtilAdapter(DataSource.getDataSource());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(diffUtilAdapter);
    }
}
