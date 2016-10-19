package com.cpacm.moemusic.permission;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/9
 * @desciption: 拒绝权限
 */

public interface OnPermissionsDeniedListener {
    void onPermissionsDenied(PermissionBuilder builder, List<String> perms);
}
