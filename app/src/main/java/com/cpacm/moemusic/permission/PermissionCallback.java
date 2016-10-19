package com.cpacm.moemusic.permission;

import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/9
 * @desciption: 权限回调接口
 */

public interface PermissionCallback extends ActivityCompat.OnRequestPermissionsResultCallback {


    /**
     * 被允许的权限列表
     *
     * @param builder
     * @param perms
     */
    void onPermissionsGranted(PermissionBuilder builder, List<String> perms);


    /**
     * 被拒绝的权限列表
     *
     * @param builder
     * @param perms
     */
    void onPermissionsDenied(PermissionBuilder builder, List<String> perms);

}
