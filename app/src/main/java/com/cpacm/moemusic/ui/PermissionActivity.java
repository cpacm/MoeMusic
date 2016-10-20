package com.cpacm.moemusic.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.SparseArray;

import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SongManager;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.permission.OnPermissionsDeniedListener;
import com.cpacm.moemusic.permission.OnPermissionsGrantedListener;
import com.cpacm.moemusic.permission.PermissionBuilder;
import com.cpacm.moemusic.permission.PermissionManager;

import java.util.List;


/**
 * @author: cpacm
 * @date: 2016/10/19
 * @desciption: 为 6.0 添加权限管理
 */

public abstract class PermissionActivity extends AbstractAppActivity {

    private SparseArray<PermissionBuilder> builderMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builderMap = new SparseArray<>();
    }

    protected void request(PermissionBuilder builder) {
        builderMap.put(builder.requestCode, builder);
        builder.request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionBuilder permissionBuilder = builderMap.get(requestCode);
        permissionBuilder.onRequestPermissionsResult(permissions, grantResults);
    }

    protected PermissionBuilder createPermissionBuilderAndRequest(int requestCode,
                                                                  @StringRes int rationale,
                                                                  @StringRes int rationaleAgain,
                                                                  OnPermissionsGrantedListener onPermissionsGrantedListener,
                                                                  OnPermissionsDeniedListener onPermissionsDeniedListener,
                                                                  DialogInterface.OnClickListener negativeButtonOnClickListener,
                                                                  String... perms) {
        PermissionBuilder builder = new PermissionBuilder()
                .withContext(this)
                .setRequestCode(requestCode)
                .setOk(android.R.string.ok)
                .setCancel(android.R.string.cancel)
                .setSetting(R.string.setting)
                .setRationale(getString(rationale))
                .setAskAgainRationale(getString(rationaleAgain))
                .askAgain(true)
                .setOnPermissionsGrantedListener(onPermissionsGrantedListener)
                .setOnPermissionsDeniedListener(onPermissionsDeniedListener)
                .setNegativeButtonOnClickListener(negativeButtonOnClickListener)
                .setPerms(perms);
        request(builder);
        return builder;
    }

    /**
     * 下载歌曲
     *
     * @param song
     */
    protected void downloadSong(final Song song) {
        createPermissionBuilderAndRequest(PermissionManager.PERMISSION_STORAGE_CODE,
                R.string.permission_storage_rationale,
                R.string.permission_storage_rationale_again,
                new OnPermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(PermissionBuilder builder, List<String> perms) {
                        showSnackBar(getString(R.string.song_add_download));
                        SongManager.getInstance().download(song);
                    }
                },
                new OnPermissionsDeniedListener() {
                    @Override
                    public void onPermissionsDenied(PermissionBuilder builder, List<String> perms) {
                        showSnackBar(getString(R.string.permission_storage_denied));
                    }
                },
                null,
                PermissionManager.PERMISSION_STORAGE
        );
    }

}
