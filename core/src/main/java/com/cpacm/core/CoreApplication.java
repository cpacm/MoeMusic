package com.cpacm.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;


/**
 * Global application
 *
 * @Auther: cpacm
 * @Date: 2015/10/24 0024-上午 9:59
 */
public class CoreApplication extends Application {

    /**
     * application singleton
     */
    private static CoreApplication instance;

    public static CoreApplication getInstance() {
        return instance;
    }

    /**
     * 运用list来保存每一个activity
     * activity list
     */
    private List<Activity> mList;


    @Override
    public void onCreate() {
        super.onCreate();
        //TODO 解除注释
        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext());
        //LeakCanary.install(this); //TODO 内存泄漏开关
        mList = new LinkedList<>();
        instance = this;
    }

    /**
     * 添加一个activity到列表中<br/>
     * add Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 从列表中删除一个activity<br/>
     * remove Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        try {
            mList.remove(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否已经运行该activity<br/>
     *
     * @param activity
     * @return
     */
    public boolean containActivity(Class activity) {
        for (Activity act : mList) {
            if (act.getClass() == activity) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取已经运行的Activity<br/>
     *
     * @param activity
     * @return
     */
    public Activity getActivity(Class activity) {
        for (Activity act : mList) {
            if (act.getClass() == activity) {
                return act;
            }
        }
        return null;
    }

    /**
     * 关闭list内的每一个activity<br/>
     * close all activity
     */
    public void closeAllActivity() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获得最后打开的activity<br/>
     * get last opened activity
     */
    public Activity getCurActivity() {
        if (mList.size() > 0)
            return mList.get(mList.size() - 1);
        return null;
    }


    /**
     * 关闭list内的每一个activity并且退出应用<br/>
     * close all activity and exit app
     */
    public void exit() {
        closeAllActivity();
        //System.exit(0);
    }


    /**
     * 主动进行内存回收<br/>
     * run system gc
     */
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    /**
     * 获取app内存大小<br/>
     * get memory size
     *
     * @return
     */
    public int getMemSize() {
        return ((ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

}
