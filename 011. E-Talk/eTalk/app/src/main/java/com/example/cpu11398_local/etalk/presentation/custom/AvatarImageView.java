package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        setBackground(getContext().getDrawable(R.drawable.img_rounded_corners));
        setScaleType(ScaleType.CENTER_CROP);
        setClipToOutline(true);
    }

    public void setImageFromUrl(String url) {
        GlideApp
                .with(getContext())
                .load(url)
                .placeholder(R.drawable.img_avatar_holder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(this);
    }
}
