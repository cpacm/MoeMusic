package com.cpacm.moemusic.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author: cpacm
 * @date: 2016/7/18
 * @desciption: 用于获取音乐播放的service
 */
public class MediaServiceHelper {
    Context cxt;
    private static MediaServiceHelper i = new MediaServiceHelper();

    public static MediaServiceHelper get(Context c) {
        i.cxt = c;
        return i;
    }

    private MediaServiceHelper() {
    }

    MusicService mService;
    public void initService(){
        if (mService == null){
            Intent i = new Intent(cxt, MusicService.class);
            ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
                    mService = binder.getService();
                    mService.setUp();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mService = null;
                }
            };
            cxt.startService(i);
            cxt.bindService(i, conn, Context.BIND_AUTO_CREATE);
        }
    }

    public MusicService getService() {
        return mService;
    }

}
