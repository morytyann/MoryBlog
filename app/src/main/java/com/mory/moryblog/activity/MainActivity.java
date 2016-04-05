package com.mory.moryblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawer;
    private SwipeRefreshLayout srl;
    private RecyclerView rvWeibos;
    private SsoHandler handler;
    private WeiboAdapter rvAdapter;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        rvWeibos = (RecyclerView) findViewById(R.id.rvWeibos);
        // 设置Toolbar
        if (toolbar != null) {
            toolbar.setOnClickListener(this);
        }
        setSupportActionBar(toolbar);
        // 设置浮动按钮的监听
        if (fab != null) {
            fab.setOnClickListener(this);
        }
        // 设置侧边栏菜单点击监听
        if (nav != null) {
            nav.setNavigationItemSelectedListener(this);
        }
        // 初始化RecyclerView
        rvWeibos.setLayoutManager(new LinearLayoutManager(this));
        rvWeibos.setItemAnimator(new DefaultItemAnimator());
        // 下拉刷新的监听
        if (srl != null) {
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Observable.create(new Observable.OnSubscribe<Integer>() {
                        @Override
                        public void call(Subscriber<? super Integer> subscriber) {
                            subscriber.onNext(WeiboBiz.refreshWeibo(MainActivity.this, Constant.LOAD_NEW));
                            subscriber.onCompleted();
                        }
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<Integer>() {
                                @Override
                                public void onCompleted() {
                                    Constant.IS_FRESHING = false;
                                    srl.setRefreshing(false);
                                    Log.d(Constant.TAG, "onCompleted: " + "OK");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Constant.IS_FRESHING = false;
                                    srl.setRefreshing(false);
                                    Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                                    Log.d(Constant.TAG, "onError: " + e.getLocalizedMessage());
                                }

                                @Override
                                public void onNext(Integer integer) {
                                    switch (integer) {
                                        case Constant.FRESH_SUCCESS: // 刷新成功
                                            rvAdapter.notifyDataSetChanged();
                                            break;
                                        case Constant.FRESH_NO_NEW: // 刷新成功但没有新微博
                                            Toast.makeText(MainActivity.this, "没有新微博了", Toast.LENGTH_SHORT).show();
                                            break;
                                        case Constant.FRESH_FAILED: // 刷新失败
                                            Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                                            break;
                                        case Constant.FRESHING: // 正在刷新
                                            Toast.makeText(MainActivity.this, "正在刷新...", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            });
                }
            });
        }
        // 加载更多

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
        Observable.create(new Observable.OnSubscribe<ArrayList<Weibo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Weibo>> subscriber) {
                subscriber.onNext(WeiboBiz.loadWeibo(MainActivity.this));
                subscriber.onCompleted();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<ArrayList<Weibo>>() {
                    @Override
                    public void call(ArrayList<Weibo> weibos) {
                        if (Constant.weibos == null) {
                            Constant.weibos = weibos;
                        } else {
                            Constant.weibos.clear();
                            Constant.weibos.addAll(weibos);
                        }
                        MainActivity.this.rvAdapter = new WeiboAdapter(MainActivity.this, Constant.weibos, R.layout.list_item_weibo);
                        MainActivity.this.rvWeibos.setAdapter(MainActivity.this.rvAdapter);
                    }
                });
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
            case R.id.toolbarMain:
                rvWeibos.getLayoutManager().scrollToPosition(0);
                break;
        }
    }
}
