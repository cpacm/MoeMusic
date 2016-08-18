package com.cpacm.moemusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cpacm.moemusic.music.MusicPlayerManager;

/**
 * @Auther: cpacm
 * @Date: 2016/6/25.
 * @description: 所有Activity的父类
 */
public abstract class AbstractAppActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //启动音乐服务
        MusicPlayerManager.startServiceIfNecessary(getApplicationContext());
    }

    /**
     * snackbar的显示
     *
     * @param toast
     */
    public void showSnackBar(String toast) {
        Snackbar.make(getWindow().getDecorView(), toast, Snackbar.LENGTH_SHORT).show();
    }

    public void showToast(String toast) {
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
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

}
