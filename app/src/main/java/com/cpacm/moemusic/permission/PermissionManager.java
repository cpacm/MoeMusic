package com.cpacm.moemusic.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/9
 * @desciption: Android权限管理器
 */
public class PermissionManager {

    public static final int REQ_PER_CODE = 9527;
    public static final String RATIONAL_TITLE = "获取权限";

    public static final int PERMISSION_STORAGE_CODE = 1;
    public static final int PERMISSION_RECODE_CODE = 2;

    public static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    /**
     * 判断是否有权限，少于api 23时总是返回true
     *
     * @param context
     * @param perms
     * @return
     */
    public static boolean hasPermissions(Context context, String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(final PermissionBuilder builder) {
        Context context = checkCallingObjectSuitability(builder.object);
        if (!hasPermissions(context, builder.perms)) {
            boolean shouldShowRationale = false;
            for (String perm : builder.perms) {
                shouldShowRationale = shouldShowRationale || shouldShowPermissionRationale(builder.object, perm);
            }
            if (!shouldShowRationale) {
                callWithPermissions(builder.object, builder.perms, builder.requestCode);
            } else {
                if (context == null) {
                    return;
                }
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle(RATIONAL_TITLE)
                        .setMessage(builder.rationale)
                        .setPositiveButton(builder.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callWithPermissions(builder.object, builder.perms, builder.requestCode);
                            }
                        })
                        .setNegativeButton(builder.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (builder.onPermissionsDeniedListener != null) {
                                    builder.onPermissionsDeniedListener.onPermissionsDenied(builder, Arrays.asList(builder.perms));
                                }
                            }
                        }).create();
                alertDialog.show();
            }
            return;
        }
        if (builder.onPermissionsGrantedListener != null) {
            builder.onPermissionsGrantedListener.onPermissionsGranted(builder, Arrays.asList(builder.perms));
        }
    }

    /**
     * 对权限进行申请
     *
     * @param object
     * @param perms
     * @param requestCode
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void callWithPermissions(Object object, String[] perms, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    /**
     * 是否需要显示说明对话框
     *
     * @param object
     * @param perm
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    /**
     * 检查是否在activity或fragment中进行权限申请
     *
     * @param object
     */
    private static Context checkCallingObjectSuitability(Object object) {
        // Make sure Object is an Activity or Fragment
        boolean isActivity = object instanceof Activity;
        if (isActivity) {
            return ((Activity) object);
        }
        boolean isSupportFragment = object instanceof Fragment;
        if (isSupportFragment) {
            return ((Fragment) object).getActivity();
        }
        boolean isAppFragment = object instanceof android.app.Fragment;
        boolean isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        if (isAppFragment && isMinSdkM) {
            return ((android.app.Fragment) object).getActivity();
        } else if (isAppFragment) {
            throw new IllegalArgumentException(
                    "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
        }
        throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
    }

    /**
     * 对权限返回的结果进行回调
     *
     * @param builder
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(PermissionBuilder builder, String[] permissions,
                                                  int[] grantResults) {

        checkCallingObjectSuitability(builder.object);

        // Make a collection of granted and denied permissions from the request.
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        // Report denied permissions, if any.
        if (!denied.isEmpty()) {
            if (builder.onPermissionsDeniedListener != null) {
                builder.onPermissionsDeniedListener.onPermissionsDenied(builder, denied);
                builder.checkDeniedPermissionsNeverAskAgain(denied);
            }
        } else {
            if (builder.onPermissionsGrantedListener != null) {
                builder.onPermissionsGrantedListener.onPermissionsGranted(builder, denied);
            }
        }
    }

    @TargetApi(11)
    private static void startAppSettingsScreen(Object object,
                                               Intent intent) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, REQ_PER_CODE);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, REQ_PER_CODE);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, REQ_PER_CODE);
        }
    }

    /**
     * 不再询问
     *
     * @param builder
     * @param deniedPerms
     * @return
     */
    public static boolean checkDeniedPermissionsNeverAskAgain(final PermissionBuilder builder,
                                                              List<String> deniedPerms) {
        boolean shouldShowRationale = false;
        for (String perm : deniedPerms) {
            shouldShowRationale = shouldShowRationale || shouldShowPermissionRationale(builder.object, perm);
        }
        if (!shouldShowRationale) {
            final Context activity = checkCallingObjectSuitability(builder.object);
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage(builder.askAgainRationale)
                    .setPositiveButton(builder.setting, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            startAppSettingsScreen(builder.object, intent);
                        }
                    })
                    .setNegativeButton(builder.cancel, builder.negativeButtonOnClickListener)
                    .create();
            dialog.show();
            return true;
        }
        return false;
    }
}
