package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MyView extends FrameLayout {

    private static final int[] FADE_COLORS = new int[]{Color.TRANSPARENT, Color.BLACK};
    private static final int FADEING_LENGTH = 200;
    private Paint               paint;
    private Matrix              matrix;
    private Shader              shader;
    private PorterDuffXfermode  xfermode;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint    = new Paint(Paint.ANTI_ALIAS_FLAG);
        matrix   = new Matrix();
        shader   = new LinearGradient(0, 0, 0, 1, FADE_COLORS, null, Shader.TileMode.CLAMP);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int l = 0;
        int t = 0;
        int r = getMeasuredWidth();
        int b = t + FADEING_LENGTH;
        int saveCount = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        matrix.setScale(1, FADEING_LENGTH);
        matrix.postTranslate(l, t);
        shader.setLocalMatrix(matrix);
        paint.setXfermode(xfermode);
        paint.setShader(shader);
        canvas.drawRect(l, t, r, b, paint);
        canvas.restoreToCount(saveCount);
    }
}