package com.mory.moryblog.biz;

import android.os.Message;
import android.support.annotation.NonNull;

import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
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
    /**
     * 初次加载微博
     *
     * @param activity 上下文
     * @return 微博列表
     */
    public static ArrayList<Weibo> loadWeibo(MainActivity activity) {
        ArrayList<Weibo> weibos = new ArrayList<>();
        AsyncWeiboRunner runner = new AsyncWeiboRunner(activity);
        WeiboParameters params = new WeiboParameters(Constant.APP_KEY);
        params.put("access_token", SettingKeeper.readAccessToken(activity).getToken());
        String s = runner.request(Constant.HOME_TIMELINE, params, "GET");
        try {
            JSONObject jObject = new JSONObject(s);
            if (!jObject.has("statuses")) {
                return null;
            } else {
                JSONArray jArray = jObject.getJSONArray("statuses");
                for (int i = 0; i < jArray.length(); i++) {
                    weibos.add(getWeibo(jArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weibos;
    }

    /**
     * 更新微博列表（加载最新Or加载更多）
     *
     * @param activity 上下文
     * @param weibos   微博列表
     * @param type     类型 {@link Constant}
     */
    synchronized public static int refreshWeibo(MainActivity activity, ArrayList<Weibo> weibos, String type) {
        if (Constant.IS_FRESHING) {
            return Constant.FRESHING;
        }
        Constant.IS_FRESHING = true;
        if (weibos == null) {
            return Constant.FRESH_FAILED;
        }
        switch (type) {
            case Constant.LOAD_MORE:
                break;
            case Constant.LOAD_NEW:
                ArrayList<Weibo> newWeibos = new ArrayList<>();
                AsyncWeiboRunner runner = new AsyncWeiboRunner(activity);
                WeiboParameters params = new WeiboParameters(Constant.APP_KEY);
                params.put("access_token", SettingKeeper.readAccessToken(activity).getToken());
                params.put("since_id", weibos.get(0).getIdString());
                String s = runner.request(Constant.HOME_TIMELINE, params, "GET");
                try {
                    JSONObject jObject = new JSONObject(s);
                    if (!jObject.has("statuses")) {
                        return Constant.FRESH_FAILED;
                    } else {
                        JSONArray jArray = jObject.getJSONArray("statuses");
                        if (jArray.length() == 0) {
                            return Constant.FRESH_NO_NEW;
                        }
                        for (int i = 0; i < jArray.length(); i++) {
                            newWeibos.add(getWeibo(jArray.getJSONObject(i)));
                        }
                        newWeibos.addAll(weibos);
                        weibos.clear();
                        weibos.addAll(newWeibos);
                        newWeibos.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return Constant.FRESH_FAILED;
        }
        return Constant.FRESH_FAILED;
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
            ArrayList<String> pic_urls = new ArrayList<>();
            for (int j = 0; j < picArray.length(); j++) {
                pic_urls.add(picArray.getJSONObject(j).getString("thumbnail_pic"));
            }
            weibo.setPic_urls(pic_urls);
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
