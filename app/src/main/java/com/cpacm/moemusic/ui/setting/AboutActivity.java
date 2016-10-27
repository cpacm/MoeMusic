package com.cpacm.moemusic.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cpacm.core.utils.SystemParamsUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;

import net.cpacm.library.SimpleSliderLayout;
import net.cpacm.library.indicator.SpringIndicator.SpringIndicator;
import net.cpacm.library.transformers.RotateDownTransformer;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * @author: cpacm
 * @date: 2016/10/21
 * @desciption: 关于界面
 */

public class AboutActivity extends AbstractAppActivity {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AboutActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private SimpleSliderLayout simpleSliderLayout;
    private SpringIndicator springIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSlider();
    }

    private void initSlider() {
        simpleSliderLayout = (SimpleSliderLayout) findViewById(R.id.simple_slider);
        simpleSliderLayout.setClipChildren(false);
        simpleSliderLayout.setAutoCycling(false);
        simpleSliderLayout.setPageTransformer(new RotateDownTransformer());

        springIndicator = (SpringIndicator) findViewById(R.id.spring_indicator);

        AboutSliderView aboutSliderView = new AboutSliderView(this);
        aboutSliderView.setAboutImg(R.drawable.beats);
        aboutSliderView.setDescription(R.string.about_app);
        aboutSliderView.setTitle(R.string.app_name);
        aboutSliderView.setPageTitle(getString(R.string.about_name1));
        simpleSliderLayout.addSlider(aboutSliderView);

        AboutSliderView aboutSliderView2 = new AboutSliderView(this);
        aboutSliderView2.setAboutImg(R.drawable.author);
        aboutSliderView2.setDescription(R.string.about_me);
        aboutSliderView2.setTitle(R.string.about_author);
        aboutSliderView2.setPageTitle(getString(R.string.about_name2));
        simpleSliderLayout.addSlider(aboutSliderView2);

        simpleSliderLayout.setViewPagerIndicator(springIndicator);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
