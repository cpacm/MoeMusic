package com.cpacm.moemusic.utils;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * @author: cpacm
 * @date: 2016/7/8
 * @desciption: glide配置
 */
public class MoeGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置内存缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));

        int diskCacheSize = 1024 * 1024 * 50;//最多可以缓存多少字节的数据
        String path = getCacheDir(context);
        if (path == null) {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
        } else {
            builder.setDiskCache(new DiskLruCacheFactory(path, "glide", diskCacheSize));
        }
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

    public String getCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + "/Beats";
            File path1 = new File(path);
            if (!path1.exists()) {
                path1.mkdirs();
            }
            return path1.getPath();
        }
        return null;
    }
}
