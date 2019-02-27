package com.example.hung_pc.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FadingEdgeLayoutWithoutPorterDuff extends FrameLayout {

    private final int FADING_EDGE_LENGTH = 100; //pixels

    public FadingEdgeLayoutWithoutPorterDuff(Context context) {
        super(context);
    }

    public FadingEdgeLayoutWithoutPorterDuff(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FadingEdgeLayoutWithoutPorterDuff(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Bitmap bm = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bm);
        super.dispatchDraw(cv);
        bm = fadeEdge(bm);
        canvas.drawBitmap(bm, 0, 0, null);
    }

    //Fade top edge layout
    private Bitmap fadeEdge(Bitmap bm) {
        int   bmW = bm.getWidth();
        int   bmH = bm.getHeight();
        int[] buf = new int[bmW * bmH];
        bm.getPixels(buf, 0, bmW, 0, 0, bmW, bmH);

        for (int y = 0; y < FADING_EDGE_LENGTH; y++) {
            int newAlpha = y * 256 / FADING_EDGE_LENGTH;
            for (int x = 0; x < bmW; x++) {
                int index = y * bmW + x;
                int alpha = (buf[index] >> 24) & 0xFF;
                if (alpha > newAlpha) {
                    buf[index] = (buf[index] & 0xFFFFFF) | (newAlpha << 24);
                }
            }
        }

        return Bitmap.createBitmap(buf, bmW, bmH, Bitmap.Config.ARGB_8888);
    }
}
