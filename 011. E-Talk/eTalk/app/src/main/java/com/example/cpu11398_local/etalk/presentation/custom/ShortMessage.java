package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.model.Message;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(String data, long type) {
        if (type == Message.TEXT) {
            setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        }
        if (type == Message.IMAGE) {
            setText(getContext().getString(R.string.app_image));
        }
        if (type == Message.SOUND) {
            setText(getContext().getString(R.string.app_sound));
        }
        if (type == Message.VIDEO) {
            setText(getContext().getString(R.string.app_video));
        }
        if (type == Message.FILE) {
            setText(getContext().getString(R.string.app_file));
        }
    }
}
