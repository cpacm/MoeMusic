package com.cpacm.moemusic.ui.widgets.floatingmusicmenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: cpacm
 * @date: 2016/8/10
 * @desciption: music button
 */
public class FloatingMusicButton extends FloatingActionButton {

    private RotatingProgressDrawable coverDrawable;
    private int percent, color;
    private ColorStateList backgroundHint;
    private float progress = 0f;

    public FloatingMusicButton(Context context) {
        super(context);
    }

    public FloatingMusicButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingMusicButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setMaxImageSize();
    }

    /**
     * 利用反射重新定义fab图片的大小，默认充满整个fab
     */
    public void setMaxImageSize() {
        try {
            Class clazz = getClass().getSuperclass();
            Method sizeMethod = clazz.getDeclaredMethod("getSizeDimension");
            sizeMethod.setAccessible(true);
            int size = (Integer) sizeMethod.invoke(this);
            Field field = clazz.getDeclaredField("mMaxImageSize");
            field.setAccessible(true);
            field.set(this, size);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        postInvalidate();
    }

    /**
     * 对fmb进行配置
     *
     * @param percent        进度条宽度百分比
     * @param color          进度条颜色
     * @param backgroundHint fmb背景颜色
     */
    public void config(int percent, int color, ColorStateList backgroundHint) {
        this.percent = percent;
        this.color = color;
        this.backgroundHint = backgroundHint;
        config();
    }

    public void config() {
        if (coverDrawable != null) {
            coverDrawable.setProgressWidthPercent(percent);
            coverDrawable.setProgressColor(color);
            if (backgroundHint != null) {
                setBackgroundTintList(backgroundHint);
            }
        }
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
        if (coverDrawable != null) {
            coverDrawable.setProgress(progress);
        }
    }

    /**
     * 设置按钮背景
     *
     * @param drawable
     */
    public void setCoverDrawable(Drawable drawable) {
        this.coverDrawable = new RotatingProgressDrawable(drawable);
        config();
        setImageDrawable(this.coverDrawable);
    }

    public void setCoverDrawable(RotatingProgressDrawable drawable) {
        this.coverDrawable = drawable;
        config();
        setImageDrawable(this.coverDrawable);
    }

    public void setCover(Bitmap bitmap) {
        coverDrawable = new RotatingProgressDrawable(bitmap);
        config();
        setImageDrawable(this.coverDrawable);
    }

    public RotatingProgressDrawable getCoverDrawable() {
        return coverDrawable;
    }

    public void start() {
        coverDrawable.rotate(true);
    }

    public void stop() {
        coverDrawable.rotate(false);
    }

}
