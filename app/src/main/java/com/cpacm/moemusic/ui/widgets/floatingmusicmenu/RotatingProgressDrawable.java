package com.cpacm.moemusic.ui.widgets.floatingmusicmenu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/**
 * @author: cpacm
 * @date: 2016/8/10
 * @desciption: music button cover drawable
 */
public class RotatingProgressDrawable extends Drawable {

    private static final int START_DURATION = 500;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_4444;
    private Paint mPaint, progressPaint;
    private Bitmap mBitmap;
    private int mWidth;
    private float mRotation;
    private RectF rectF;
    private ObjectAnimator rotationAnimator;

    private float progress;//进度条
    private int progressPercent;//进度条宽度
    private int progressColor;//进度条颜色
    private int cycleSpeed;//绕一圈所需要的时间


    public RotatingProgressDrawable(Drawable drawable) {
        circleBitmapFromDrawable(drawable);
        initDrawable();
    }

    public RotatingProgressDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        circleBitmap();
        initDrawable();
    }

    public RotatingProgressDrawable() {
        initDrawable();
    }

    private void initDrawable() {
        progressPercent = 3;
        progress = 0f;
        cycleSpeed = 6000;
        progressColor = Color.RED;

        rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        rotationAnimator.setDuration(cycleSpeed);
        rotationAnimator.setStartDelay(START_DURATION);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);

        rectF = new RectF();
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
    }

    @SuppressWarnings("UnusedDeclaration")
    public float getRotation() {
        return mRotation;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setRotation(float rotation) {
        mRotation = rotation;
        invalidateSelf();
    }

    public void setDrawable(Drawable drawable) {
        circleBitmapFromDrawable(drawable);
        invalidateSelf();
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        circleBitmap();
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        float progressWidth = mWidth * progressPercent / 100f;
        float halfWidth = progressWidth / 2;
        canvas.save();
        canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - progressWidth, mPaint);
        canvas.restore();
        rectF.set(halfWidth, halfWidth, mWidth - halfWidth, mWidth - halfWidth);
        canvas.drawArc(rectF, -90, progress, false, progressPaint);
    }

    /**
     * set progress
     * 设置进度
     *
     * @param progress 0-100
     */
    public void setProgress(float progress) {
        if (progress < 0 || progress > 100)
            return;
        progress = progress * 360 / 100f;
        this.progress = progress;
    }

    /**
     * 设置进度条相对于图片的百分比，默认为3%
     * @param percent 0-100
     */
    public void setProgressWidthPercent(int percent) {
        this.progressPercent = percent;
        if (mWidth > 0) {
            float progressWidth = mWidth * percent / 100f;
            progressPaint.setStrokeWidth(progressWidth);
        }
        invalidateSelf();
    }

    /**
     * 设置进度条的颜色
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidateSelf();
    }

    /**
     * 设置周速
     * @param cycleSpeed
     */
    public void setCycleSpeed(int cycleSpeed) {
        this.cycleSpeed = cycleSpeed;
        rotationAnimator.setDuration(cycleSpeed);
    }

    /**
     * 是否开始旋转
     * @param rotate
     */
    public void rotate(boolean rotate) {
        if (rotate) {
            rotationAnimator.start();
        } else {
            rotationAnimator.end();
        }
    }

    private void circleBitmap() {
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
        float progressWidth = mWidth * progressPercent / 100f;
        progressPaint.setStrokeWidth(progressWidth);
    }

    private void circleBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (drawable instanceof ColorDrawable) {
            mBitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                    COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
        } else {
            mBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), BITMAP_CONFIG);
        }
        Canvas canvas = new Canvas(mBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
        float progressWidth = mWidth * progressPercent / 100f;
        progressPaint.setStrokeWidth(progressWidth);
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
