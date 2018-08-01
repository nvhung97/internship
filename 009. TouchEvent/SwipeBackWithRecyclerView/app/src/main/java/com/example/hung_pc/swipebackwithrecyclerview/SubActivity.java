package com.example.hung_pc.swipebackwithrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity {

    RecyclerView    recyclerView;
    MyAdapter       myAdapter;
    List<Integer>   listNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        recyclerView = findViewById(R.id.nested_scrolling_child);
        listNumber = new ArrayList<>();
        for (int i = 0; i < 16; ++i) {
            listNumber.add(i);
        }

        myAdapter = new MyAdapter(listNumber, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
