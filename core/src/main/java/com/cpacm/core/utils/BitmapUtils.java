package com.cpacm.core.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.TypedValue;

import com.cpacm.core.CoreApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 一些绘画常用方法
 *
 * @Auther: cpacm
 * @Date: 2016/1/6 0006-下午 12:18
 */
public class BitmapUtils {

    public static Bitmap createBlurredImageFromBitmap(Bitmap bitmap, Context context, int inSampleSize) {

        RenderScript rs = RenderScript.create(context);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        Bitmap blurTemplate = BitmapFactory.decodeStream(bis, null, options);

        final Allocation input = Allocation.createFromBitmap(rs, blurTemplate);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurTemplate);
        return blurTemplate;
        //return new BitmapDrawable(context.getResources(), blurTemplate);
    }


    public static Bitmap getBitmapByString(String imgSrc) {
        if (imgSrc != null) {
            byte[] buf = Base64.decode(imgSrc, Base64.NO_WRAP);
            ByteArrayInputStream bas = new ByteArrayInputStream(buf);
            Bitmap pic = BitmapFactory.decodeStream(bas);
            return pic;
        } else
            return null;
    }

    public static String setBitmapByString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //把图片位图按照JPEG格式压缩  压缩比率为50%,
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            //使用Base64编码将图片的byte数组转换成字符串
            String imgStr = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
            return imgStr;
        } else
            return null;
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dp （DisplayMetrics类中属性density）
     * @return
     */
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                CoreApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dp(float pxValue) {
        final float scale = CoreApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = CoreApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = CoreApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
