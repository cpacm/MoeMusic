package com.cpacm.moemusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.ui.music.SongPlayerActivity;

/**
 * @Auther: cpacm
 * @Date: 2016/6/25.
 * @description: 所有Activity的父类
 */
public abstract class AbstractAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动音乐服务
        MusicPlayerManager.startServiceIfNecessary(getApplicationContext());

        MoeApplication.getInstance().addActivity(this);
    }

    /**
     * snackbar的显示
     *
     * @param toast
     */
    public void showSnackBar(String toast) {
        Snackbar.make(getWindow().getDecorView(), toast, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBar(@StringRes int toast) {
        Snackbar.make(getWindow().getDecorView(), getString(toast), Snackbar.LENGTH_SHORT).show();
    }


    public void showToast(int toastRes) {
        Toast.makeText(this, getString(toastRes), Toast.LENGTH_SHORT).show();
    }

    /**
     * activity的跳转
     *
     * @param activity
     */
    public void startActivity(Class activity) {
        Intent i = new Intent();
        i.setClass(this, activity);
        startActivity(i);
    }

    public boolean gotoSongPlayerActivity() {
        if (MusicPlayerManager.get().getPlayingSong() == null) {
            showToast(R.string.music_playing_none);
            return false;
        }
        SongPlayerActivity.open(this);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MoeApplication.getInstance().removeActivity(this);
    }
}
