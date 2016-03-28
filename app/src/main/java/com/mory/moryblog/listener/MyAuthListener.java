package com.mory.moryblog.listener;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by Mory on 2016/3/28.
 * 授权登陆的监听
 */
public class MyAuthListener implements WeiboAuthListener {
    private MainActivity activity;

    public MyAuthListener(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * 成功授权时的回掉
     * 解析后根据Token加载微博列表
     * 如果Token为空则日志输出错误代码
     *
     * @param bundle 封装了token的对象
     */
    @Override
    public void onComplete(Bundle bundle) {
        Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(bundle);
        if (token.isSessionValid()) {
            SettingKeeper.writeAccessToken(activity, token);
            activity.initWeiboList();
        } else {
            String code = bundle.getString("code");
            Log.d(Constant.TAG, "onComplete: " + code);
        }
    }

    /**
     * 发生错误时的回掉
     * 日志输出错误信息
     *
     * @param e
     */
    @Override
    public void onWeiboException(WeiboException e) {
        Log.d(Constant.TAG, "onWeiboException: " + e.getLocalizedMessage());
    }

    /**
     * 用户取消时的回掉
     * 吐司提示
     */
    @Override
    public void onCancel() {
        Toast.makeText(activity, "用户取消", Toast.LENGTH_SHORT).show();
    }
}
