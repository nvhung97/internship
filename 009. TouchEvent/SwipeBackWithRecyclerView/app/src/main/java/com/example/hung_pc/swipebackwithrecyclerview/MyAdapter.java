package com.example.hung_pc.swipebackwithrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Hung-pc on 8/1/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Integer>   listNumber;
    private Context         context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyView myView;
        public MyViewHolder(View view) {
            super(view);
            myView = view.findViewById(R.id.item_number);
        }
    }

    public MyAdapter(List<Integer> listNumber, Context context) {
        this.listNumber = listNumber;
        this.context    = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.myView.setText(String.valueOf(listNumber.get(position)));
        holder.myView.setBackgroundColor(context.getResources().getColor(position % 2 == 0 ? R.color.colorGray : R.color.colorWhite));
    }

    @Override
    public int getItemCount() {
        return listNumber.size();
    }
}
