package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;

public class ShortMessage extends android.support.v7.widget.AppCompatTextView{

    public ShortMessage(Context context) {
        super(context);
    }

    public ShortMessage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShortMessage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(String data) {
        switch (data.substring(0,2)) {
            case FirebaseTree.Database.Messages.Keys.Key.Data.TEXT:
                setText(data.substring(3));
                break;
            case FirebaseTree.Database.Messages.Keys.Key.Data.IMAGE:
                setText(getContext().getString(R.string.app_image));
                break;
            case FirebaseTree.Database.Messages.Keys.Key.Data.SOUND:
                setText(getContext().getString(R.string.app_sound));
                break;
            case FirebaseTree.Database.Messages.Keys.Key.Data.VIDEO:
                setText(getContext().getString(R.string.app_video));
                break;
            case FirebaseTree.Database.Messages.Keys.Key.Data.FILE:
                setText(getContext().getString(R.string.app_sound));
                break;
        }
    }
}
