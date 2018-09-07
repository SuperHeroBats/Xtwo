package com.xtwo.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private String url = "http://mp.weixin.qq.com/mp/homepage?__biz=MzAxOTg3MDQxNQ==&hid=2&sn=8609dfd9cf67c19b5e9b93f12c633928&scene=18#wechat_redirect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //X5设置键盘 防止遮住输入光标
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //设置Toolbar导航键
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //显示网页
        webView = findViewById(R.id.web_view);
        initWebView();
    }

    //初始化webView
    private void initWebView(){
        webView.loadUrl(url);

        WebSettings settings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            /*
            可以设定加载开始操作onPageStarted及结束的onPageFinished
            更多方法看demo
             */

        });

        //网页的各种属性设置
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);   //图片调整webView大小

        //设置加载图片
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("utf-8");   //避免乱码
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setNeedInitialFocus(false);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);   //适应屏幕
        settings.setLoadsImagesAutomatically(true);   //自动加载图片
        settings.setCacheMode(WebSettings.LOAD_DEFAULT | WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }

    //可返回网页
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    //主页返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //状态的处理
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        //同时销毁webView 如果不执行这个操作 会出现退出应用视频仍在播放
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

}
