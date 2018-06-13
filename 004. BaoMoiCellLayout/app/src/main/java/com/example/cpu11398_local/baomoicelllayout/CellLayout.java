package com.example.cpu11398_local.baomoicelllayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CellLayout extends FrameLayout {

    final int OBJECT_MARGIN = (int)TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10,
            getResources().getDisplayMetrics()
    );
    final int IMAGE_SIZE = (int)TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            100,
            getResources().getDisplayMetrics()
    );

    public CellLayout(@NonNull Context context) {
        super(context);
    }

    public CellLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CellLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ImageView   imageView;
    TextView    textViewTitle;
    TextView    textViewSource;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView      = findViewById(R.id.cell_image);
        textViewTitle  = findViewById(R.id.cell_title);
        textViewSource = findViewById(R.id.cell_source);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize   = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize  = MeasureSpec.getSize(heightMeasureSpec);
        int width       = widthSize;
        int height      = 0;

        imageView.measure(
                IMAGE_SIZE | MeasureSpec.EXACTLY,
                IMAGE_SIZE | MeasureSpec.EXACTLY
        );
        textViewSource.measure(
                widthSize - 3 * OBJECT_MARGIN - imageView.getMeasuredWidth() | MeasureSpec.AT_MOST,
                heightSize | MeasureSpec.AT_MOST
        );
        textViewTitle.measure(
                widthSize - 3 * OBJECT_MARGIN - imageView.getMeasuredWidth() | MeasureSpec.EXACTLY,
                heightSize - 3 * OBJECT_MARGIN - textViewSource.getMeasuredHeight() | MeasureSpec.AT_MOST
        );

        height = Math.max(
                imageView.getMeasuredHeight() + 2 * OBJECT_MARGIN,
                textViewTitle.getMeasuredHeight() + textViewSource.getMeasuredHeight() + 3 * OBJECT_MARGIN
        );

        setMeasuredDimension(width, height);
    }

   @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int leftPos = OBJECT_MARGIN;
        int topPos  = OBJECT_MARGIN;

       imageView.layout(
               leftPos,
               topPos,
               leftPos + imageView.getMeasuredWidth(),
               topPos + imageView.getMeasuredHeight()
       );

       leftPos +=  imageView.getMeasuredWidth() + OBJECT_MARGIN;
       textViewTitle.layout(
               leftPos,
               topPos,
               leftPos + textViewTitle.getMeasuredWidth(),
               topPos + textViewTitle.getMeasuredHeight()
       );

       topPos = (textViewTitle.getMeasuredHeight() + textViewSource.getMeasuredHeight() + OBJECT_MARGIN < imageView.getMeasuredHeight())
               ? bottom - top - OBJECT_MARGIN - textViewSource.getMeasuredHeight()
               : 2 * OBJECT_MARGIN + textViewTitle.getMeasuredHeight();
       textViewSource.layout(
               leftPos,
               topPos,
               leftPos + textViewSource.getMeasuredWidth(),
               topPos + textViewSource.getMeasuredHeight()
       );
    }
}
