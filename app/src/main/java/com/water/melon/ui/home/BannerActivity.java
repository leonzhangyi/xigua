package com.water.melon.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;

import androidx.annotation.Nullable;

public class BannerActivity extends BaseActivity {
    public final static String M_URL = "m_url";

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_banner;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {

        //获得控件
        WebView webView = (WebView) findViewById(R.id.wv_webview);
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(this.getDir("appcache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //设置UA
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " frog");
        //移除部分系统JavaScript接口
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);

        //访问网页
        webView.loadUrl(getIntent().getStringExtra(M_URL));
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }
}