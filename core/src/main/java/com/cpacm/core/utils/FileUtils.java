package com.cpacm.core.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.cpacm.core.CoreApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * manage file include download , upload , read ,write
 * Created by cpcam on 2015/7/7.
 */
public class FileUtils {

    private static final int DELAY_TIME = 10000;

    public final static String CACHE_DIR = "Beats";
    public final static String GLIDE_CACHE_DIR = "glide";
    public final static String SONG_CACHE_DIR = "songs";

    public final static String APK_NAME = "beats.apk";

    /**
     * 应用关联的图片存储空间
     *
     * @param context
     * @return
     */
    public static String getAppPictureDir(Context context) {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return file.getPath();
    }

    /**
     * 获取缓存主目录
     *
     * @return
     */
    public static String getMountedCacheDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + File.separator + CACHE_DIR;
            File path1 = new File(path);
            if (!path1.exists()) {
                path1.mkdirs();
            }
            return path1.getPath();
        }
        return null;
    }

    /**
     * 获取存放歌曲的目录
     *
     * @return
     */
    public static String getSongDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = getMountedCacheDir() + File.separator + SONG_CACHE_DIR;
            File path1 = new File(path);
            if (!path1.exists()) {
                path1.mkdirs();
            }
            return path1.getPath();
        }
        return null;
    }

    /**
     * 获取存放缓存的目录
     *
     * @return
     */
    public static String getCacheDir() {
        File file = CoreApplication.getInstance().getExternalCacheDir();
        return file.getPath();
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件<br/>
     * 支持两级目录删除
     */
    public static void cleanCacheDir() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File directory = CoreApplication.getInstance().getExternalCacheDir();
            if (directory != null && directory.exists() && directory.isDirectory()) {
                for (File item : directory.listFiles()) {
                    if (item.isDirectory()) {
                        for (File img : item.listFiles()) {
                            img.delete();
                        }
                    }
                    item.delete();
                }
            }
        }
    }


    /**
     * 获取apk放置的地址
     *
     * @return
     */
    public static String getApkPath() {
        String apkPath = getCacheDir() + File.separator + APK_NAME;
        File file = new File(apkPath);
        if (file.exists()) {
            file.delete();
        }
        return file.getPath();
    }

    /**
     * 读取Assets目录下的文件
     *
     * @param context
     * @param name
     * @return
     */
    public static String getAssets(Context context, String name) {
        String result = null;
        try {
            InputStream in = context.getAssets().open(name);  //获得AssetManger 对象, 调用其open 方法取得  对应的inputStream对象
            int size = in.available();//取得数据流的数据大小
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            result = new String(buffer);
        } catch (Exception e) {
            MoeLogger.d("getAssets:" + result);
        }
        return result;
    }

    /**
     * 媒体扫描，防止下载后在sdcard中获取不到歌曲的信息
     *
     * @param path
     */
    public static void mp3Scanner(String path) {
        MediaScannerConnection.scanFile(CoreApplication.getInstance().getApplicationContext(),
                new String[]{path}, null, null);
    }

    public static boolean existFile(String path) {
        File path1 = new File(path);
        return path1.exists();
    }

    public static boolean deleteFile(String path) {
        File path1 = new File(path);
        if (path1.exists()) {
            return path1.delete();
        }
        return false;
    }

    /**
     * open apk
     *
     * @param context
     * @param apk
     */
    public static void openApk(Context context, File apk) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 获取下载文件的大小
     *
     * @param soFarBytes 已下载字节
     * @param totalBytes 总共的字节
     * @return
     */
    public static String getProgressSize(long soFarBytes, long totalBytes) {
        float progress = soFarBytes * 1.0f / 1024 / 1024;
        float total = totalBytes * 1.0f / 1024 / 1024;
        String format = "%.1fM/%.1fM";
        String str = String.format(Locale.CHINA, format, progress, total);
        return str;
    }

    /**
     * 获取下载进度
     *
     * @param soFarBytes 已下载字节
     * @param totalBytes 总共的字节
     * @return
     */
    public static int getProgress(long soFarBytes, long totalBytes) {
        if (totalBytes != 0) {
            long progress = soFarBytes * 100 / totalBytes;
            return (int) progress;
        }
        return 0;
    }

    /**
     * download apk from server
     *
     * @param path the apk path
     * @param fp   file progress listener
     * @return apk file
     * @throws Exception
     */
    public static File getFilefromServerToProgress(String path, FileProgress fp) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(DELAY_TIME);
            int max = conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "beats.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                if (fp != null)
                    fp.getProgress(total, max);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    public static String filenameFilter(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("");
    }

    public interface FileProgress {
        void getProgress(int total, int max);
    }
}
