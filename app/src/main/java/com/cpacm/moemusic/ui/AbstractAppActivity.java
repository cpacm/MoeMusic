package com.cpacm.moemusic.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @Auther: cpacm
 * @Date: 2016/6/25.
 * @description: 所有Activity的父类
 */
public abstract class AbstractAppActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


}
