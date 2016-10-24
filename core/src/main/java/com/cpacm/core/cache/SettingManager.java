package com.cpacm.core.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.cpacm.core.CoreApplication;

/**
 * app设置管理类，主要记录一些用户的设置和用户的首次操作<br/>
 * Recording the first operation of the user
 *
 * @Auther: cpacm
 * @Date: 2015/10/22 0022-下午 5:36
 */
public class SettingManager {

    private static SettingManager instance;

    /**
     * singleton
     *
     * @return
     */
    public static SettingManager getInstance() {
        if (instance == null) {
            synchronized (SettingManager.class) {
                if (instance == null) {
                    instance = new SettingManager();
                }
            }
        }
        return instance;
    }

    private static final String SETTING_PREFERENCE = "SETTINGMANAGER";//设置文件的文件名

    public static final String FIRST_APP_START = "first_app_start";//app第一次启动
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACCESS_TOKEN_SECRET = "access_token_secret";
    public static final String ACCOUNT_ID = "account_id";

    //设置
    public static final String SETTING_NOTIFY = "setting_notify";//歌曲通知栏
    public static final String SETTING_WIFI = "setting_wifi";//仅wifi下载歌曲


    private SharedPreferences sharedPreferences;
    private Context context;

    private SettingManager() {
        context = CoreApplication.getInstance().getApplicationContext();
    }

    /**
     * 获取setting的sp文件
     *
     * @return
     */
    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SETTING_PREFERENCE, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public void setSetting(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSetting(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public String getSetting(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public void setSetting(String key, boolean flag) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, flag);
        editor.apply();
    }

    public Boolean getSetting(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public void setSetting(String key, int flag) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, flag);
        editor.apply();
    }

    public Integer getSetting(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    /**
     * 清除用户登录信息
     */
    public void clearAccount() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(ACCOUNT_ID);
        editor.remove(ACCESS_TOKEN_SECRET);
        editor.remove(ACCESS_TOKEN);
        editor.apply();
    }

    /**
     * 清空用户配置
     */
    public void clear() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }
}
