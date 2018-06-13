package com.example.cpu11398_local.volleydemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.cpu11398_local.volleydemo.model.Question;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Context         context;
    private List<Question> questions;

    public ListViewAdapter(Context context, List<Question> questions) {
        this.context    = context;
        this.questions  = questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.row_question, null);
        TextView textView = view.findViewById(R.id.txt_title);

        textView.setText(questions.get(position).getTitle());

        return view;
    }
}
