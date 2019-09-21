package cn.paulpaulzhang.fair.sc.main.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.common
 * 创建时间：9/21/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class WebActivity extends FairActivity {


    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @BindView(R2.id.web_view)
    WebView mWebView;

    @BindView(R2.id.progress_bar)
    ProgressBar mProgressBar;


    @Override
    public int setLayout() {
        return R.layout.activity_web;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(mWebChromeClient);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage(message)
                    .setPositiveButton("确定", null)
                    .setCancelable(false)
                    .create()
                    .show();

            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            mToolbar.setTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress < 100 && newProgress >= 0) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
            mProgressBar.setProgress(newProgress);
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
