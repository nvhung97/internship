package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.utils.GlideApp;

public class AvatarImageView extends AppCompatImageView {

    public AvatarImageView(Context context) {
        super(context);
        init();
    }

    public AvatarImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(Float.MAX_VALUE);
        shape.setColor(getContext().getResources().getColor(R.color.colorWhite));
        setBackground(shape);
        setScaleType(ScaleType.CENTER_CROP);
        setClipToOutline(true);
    }

    public void setImageFromObject(Object object) {
        GlideApp
                .with(getContext())
                .load(object)
                .placeholder(R.drawable.img_avatar_holder)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(this);
    }
}
