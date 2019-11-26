package com.mory.moryblog.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Mory on 2016/3/28.
 * 用于保存登录状态以及其他设置
 */
public class SettingKeeper {

    private static final String PREFERENCES_NAME = "MoryBlog";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    private static SharedPreferences pref;

    public static void init(Context context) {
        pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param token Token 对象
     */
    public static void writeAccessToken(Oauth2AccessToken token) {
        if (null == token) {
            return;
        }

        pref.edit()
                .putString(KEY_UID, token.getUid())
                .putString(KEY_ACCESS_TOKEN, token.getToken())
                .putString(KEY_REFRESH_TOKEN, token.getRefreshToken())
                .putLong(KEY_EXPIRES_IN, token.getExpiresTime())
                .apply();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @return Token 对象
     */
    public static Oauth2AccessToken readAccessToken() {
        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));

        return token;
    }

    /**
     * 清空 SharedPreferences 。
     */
    public static void clear() {
        pref.edit().clear().apply();
    }
}
