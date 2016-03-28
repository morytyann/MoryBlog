package com.mory.moryblog.biz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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
        final ArrayList<Weibo> weibos = new ArrayList<>();
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
                        weibos.add(getWeibo(weiboObject));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.d(Constant.TAG, "onWeiboException: " + e.getLocalizedMessage());
            }
        });
        if (weibos.size() > 0) {
            return weibos;
        }
        return null;
    }

    @NonNull
    private static Weibo getWeibo(JSONObject weiboObject) throws JSONException {
        Weibo weibo = new Weibo();
        if (weiboObject.has("created_at")) {
            weibo.setCreated_at(weiboObject.getString("created_at"));
        }
        if (weiboObject.has("text")) {
            weibo.setText(weiboObject.getString("text"));
        }
        if (weiboObject.has("idstr")) {
            weibo.setIdString(weiboObject.getString("idstr"));
        }
        if (weiboObject.has("reposts_count")) {
            weibo.setReposts_count(weiboObject.getInt("reposts_count"));
        }
        if (weiboObject.has("comments_count")) {
            weibo.setComments_count(weiboObject.getInt("comments_count"));
        }
        if (weiboObject.has("attitudes_count")) {
            weibo.setAttitudes_count(weiboObject.getInt("attitudes_count"));
        }
        if (weiboObject.has("pic_urls")) {
            JSONArray picArray = weiboObject.getJSONArray("pic_urls");
            ArrayList<String> pic_urls = new ArrayList<String>();
            for (int j = 0; j < picArray.length(); j++) {
                pic_urls.add(picArray.getJSONObject(j).getString("thumbnail_pic"));
            }
        }
        if (weiboObject.has("favorited")) {
            weibo.setFavorited(weiboObject.getBoolean("favorited"));
        }
        if (weiboObject.has("truncated")) {
            weibo.setTruncated(weiboObject.getBoolean("truncated"));
        }
        if (weiboObject.has("source")) {
            weibo.setSource(weiboObject.getString("source"));
        }
        if (weiboObject.has("retweeted_status")) {
            weibo.setRetweeted_status(getWeibo(weiboObject.getJSONObject("retweeted_status")));
        }
        if (weiboObject.has("user")) {
            weibo.setUser(UserBiz.getUser(weiboObject.getJSONObject("user")));
        }
        return weibo;
    }
}
