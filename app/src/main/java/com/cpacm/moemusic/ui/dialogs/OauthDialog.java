package com.cpacm.moemusic.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

/**
 * @author: cpacm
 * @date: 2016/7/4
 * @desciption: oauth验证对话框
 */
public class OauthDialog extends Dialog {

    public OauthDialog(Context context) {
        super(context);
    }

    public OauthDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected OauthDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
