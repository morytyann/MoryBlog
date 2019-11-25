package com.mory.moryblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawer;
    private SwipeRefreshLayout srl;
    private RecyclerView rvWeibos;
    private SsoHandler ssoHandler;
    private WeiboAdapter rvAdapter;
    private LinearLayoutManager manager;

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
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        FloatingActionButton fab = findViewById(R.id.fab);
        NavigationView nav = findViewById(R.id.nav_view);
        srl = findViewById(R.id.srl);
        drawer = findViewById(R.id.drawer_layout);
        rvWeibos = findViewById(R.id.rvWeibos);
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
        manager = new LinearLayoutManager(this);
        rvWeibos.setLayoutManager(manager);
        rvWeibos.setItemAnimator(new DefaultItemAnimator());
        // 下拉刷新的监听
        if (srl != null) {
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRefreshLoadNew();
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
        ssoHandler = new SsoHandler(this, authInfo);
        ssoHandler.authorize(new MyAuthListener(this));
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
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<ArrayList<Weibo>>() {
            @Override
            public void onCompleted() {
                Constant.IS_FRESHING = false;
                MainActivity.this.rvAdapter = new WeiboAdapter(MainActivity.this, manager, srl, Constant.weibos, R.layout.list_item_weibo);
                MainActivity.this.rvWeibos.setAdapter(MainActivity.this.rvAdapter);
            }

            @Override
            public void onError(Throwable e) {
                Constant.IS_FRESHING = false;
                Toast.makeText(MainActivity.this, "出错啦，错误代码:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ArrayList<Weibo> weibos) {
                Constant.weibos = new ArrayList<>();
                Constant.weibos.addAll(weibos);
            }
        });
    }

    /**
     * 完成加载新微博的业务
     */
    private void doRefreshLoadNew() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(WeiboBiz.refreshWeibo(MainActivity.this, Constant.TYPE_LOAD_NEW));
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Constant.IS_FRESHING = false;
                srl.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Constant.IS_FRESHING = false;
                srl.setRefreshing(false);
                Toast.makeText(MainActivity.this, "出错啦，错误信息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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

    @SuppressWarnings("StatementWithEmptyBody")
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
                Snackbar.make(v, "HelloWorld", Snackbar.LENGTH_SHORT).show();
                break;
            // 如果当前位置不是最上方则滚动到最上方，是的话加载新微博
            case R.id.toolbarMain:
                if (manager.findFirstCompletelyVisibleItemPosition() == 0) {
                    srl.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doRefreshLoadNew();
                        }
                    }, 1000);
                    return;
                }
                manager.smoothScrollToPosition(rvWeibos, null, 0);
                break;
        }
    }
}
