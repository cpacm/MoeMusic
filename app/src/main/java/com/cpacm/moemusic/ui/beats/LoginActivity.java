package com.cpacm.moemusic.ui.beats;

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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cpacm.moemusic.R;
import com.cpacm.moemusic.utils.DrawableUtil;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userLayout;
    private TextInputEditText userEditText;
    private TextInputLayout pwdLayout;
    private TextInputEditText pwdEditText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        userLayout = (TextInputLayout) findViewById(R.id.user_editlayout);
        userEditText = (TextInputEditText) findViewById(R.id.user_edittext);
        pwdLayout = (TextInputLayout) findViewById(R.id.password_editlayout);
        pwdEditText = (TextInputEditText) findViewById(R.id.password_edittext);

        Drawable[] ds = userEditText.getCompoundDrawables();
        int r = ds[0].getBounds().right;
        int b = ds[0].getBounds().bottom;
        Drawable warpDrawable = DrawableUtil.tintDrawable(ds[0], getResources().getColorStateList(R.color.login_icon_colors));
        warpDrawable.setBounds(0,0,r,b);
        userEditText.setCompoundDrawables(warpDrawable, ds[1], ds[2], ds[3]);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
