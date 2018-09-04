package com.xtwo.android;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.xtwo.android.util.ToastUtil;

public class MemoryActivity extends AppCompatActivity {

    public static final String MEMORY_NAME = "memory_name";
    public static final String MEMORY_IMAGE_ID = "memory_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_activity);

        //和状态栏融合
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        //悬浮按钮点击事件
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortToast("功能待开发");
            }
        });

        Intent intent = getIntent();
        String memoryName = intent.getStringExtra(MEMORY_NAME);
        int memoryImageId = intent.getIntExtra(MEMORY_IMAGE_ID, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView memoryImageView = findViewById(R.id.memory_image_view);
        TextView memoryContentText = findViewById(R.id.memory_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(memoryName);
        Glide.with(this).load(memoryImageId).into(memoryImageView);
        String memoryContent = generateMemoryContent();
        memoryContentText.setText(memoryContent);

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

    //填充内容
    private String generateMemoryContent() {
        StringBuilder memoryContent = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            memoryContent.append("强行霸屏给你们体验一下上下滑动的刺激刺激，");
        }
        return memoryContent.toString();
    }

}
