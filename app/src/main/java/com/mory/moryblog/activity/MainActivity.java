package com.mory.moryblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.mory.moryblog.App;
import com.mory.moryblog.R;
import com.mory.moryblog.adapter.WeiboAdapter;
import com.mory.moryblog.entity.Timeline;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.mory.moryblog.util.TextUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, WeiboAuthListener {

    private DrawerLayout drawer;
    private SwipeRefreshLayout srl;
    private RecyclerView rvWeibos;
    private SsoHandler ssoHandler;
    private WeiboAdapter rvAdapter;
    private LinearLayoutManager manager;
    private Oauth2AccessToken oauth2AccessToken;
    private List<Weibo> weiboList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        Oauth2AccessToken oauth2AccessToken = SettingKeeper.readAccessToken();
        if ("".equals(oauth2AccessToken.getToken())) {
            oAuth();
        } else {
            this.oauth2AccessToken = oauth2AccessToken;
            loadWeibo(true, false, 0);
        }
    }

    /**
     * 初始化界面
     */
    private void initViews() {
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
                    loadWeibo(false, true, 0);
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
        ssoHandler.authorize(this);
    }

    /**
     * 加载微博方法
     *
     * @param reload    是否重载
     * @param loadNew   是否请求更新的
     * @param weiboType 微博类型 0 全部
     */
    private void loadWeibo(boolean reload, boolean loadNew, int weiboType) {
        String accessToken = oauth2AccessToken.getToken();
        if (weiboList == null) {
            weiboList = new ArrayList<>();
        }
        reload = weiboList.isEmpty() || reload;
        long sinceId = 0L;
        long maxId = 0L;
        if (!reload) {
            if (loadNew) {
                Weibo weibo = weiboList.get(0);
                sinceId = weibo.getId();
            } else {
                Weibo weibo = weiboList.get(weiboList.size() - 1);
                maxId = weibo.getId();
            }
        }
        App.weiboApi.homeTimeline(accessToken, sinceId, maxId, weiboType, 0).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Timeline>() {
            @Override
            public void onCompleted() {
                TextUtil.changeNow();
                MainActivity.this.initRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("MoryBlog", "onError: ", e);
            }

            @Override
            public void onNext(Timeline timeline) {
                MainActivity.this.weiboList.addAll(timeline.getStatuses());
            }
        });
    }

    private void initRecyclerView() {
        rvAdapter = new WeiboAdapter(this, manager, srl, weiboList, R.layout.list_item_weibo);
        rvWeibos.setAdapter(this.rvAdapter);
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
                if (manager.findFirstCompletelyVisibleItemPosition() != 0) {
                    manager.smoothScrollToPosition(rvWeibos, null, 0);
                    return;
                }
                srl.setRefreshing(true);
                loadWeibo(false, true, 0);
                break;
        }
    }

    @Override
    public void onComplete(Bundle bundle) {
        oauth2AccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (oauth2AccessToken.isSessionValid()) {
            SettingKeeper.writeAccessToken(oauth2AccessToken);
            loadWeibo(true, false, 0);
        } else {
            String code = bundle.getString("code");
            Log.e("MoryBlog", "session失效，错误码" + code);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Log.e("MoryBlog", "oAuth异常", e);
    }

    @Override
    public void onCancel() {
        Log.i("MoryBlog", "onCancel: ");
    }

}
