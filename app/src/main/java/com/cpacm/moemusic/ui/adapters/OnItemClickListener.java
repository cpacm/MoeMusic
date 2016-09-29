package com.cpacm.moemusic.ui.adapters;

import android.view.View;

import com.cpacm.core.bean.Song;

/**
 * @author: cpacm
 * @date: 2016/9/8
 * @desciption: 列表点击接口
 */
public interface OnItemClickListener<T> {

    void onItemClick(T item, int position);

    void onItemSettingClick(View v, T item, int position);
}
