package com.cpacm.moemusic.ui.web;

import android.os.Bundle;

import com.cpacm.moemusic.ui.AbstractAppActivity;

/**
 * @author: cpacm
 * @date: 2016/7/4
 * @desciption: 浏览器界面，其中包括一定的优化
 */
public class RegisterActivity extends BaseWebActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startUrl();
    }
}
