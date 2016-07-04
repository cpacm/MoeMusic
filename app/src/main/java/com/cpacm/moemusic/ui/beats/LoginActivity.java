package com.cpacm.moemusic.ui.beats;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cpacm.core.http.HttpUtil;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.web.RegisterActivity;
import com.cpacm.moemusic.utils.DrawableUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout userLayout;
    private TextInputEditText userEditText;
    private TextInputLayout pwdLayout;
    private TextInputEditText pwdEditText;
    private Button loginBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        initEditText();
    }

    private void initEditText() {
        userLayout = (TextInputLayout) findViewById(R.id.user_editlayout);
        userEditText = (TextInputEditText) findViewById(R.id.user_edittext);
        pwdLayout = (TextInputLayout) findViewById(R.id.password_editlayout);
        pwdEditText = (TextInputEditText) findViewById(R.id.password_edittext);
        //user
        Drawable[] uds = userEditText.getCompoundDrawables();
        Drawable warpDrawable = DrawableUtil.tintDrawable(uds[0], getResources().getColorStateList(R.color.login_icon_colors));
        userEditText.setCompoundDrawables(warpDrawable, uds[1], uds[2], uds[3]);

        //pwd
        Drawable[] pds = pwdEditText.getCompoundDrawables();
        Drawable warp = DrawableUtil.tintDrawable(pds[0], getResources().getColorStateList(R.color.login_icon_colors));
        pwdEditText.setCompoundDrawables(warp, pds[1], pds[2], pds[3]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
        }

    }

    private void login() {
        String user = userEditText.getText().toString();
        if (TextUtils.isEmpty(user)) return;
        String pwd = pwdEditText.getText().toString();
        if (TextUtils.isEmpty(pwd)) return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

}
