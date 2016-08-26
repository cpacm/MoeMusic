package com.cpacm.core.utils;

import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * @author: cpacm
 * @date: 2016/7/21
 * @desciption:
 */
public class FileUtils {

    public static String getSDCardFilePath(String filePath) {
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        String path = sdCardPath + File.separator + filePath;
        return path;
    }
}
