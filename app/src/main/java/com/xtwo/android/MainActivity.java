package com.xtwo.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xtwo.android.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //获取天气及定位
//    private TextView positionText;
//    private LocationClient mLocationClient;

    //实例化DrawerLayout
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private long exitTime = 0;   //退出程序

    //创建图片数组
    private Memory[] memories = {
            new Memory("第三届师兄师姐们", R.drawable.img1), new Memory("最开心就是搞服务日", R.drawable.img2),
            new Memory("最开心就是搞服务日", R.drawable.img3), new Memory("最开心就是搞服务日", R.drawable.img4),
            new Memory("最开心就是搞服务日", R.drawable.img5), new Memory("最开心就是搞服务日", R.drawable.img6),
            new Memory("社团周年庆", R.drawable.img7), new Memory("社团周年庆", R.drawable.img8),
            new Memory("社团周年庆", R.drawable.img9), new Memory("第三届师兄师姐们", R.drawable.img10),
            new Memory("第三届师兄师姐们", R.drawable.img11), new Memory("社团周年庆", R.drawable.img12),
            new Memory("社团周年庆", R.drawable.img13), new Memory("红红火火恍恍惚惚", R.drawable.img14),
            new Memory("红红火火恍恍惚惚", R.drawable.img15), new Memory("红红火火恍恍惚惚", R.drawable.img16),
            new Memory("红红火火恍恍惚惚", R.drawable.img17), new Memory("换届了喂", R.drawable.img18),
            new Memory("换届了喂", R.drawable.img19), new Memory("换届了喂", R.drawable.img20),
            new Memory("换届了喂", R.drawable.img21), new Memory("换届了喂", R.drawable.img22),
            new Memory("换届了喂", R.drawable.img23), new Memory("换届了喂", R.drawable.img24),
            new Memory("换届了喂", R.drawable.img25), new Memory("换届了喂", R.drawable.img26),
            new Memory("换届了喂", R.drawable.img27), new Memory("换届了喂", R.drawable.img28),
            new Memory("换届了喂", R.drawable.img29), new Memory("优秀", R.drawable.img30),
            new Memory("优秀", R.drawable.img31), new Memory("红红火火恍恍惚惚", R.drawable.img32),
            new Memory("红红火火恍恍惚惚", R.drawable.img33), new Memory("红红火火恍恍惚惚", R.drawable.img34),
            new Memory("红红火火恍恍惚惚", R.drawable.img35), new Memory("红红火火恍恍惚惚", R.drawable.img36),
            new Memory("红红火火恍恍惚惚", R.drawable.img37), new Memory("最开心就是搞服务日", R.drawable.img38),
            new Memory("最开心就是搞服务日", R.drawable.img39), new Memory("最开心就是搞服务日", R.drawable.img40),
            new Memory("最开心就是搞服务日", R.drawable.img41), new Memory("红红火火恍恍惚惚", R.drawable.img42),
            new Memory("最开心就是搞服务日", R.drawable.img43), new Memory("红红火火恍恍惚惚", R.drawable.img44),
            new Memory("社团周年庆", R.drawable.img45), new Memory("社团周年庆", R.drawable.img46),
            new Memory("社团周年庆", R.drawable.img47), new Memory("啦啦啦啦", R.drawable.img48),
            new Memory("优秀执行团队", R.drawable.h49), new Memory("专注的人最帅", R.drawable.h50),
            new Memory("红红火火恍恍惚惚", R.drawable.img51), new Memory("红红火火恍恍惚惚", R.drawable.img52),
            new Memory("红红火火恍恍惚惚", R.drawable.img53), new Memory("红红火火恍恍惚惚", R.drawable.img54),
            new Memory("红红火火恍恍惚惚", R.drawable.img55)};

    private List<Memory> memoryList = new ArrayList<>();
    private MemoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //实例化LocationClient并注册
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());

        setContentView(R.layout.activity_main);

//        positionText = findViewById(R.id.position);
        //判断是否有权限 加入权限组
//        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!permissionList.isEmpty()) {
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
//        } else {
//            requestLocation();
//        }

        //刷新数据
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMemories();
            }
        });

        //加载RecyclerView 初始化
        initMemories();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);   //设置布局方式 一行两列
        recyclerView.setLayoutManager(layoutManager);
        //适配器是传入适配器的构造方法的参数
        adapter = new MemoryAdapter(memoryList);
        recyclerView.setAdapter(adapter);

        //实例化NavigationView 指定默认选中
        final NavigationView navView = findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_photo);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //选中photo 关闭滑动菜单直接显示照片墙
                    case R.id.nav_photo:   //照片墙
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_we:   //We软小二
//                        ToastUtil.showShortToast("主功能开发中");
                        Intent webIntent = new Intent(MainActivity.this, WebActivity.class);
                        startActivity(webIntent);
                        break;
                    case R.id.nav_friends:   //朋友
                        ToastUtil.showShortToast("功能待开发");
                        break;
                    case R.id.nav_about:   //关于
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case R.id.nav_update:   //检查版本
                        ToastUtil.showShortToast("当前版本为最新V1.1");
                        break;
                    case R.id.nav_build:   //智能报修
                        Intent buildIntent = new Intent(MainActivity.this, RepairActivity.class);
                        startActivity(buildIntent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //实例化DrawerLayout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //实例化Toolbar设置
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置导航键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.logo_ico);
        }

        //退出登录
        TextView exit = findViewById(R.id.exit_text);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                ToastUtil.showShortToast("成功退出");
                finish();
            }
        });
    }

    //请求定位
//    private void requestLocation(){
//        initLocation();
//        mLocationClient.start();
//    }

    //设置定位其他选项
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setScanSpan(5000);   //设置定时更新位置
//        option.setIsNeedAddress(true);
//        mLocationClient.setLocOption(option);
//    }

    //记得销毁定位活动 耗电
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocationClient.stop();
    }

    //申请权限
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int result : grantResults) {
//                        if (result != PackageManager.PERMISSION_GRANTED) {
//                            ToastUtil.showShortToast("必须同意全部权限才可以使用定位功能");
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();
//                }else {
//                    ToastUtil.showShortToast("发生未知错误");
//                    finish();
//                }
//                break;
//                default:
//        }
//    }

    //退出程序
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showShortToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        }else {
            super.onBackPressed();
        }
    }

    //刷新数据
    private void refreshMemories(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initMemories();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //初始化数据方法
    private void initMemories(){
        memoryList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            //取值范围就是这个数组的长度 然后随机选取 添加到List中
            int index = random.nextInt(memories.length);
            memoryList.add(memories[index]);
        }
    }

    //设置菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;   //显示菜单项
    }

    //不是为了看的
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);   //统一方向
                break;
            case R.id.apps:
                ToastUtil.showShortToast("Developer by SuperHeroBats");
                break;
            default:
        }
        return true;
    }

    private static final String TAG = "MainActivity";

    //获取定位信息
//    public class MyLocationListener extends BDAbstractLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//
//            String districtName = location.getDistrict();
//            Log.d(TAG, districtName);
////            positionText.setText(districtName);
//        }
//    }

}
