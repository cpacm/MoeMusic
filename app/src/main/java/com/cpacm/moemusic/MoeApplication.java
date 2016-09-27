package com.cpacm.moemusic;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.cpacm.core.CoreApplication;
import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.db.dao.AccountDao;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/7/8
 * @desciption: 应用
 */
public class MoeApplication extends CoreApplication {

    /**
     * application singleton
     */
    private static MoeApplication instance;

    public static MoeApplication getInstance() {
        return instance;
    }

    /**
     * 运用list来保存每一个activity
     * activity list
     */
    private List<Activity> mList;

    /**
     * 用户信息，作为全局变量存储
     */
    private AccountBean accountBean;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mList = new LinkedList<>();

        //预先加载数据库的信息
        SongManager.getInstance();
        CollectionManager.getInstance();
    }

    public AccountBean getAccountBean() {
        if (accountBean == null) {
            int uid = SettingManager.getInstance().getSetting(SettingManager.ACCOUNT_ID, -1);
            AccountDao accountDao = new AccountDao();
            accountBean = accountDao.query(uid);
            accountDao.close();
        }
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
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
