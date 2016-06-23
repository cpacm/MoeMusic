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
        editor.commit();
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
        editor.commit();
    }

    public Boolean getSetting(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }
}
