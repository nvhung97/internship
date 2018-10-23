package com.example.cpu11398_local.etalk.presentation.view.chat.media;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cpu11398_local.etalk.R;

public class EmojiAdapter extends BaseAdapter{

    private Context context;

    private Integer[] emoji = {
            0x1F600, 0x1F603, 0x1F604, 0x1F601, 0x1F606,
            0x1F605, 0x1F923, 0x1F602, 0x1F642, 0x1F643, 
            0x1F609, 0x1F60A, 0x1F607, 0x1F60E, 0x1F60D,
            0x1F929, 0x1F618, 0x1F617, 0x1F61A, 0x1F619,
            0x1F60B, 0x1F61B, 0x1F61C, 0x1F92A, 0x1F61D,
            0x1F911, 0x1F917, 0x1F92D, 0x1F92B, 0x1F914,
            0x1F910, 0x1F928, 0x1F610, 0x1F611, 0x1F636,
            0x1F60F, 0x1F612, 0x1F644, 0x1F62C, 0x1F925,
            0x1F913, 0x1F9D0, 0x1F615, 0x1F61F, 0x1F641,
            0x1F62E, 0x1F62F, 0x1F632, 0x1F633, 0x1F625,
            0x1F626, 0x1F627, 0x1F628, 0x1F630
    };

    public EmojiAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            textView.setPadding(0, 20, 0, 20);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        } else {
            textView = (TextView)convertView;
        }
        textView.setText(new String(Character.toChars(emoji[position])));
        return textView;
    }

    @Override
    public Object getItem(int position) {
        return emoji[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return emoji.length;
    }
}
