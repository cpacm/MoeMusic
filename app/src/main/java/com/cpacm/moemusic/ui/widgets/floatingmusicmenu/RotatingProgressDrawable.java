package com.cpacm.moemusic.ui.widgets.floatingmusicmenu;

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
import android.os.Handler;
import android.os.Message;

/**
 * @author: cpacm
 * @date: 2016/8/10
 * @desciption: music button cover drawable
 */
public class RotatingProgressDrawable extends Drawable {

    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_4444;
    private Paint mPaint, progressPaint;
    private Bitmap mBitmap;
    private int mWidth;
    private float mRotation;
    private RectF rectF;

    private float progress;//进度条
    private int progressPercent;//进度条宽度
    private int progressColor;//进度条颜色

    // 旋转控制
    private RotateHandler rotateHandler;
    private RotateThread rotateThread;


    public RotatingProgressDrawable(Drawable drawable) {
        initDrawable();
        circleBitmapFromDrawable(drawable);
    }

    public RotatingProgressDrawable(Bitmap bitmap) {
        initDrawable();
        mBitmap = bitmap;
        circleBitmap();
    }

    public RotatingProgressDrawable() {
        initDrawable();
    }

    private void initDrawable() {
        progressPercent = 3;
        progress = 0f;
        progressColor = Color.RED;

        rotateHandler = new RotateHandler();
        rotateThread = new RotateThread();
        rotateThread.start();

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
        if (rotateThread != null && !rotateThread.isPause) {
            return;
        }
        invalidateSelf();
    }

    /**
     * 设置进度条相对于图片的百分比，默认为3%
     *
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
     *
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidateSelf();
    }

    /**
     * 是否开始旋转
     *
     * @param rotate
     */
    public void rotate(boolean rotate) {
        if (rotate) {
            if (rotateThread != null) {
                rotateThread.restart();
            } else {
                rotateThread = new RotateThread();
                rotateThread.start();
            }
        } else {
            if (rotateThread != null) {
                rotateThread.pause();
            }
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

    public void destroy() {
        rotateThread.cancel();
    }

    public class RotateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mRotation = mRotation + 1;
                if (mRotation > 360) {
                    mRotation = 0;
                }
                setRotation(mRotation);
            }
            super.handleMessage(msg);
        }

    }

    public class RotateThread extends Thread {

        public boolean isRotate = true;
        public boolean isPause = true;

        @Override
        public void run() {
            super.run();
            while (isRotate) {
                try {
                    sleep(25);
                    if (!isPause) {
                        rotateHandler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            this.isRotate = false;
        }

        public void pause() {
            isPause = true;
        }

        public void restart() {
            isPause = false;
        }

    }

}
