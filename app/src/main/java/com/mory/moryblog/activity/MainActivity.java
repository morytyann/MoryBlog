package com.mory.moryblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mory.moryblog.R;
import com.mory.moryblog.adapter.WeiboAdapter;
import com.mory.moryblog.biz.WeiboBiz;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.listener.MyAuthListener;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawer;
    private SwipeRefreshLayout srl;
    private RecyclerView rvWeibos;
    private ArrayList<Weibo> weibos;
    private SsoHandler handler;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    srl.setRefreshing(false);
                    break;
                case Constant.FAILED:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        if (SettingKeeper.readAccessToken(this).getToken().equals("")) {
            oAuth();
        } else {
            Log.d(Constant.TAG, "onCreate: " + SettingKeeper.readAccessToken(this).getToken());
            initWeiboList();
        }
    }

    /**
     * 初始化界面
     */
    private void setViews() {
        // findViewById
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        rvWeibos = (RecyclerView) findViewById(R.id.rvWeibos);
        // 设置Toolbar
        setSupportActionBar(toolbar);
        // 设置浮动按钮的监听
        if (fab != null) {
            fab.setOnClickListener(this);
        }
        // 设置侧边栏菜单点击监听
        if (nav != null) {
            nav.setNavigationItemSelectedListener(this);
        }
        // 下拉刷新的监听
        if (srl != null) {
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    WeiboBiz.loadWeibo(MainActivity.this, weibos, Constant.LOAD_NEW);
                }
            });
        }
        // 设置侧边栏打开关闭旋钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 登录微博获得授权Token
     */
    private void oAuth() {
        AuthInfo authInfo = new AuthInfo(this, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
        handler = new SsoHandler(this, authInfo);
        handler.authorize(new MyAuthListener(this));
    }

    /**
     * 初始化微博列表
     */
    public void initWeiboList() {
        AsyncTaskLoader<ArrayList<Weibo>> loader = new AsyncTaskLoader<ArrayList<Weibo>>(this) {
            @Override
            public ArrayList<Weibo> loadInBackground() {
                weibos = WeiboBiz.loadWeibo(MainActivity.this);
                return null;
            }
        };
        loader.registerListener(0, new Loader.OnLoadCompleteListener<ArrayList<Weibo>>() {
            @Override
            public void onLoadComplete(Loader<ArrayList<Weibo>> loader, ArrayList<Weibo> data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weibos != null) {
                            rvWeibos.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            rvWeibos.setItemAnimator(new DefaultItemAnimator());
                            rvWeibos.setAdapter(new WeiboAdapter(MainActivity.this, weibos, R.layout.list_item_weibo));
                        } else {
                            Toast.makeText(MainActivity.this, "微博列表加载异常，请尝试重启APP。", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loader.forceLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (handler != null) {
            handler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 侧滑菜单打开时先关闭侧滑菜单
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                break;
        }
    }
}
