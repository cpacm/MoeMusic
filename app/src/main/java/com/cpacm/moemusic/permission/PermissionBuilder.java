package com.cpacm.moemusic.permission;

import android.content.DialogInterface;
import android.support.annotation.StringRes;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/19
 * @desciption: 权限构造器
 */

public class PermissionBuilder {
    public Object object;
    public int requestCode;
    public OnPermissionsDeniedListener onPermissionsDeniedListener;
    public OnPermissionsGrantedListener onPermissionsGrantedListener;
    public String rationale;
    public String askAgainRationale;
    public DialogInterface.OnClickListener negativeButtonOnClickListener;
    public String[] perms;
    @StringRes
    public int ok;
    @StringRes
    public int cancel;
    @StringRes
    public int setting;
    public boolean askAgain;

    public PermissionBuilder withContext(Object object) {
        this.object = object;
        return this;
    }

    public PermissionBuilder setOnPermissionsDeniedListener(OnPermissionsDeniedListener onPermissionsDeniedListener) {
        this.onPermissionsDeniedListener = onPermissionsDeniedListener;
        return this;
    }

    public PermissionBuilder setOnPermissionsGrantedListener(OnPermissionsGrantedListener onPermissionsGrantedListener) {
        this.onPermissionsGrantedListener = onPermissionsGrantedListener;
        return this;
    }

    public PermissionBuilder setRationale(String rationale) {
        this.rationale = rationale;
        return this;
    }

    public PermissionBuilder setAskAgainRationale(String askAgainRationale) {
        this.askAgainRationale = askAgainRationale;
        return this;
    }

    public PermissionBuilder setNegativeButtonOnClickListener(DialogInterface.OnClickListener negativeButtonOnClickListener) {
        this.negativeButtonOnClickListener = negativeButtonOnClickListener;
        return this;
    }

    public PermissionBuilder setPerms(String... perms) {
        this.perms = perms;
        return this;
    }

    public PermissionBuilder setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public PermissionBuilder setOk(@StringRes int ok) {
        this.ok = ok;
        return this;
    }

    public PermissionBuilder setCancel(@StringRes int cancel) {
        this.cancel = cancel;
        return this;
    }

    public PermissionBuilder setSetting(@StringRes int setting) {
        this.setting = setting;
        return this;
    }

    public PermissionBuilder askAgain(boolean askAgain) {
        this.askAgain = askAgain;
        return this;
    }

    /**
     * 请求权限
     */
    public void request() {
        PermissionManager.requestPermissions(this);
    }

    /**
     * 请求结果返回
     *
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(String[] permissions, int[] grantResults) {
        PermissionManager.onRequestPermissionsResult(this, permissions, grantResults);
    }

    public void checkDeniedPermissionsNeverAskAgain(List<String> denied) {
        if (askAgain) {
            PermissionManager.checkDeniedPermissionsNeverAskAgain(this, denied);
        }
    }

}
