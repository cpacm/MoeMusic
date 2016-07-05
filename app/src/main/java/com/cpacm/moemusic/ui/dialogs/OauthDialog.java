package com.cpacm.moemusic.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.presenter.LoginPresenter;
import com.cpacm.moemusic.ui.login.WebAppBridge;
import com.cpacm.moemusic.utils.FileManager;


/**
 * @author: cpacm
 * @date: 2016/7/4
 * @desciption: oauth验证对话框
 */
public class OauthDialog extends DialogFragment {

    private final String JSAPP = "oauth";

    public static OauthDialog create() {
        OauthDialog dialog = new OauthDialog();
        return dialog;
    }

    private WebView webView;
    private ProgressBar progressBar;
    private String account, password;
    private LoginPresenter loginPresenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View customView;
        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_oauth, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.login)
                .customView(customView, false)
                .build();
        webView = (WebView) customView.findViewById(R.id.webview);
        initWebView();
        progressBar = (ProgressBar) customView.findViewById(android.R.id.progress);
        setupProgress(progressBar);
        return dialog;
    }

    private void setupProgress(final ProgressBar progressBar) {
        if (progressBar == null) return;
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        progressBar.setMax(0);
    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webView.addJavascriptInterface(new WebAppBridge(new WebAppBridge.OauthLoginImpl() {
                    @Override
                    public void getResult(String s) {
                        loginPresenter.LoginFailed(s);
                        dismiss();
                    }
                }),
                JSAPP);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("http://api.moefou.org/oauth/authorize")) {
                    webView.loadUrl("javascript:" + getAssetsJs("autologin.js"));
                    webView.loadUrl("javascript:adduplistener()");
                    String js = "javascript:autologin('" + account + "','" + password + "')";
                    webView.loadUrl(js);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String v = "";
                String[] ss = url.split("&");
                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].contains("oauth_verifier=")) {
                        String verifier = ss[i].substring(ss[i].indexOf("=") + 1, ss[i].length());
                        if (verifier != null && !verifier.equals("")) {
                            v = verifier;
                            break;
                        }
                    }
                }
                loginPresenter.getAccessToken(v);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public String getAssetsJs(String filename) {
        return FileManager.getAssets(getActivity(), filename);
    }

    public void setLoginPresenter(LoginPresenter loginPresenter, String account, String password) {
        this.account = account;
        this.password = password;
        this.loginPresenter = loginPresenter;
    }

    public void redirectUrlAndLogin(String url) {
        webView.loadUrl(url);
    }
}
