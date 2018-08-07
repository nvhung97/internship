package com.example.hung_pc.recyclerviewdemo.diff_util;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hung_pc.recyclerviewdemo.R;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class DiffUtilAdapter extends RecyclerView.Adapter<DiffUtilAdapter.VerticalLayoutViewHolder> {

    public List<String> listItem;

    public class VerticalLayoutViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public VerticalLayoutViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_item);
        }
    }

    public DiffUtilAdapter(List<String> listItem) {
        this.listItem = listItem;
    }

    @Override
    public VerticalLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerticalLayoutViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_row, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(VerticalLayoutViewHolder holder, int position) {
        holder.textView.setText(listItem.get(position));
    }

    @Override
    public void onBindViewHolder(VerticalLayoutViewHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.textView.setText(String.valueOf(payloads.get(0)));
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public void onNewData(List<String> listNewItem) {
        Observable
                .just(DiffUtil.calculateDiff(new DiffUtilCallback(listItem, listNewItem)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DiffUtil.DiffResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DiffUtil.DiffResult diffResult) {
                        listItem = listNewItem;
                        diffResult.dispatchUpdatesTo(DiffUtilAdapter.this);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}