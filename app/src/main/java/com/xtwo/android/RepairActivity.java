package com.xtwo.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.support.v7.widget.Toolbar;

public class RepairActivity extends AppCompatActivity {

    private WebView repairWeb;
    private String url = "https://jinshuju.net/f/0hunGK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        //X5设置键盘 防止遮住输入光标
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //设置Toolbar导航键
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        repairWeb = findViewById(R.id.repair_web);
        initWebView();

    }

    //初始化webView
    private void initWebView(){
        repairWeb.loadUrl(url);

        WebSettings settings = repairWeb.getSettings();
        repairWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            /*
            可以设定加载开始操作onPageStarted及结束的onPageFinished
            更多方法看demo
             */

        });

        //网页的各种属性设置
        settings.setJavaScriptEnabled(true);   //开启JavaScript脚本
        repairWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);   //滚动条设置
        settings.setDefaultTextEncodingName("utf-8");   //避免乱码
        settings.setNeedInitialFocus(false);   //设置节点获取焦点
        settings.setSupportZoom(true);   //支持缩放
        settings.setUseWideViewPort(true);   //图片调整webView大小
        settings.setLoadWithOverviewMode(true);   //适应屏幕
        settings.setLoadsImagesAutomatically(true);   //自动加载图片
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);   //缓存模式

    }

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
        if (repairWeb != null) {
            repairWeb.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (repairWeb != null) {
            repairWeb.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        //同时销毁webView 如果不执行这个操作 会出现退出应用视频仍在播放
        if (repairWeb != null) {
            repairWeb.destroy();
            repairWeb = null;
        }
        super.onDestroy();
    }

}
