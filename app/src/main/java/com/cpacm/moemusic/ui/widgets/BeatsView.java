package com.cpacm.moemusic.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Auther: cpacm
 * @Date: 2016/6/26.
 * @description:
 */
public class BeatsView extends View {

    private Paint paint;
    private int beats = 1;
    private int screenW, screenH;
    private float X, Y;
    private Path path;
    private float initialScreenW;
    private float initialX, plusX;
    private float TX;
    private boolean translate;
    private int flash;

    public BeatsView(Context context) {
        super(context);
        initView(context);
    }

    public BeatsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BeatsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        paint = new Paint();
        paint.setColor(Color.argb(0xff, 0x99, 0x00, 0x00));
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShadowLayer(7, 0, 0, Color.RED);

        path = new Path();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        invalidate();
    }
}
