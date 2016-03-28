package com.mory.moryblog.biz;

import android.content.Context;

import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/28.
 * 封装了与微博相关的业务类
 * 1.加载微博（包括加载更多和加载最新）
 * 2.转发微博
 * 3.点赞微博
 * 4.评论微博
 * 5.删除微博
 */
public class WeiboBiz {
    public static ArrayList<Weibo> loadWeibo(Context c) {
        ArrayList<Weibo> weibos = new ArrayList<>();
        AsyncWeiboRunner runner = new AsyncWeiboRunner(c);
        WeiboParameters params = new WeiboParameters(Constant.APP_KEY);
        params.put("access_token", SettingKeeper.readAccessToken(c).getToken());
        runner.requestAsync(Constant.HOME_TIMELINE, params, "GET", new RequestListener() {
            @Override
            public void onComplete(String s) {
                try {
                    JSONObject jObject = new JSONObject(s);
                    if (!jObject.has("statuses")) {
                        return;
                    }
                    JSONArray jArray = jObject.getJSONArray("statuses");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject weiboObject = jArray.getJSONObject(i);
                        Weibo weibo = new Weibo();
                        if (weiboObject.has("created_at")) {
                            weibo.setCreated_at(weiboObject.getString("created_at"));
                        }
                        if (weiboObject.has("text")) {
                            weibo.setText(weiboObject.getString("text"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
        if (weibos.size() > 0) {
            return weibos;
        }
        return null;
    }
}
