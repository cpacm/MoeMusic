package com.cpacm.moemusic.ui.beats;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cpacm.moemusic.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayout = (TextInputLayout) findViewById(R.id.user_editlayout);
        textInputEditText = (TextInputEditText) findViewById(R.id.user_edittext);
        textInputLayout.setHint(getString(R.string.app_name));

    }

}
