package com.cpacm.moemusic.ui.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.core.mvp.views.SettingIView;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.account.LoginActivity;

/**
 * @author: cpacm
 * @date: 2016/10/21
 * @desciption: 设置界面
 */

public class SettingActivity extends AbstractAppActivity implements View.OnClickListener, SettingIView {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private View notifyLayout, updateLayout, cacheLayout, wifiLayout;
    private Button logoutBtn;
    private CheckBox notifyCb, wifiCb;
    private MaterialDialog updateDialog;
    private MaterialDialog loadingDialog;

    private SettingPresenter settingPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingPresenter = new SettingPresenter(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        settingPresenter.initSetting();
    }

    private void initView() {
        notifyLayout = findViewById(R.id.notify_layout);
        updateLayout = findViewById(R.id.update_layout);
        cacheLayout = findViewById(R.id.cache_layout);
        wifiLayout = findViewById(R.id.wifi_layout);
        notifyCb = (CheckBox) findViewById(R.id.notify_checkbox);
        wifiCb = (CheckBox) findViewById(R.id.wifi_checkbox);
        logoutBtn = (Button) findViewById(R.id.logout);
        notifyLayout.setOnClickListener(this);
        updateLayout.setOnClickListener(this);
        cacheLayout.setOnClickListener(this);
        wifiLayout.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_layout:
                notifyCb.setChecked(!notifyCb.isChecked());
                settingPresenter.setNotify(notifyCb.isChecked());
                break;
            case R.id.update_layout:
                loadingDialog = new MaterialDialog.Builder(this)
                        .title(R.string.setting_update_loading)
                        .progress(true, 0)
                        .progressIndeterminateStyle(true)
                        .show();
                settingPresenter.update();
                break;
            case R.id.cache_layout:
                FileUtils.cleanCacheDir();
                showSnackBar(R.string.setting_cache_clean);
                break;
            case R.id.wifi_layout:
                wifiCb.setChecked(!wifiCb.isChecked());
                settingPresenter.setWifi(wifiCb.isChecked());
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    private void logout() {
        new MaterialDialog.Builder(this)
                .title(R.string.setting_logout)
                .content(R.string.setting_logout_tip)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        settingPresenter.logout();
                        LoginActivity.open(SettingActivity.this);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void setting(boolean notify, boolean wifi) {
        notifyCb.setChecked(notify);
        wifiCb.setChecked(wifi);
    }

    @Override
    public void updateApk(boolean update, final String apkUrl, final String description) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        if (update) {
            new MaterialDialog.Builder(this)
                    .title(R.string.setting_update_title)
                    .content(description)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showUpdateDialog(apkUrl, description);
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            showSnackBar(R.string.setting_update_new);
        }
    }

    @Override
    public void downloadProgress(int progress) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.setProgress(progress);
            if (progress == 100) {
                updateDialog.dismiss();
            }
        }
    }

    /**
     * 显示版本更新对话框
     *
     * @param apkUrl
     */
    private void showUpdateDialog(final String apkUrl, String description) {
        updateDialog = new MaterialDialog.Builder(this)
                .title(R.string.setting_update_title)
                .content(description)
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 100, false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        settingPresenter.apkDownload(apkUrl);
                    }
                }).show();
    }
}
