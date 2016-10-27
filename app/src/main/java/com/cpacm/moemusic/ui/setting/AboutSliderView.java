package com.cpacm.moemusic.ui.setting;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpacm.moemusic.R;

import net.cpacm.library.slider.BaseSliderView;

/**
 * @author: cpacm
 * @date: 2016/10/25
 * @desciption: 关于界面的轮播图
 */

public class AboutSliderView extends BaseSliderView {

    private ImageView aboutImg;
    private TextView title, description;

    protected AboutSliderView(Context context) {
        super(context);
    }

    @Override
    public View getSliderView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_about_card, null);
        aboutImg = (ImageView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        bindSliderToPager(view);
        return view;
    }

    public void setAboutImg(@DrawableRes int resId) {
        aboutImg.setImageResource(resId);
    }

    public void setTitle(@StringRes int resId) {
        title.setText(resId);
    }

    public void setDescription(@StringRes int resId) {
        description.setText(resId);
    }

    public void setDescription(Spanned res) {
        description.setText(res);
    }
}
