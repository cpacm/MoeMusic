package com.cpacm.moemusic.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.views.LoginIView;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.beats.BeatsActivity;
import com.cpacm.moemusic.ui.widgets.dialogs.OauthDialog;
import com.cpacm.moemusic.utils.DrawableUtil;

/**
 * 登录界面
 */
public class LoginActivity extends AbstractAppActivity implements View.OnClickListener, LoginIView {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private TextInputLayout userLayout;
    private TextInputEditText userEditText;
    private TextInputLayout pwdLayout;
    private TextInputEditText pwdEditText;
    private Button loginBtn;
    private TextView loginTv;
    private Toolbar toolbar;
    private OauthDialog oauthDialog;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenter(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        View shadowView = findViewById(R.id.toolbar_shadow);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            shadowView.setVisibility(View.VISIBLE);
        }

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        loginTv = (TextView) findViewById(R.id.login_oauth);
        loginTv.setOnClickListener(this);
        initEditText();
    }

    private void initEditText() {
        userLayout = (TextInputLayout) findViewById(R.id.user_editlayout);
        userEditText = (TextInputEditText) findViewById(R.id.user_edittext);
        pwdLayout = (TextInputLayout) findViewById(R.id.password_editlayout);
        pwdEditText = (TextInputEditText) findViewById(R.id.password_edittext);
        //user
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Drawable[] uds = userEditText.getCompoundDrawablesRelative();
            Drawable warpDrawable = DrawableUtil.tintDrawable(uds[0], getResources().getColorStateList(R.color.login_icon_colors));
            userEditText.setCompoundDrawablesRelative(warpDrawable, uds[1], uds[2], uds[3]);
        } else {
            Drawable[] uds = userEditText.getCompoundDrawables();
            Drawable warpDrawable = DrawableUtil.tintDrawable(uds[0], getResources().getColorStateList(R.color.login_icon_colors));
            userEditText.setCompoundDrawables(warpDrawable, uds[1], uds[2], uds[3]);
        }
        //pwd
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Drawable[] pds = pwdEditText.getCompoundDrawablesRelative();
            Drawable warp = DrawableUtil.tintDrawable(pds[0], getResources().getColorStateList(R.color.login_icon_colors));
            pwdEditText.setCompoundDrawablesRelative(warp, pds[1], pds[2], pds[3]);
        } else {
            Drawable[] pds = pwdEditText.getCompoundDrawables();
            Drawable warp = DrawableUtil.tintDrawable(pds[0], getResources().getColorStateList(R.color.login_icon_colors));
            pwdEditText.setCompoundDrawables(warp, pds[1], pds[2], pds[3]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.login_oauth:
                loginOauth();
                break;
        }
    }

    private void loginOauth() {
        oauthDialog = OauthDialog.create(true);
        oauthDialog.setLoginPresenter(loginPresenter);
        oauthDialog.show(getFragmentManager(), getString(R.string.login));
        loginPresenter.login();
    }

    private void login() {
        String user = userEditText.getText().toString();
        if (TextUtils.isEmpty(user)) {
            showSnackBar(userLayout,R.string.account_empty);
            return;
        }
        String pwd = pwdEditText.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            showSnackBar(userLayout,R.string.password_empty);
            return;
        }
        showOauthDialog(user, pwd);
        loginPresenter.login();
    }

    private void showOauthDialog(String account, String password) {
        oauthDialog = OauthDialog.create();
        oauthDialog.setLoginPresenter(loginPresenter, account, password);
        oauthDialog.show(getFragmentManager(), getString(R.string.login));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_register) {
            Intent i = new Intent();
            i.setClass(this, RegisterActivity.class);
            i.putExtra("url", HttpUtil.REGISTER_URL);
            i.putExtra("title", getString(R.string.menu_register));
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OauthRedirect(String url) {
        oauthDialog.redirectUrlAndLogin(url);
    }

    @Override
    public void LoginSuccess() {
        oauthDialog.dismiss();
        startActivity(BeatsActivity.class);
        finish();
    }

    @Override
    public void LoginFailed() {
        oauthDialog.dismiss();
        showSnackBar(userLayout,R.string.login_fail);
    }

    @Override
    public void LoginFailed(String s) {
        oauthDialog.dismiss();
        showSnackBar(userLayout,s);
    }
}
