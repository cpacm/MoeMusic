package com.cpacm.moemusic.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

/**
 * @author: cpacm
 * @date: 2016/7/8
 * @desciption: glide配置
 */
public class MoeGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //builder.setDiskCache(new InternalCacheDiskCacheFactory(context,"cacheDirectoryName", 200*1024*1024));//设置文件缓存的大小为200M
        //builder.setMemoryCache(new LruResourceCache(yourSizeInBytes));//设置内存缓存大小
        //builder.setBitmapPool(new LruBitmapPool(sizeInBytes));//bitmap池大小
        //builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);//bitmap格式 default is RGB_565
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        /*MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
       glide.setMemoryCategory(MemoryCategory.HIGH);*/
    }
}
