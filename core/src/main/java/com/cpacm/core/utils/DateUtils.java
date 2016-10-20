package com.cpacm.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 时间日期工具
 *
 * @Auther: cpacm
 * @Date: 2015/12/29 0029-下午 2:48
 */
public class DateUtils {

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     *
     * @return
     */
    public static long getUnixStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 得到昨天的日期
     *
     * @return
     */
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 获取今天0时的时间戳
     *
     * @return
     */
    public static long getTodayTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获取昨天0时的时间戳
     *
     * @return
     */
    public static long getYesterdayTime() {
        return getTodayTime() - 24 * 3600 * 1000;
    }


    /**
     * 时间戳转化为时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 得到日期   yyyy-MM-dd
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    public static StringBuilder getTime(int time) {
        int cache = time / 1000;
        int second = cache % 60;
        cache = cache / 60;
        int minute = cache % 60;
        int hour = cache / 60;
        StringBuilder timeStamp = new StringBuilder();
        if (hour > 0) {
            timeStamp.append(hour);
            timeStamp.append(":");
        }
        if (minute < 10) {
            timeStamp.append("0");
        }
        timeStamp.append(minute);
        timeStamp.append(":");

        if (second < 10) {
            timeStamp.append("0");
        }
        timeStamp.append(second);
        return timeStamp;
    }

    /**
     * 得到时间  HH:mm:ss
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else {
            return formatDate(timeStamp);
        }
    }

    public static String convertTimeToFormat(String timeStr) {
        long timeStamp = Long.decode(timeStr);
        timeStamp = timeStamp / (long) 1000;
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else {
            return timeStampToStr(timeStamp);
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        return time / 60 + "";
    }

    /**
     * 获取最近10年的年份
     *
     * @return
     */
    public static String[] getRecentYears() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        String currentYear = sdf.format(System.currentTimeMillis());
        int year = Integer.parseInt(currentYear);
        String[] arr = new String[11];
        arr[0] = "全部";
        for (int i = 0; i < 10; i++) {
            arr[i + 1] = String.valueOf(year - i);
        }
        return arr;
    }

}
