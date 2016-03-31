package com.mory.moryblog.biz;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mory.moryblog.R;
import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.adapter.WeiboViewHolder;
import com.mory.moryblog.entity.User;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.listener.OnPicClickListener;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.mory.moryblog.util.StringUtil;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/28.
 * 封装了与微博相关的业务类
 * 1.加载微博（包括加载更多和加载最新）
 * 2.转发微博
 * 3.点赞微博
 * 4.评论微博
 * 5.删除微博
 * 6.显示微博
 */
public class WeiboBiz {
    private static int retweetGridLayoutWidth;
    private static int weiboGridLayoutWidth;
    private static LayoutInflater inflater;

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
        String result = runner.request(Constant.HOME_TIMELINE, params, "GET");
        try {
            JSONObject jObject = new JSONObject(result);
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
     * @param type     类型 {@link Constant}
     */
    synchronized public static int refreshWeibo(MainActivity activity, String type) {
        if (Constant.IS_FRESHING) {
            return Constant.FRESHING;
        }
        Constant.IS_FRESHING = true;
        if (Constant.weibos == null) {
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
                params.put("since_id", Constant.weibos.get(0).getIdString());
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
                        newWeibos.addAll(Constant.weibos);
                        Constant.weibos.clear();
                        Constant.weibos.addAll(newWeibos);
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

    /**
     * 获得单条微博的对象
     *
     * @param weiboObject 微博的JSONObject
     * @return 微博对象
     * @throws JSONException
     */
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

    /**
     * 显示微博
     *
     * @param activity activity
     * @param weibo    微博对象
     * @param holder   持有所有子View的对象
     * @param position 位置
     */
    public static void showWeibo(AppCompatActivity activity, Weibo weibo, WeiboViewHolder holder, int position) {
        if (position == 0) {
            Point p = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(p);
            retweetGridLayoutWidth = p.x - (holder.llContent.getPaddingStart() + holder.llContent.getPaddingEnd() + holder.llRetweet.getPaddingStart() + holder.llRetweet.getPaddingEnd());
            weiboGridLayoutWidth = p.x - (holder.llContent.getPaddingStart() + holder.llContent.getPaddingEnd());
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        Weibo retweet = weibo.getRetweeted_status();
        User user = weibo.getUser();
        Picasso.with(activity).load(user.getAvatar_large()).into(holder.civUserAvatar);
        holder.tvUserName.setText(user.getName());
        try {
            holder.tvCreateAt.setText(StringUtil.getReadableTime(weibo.getCreated_at()));
        } catch (ParseException e) {
            holder.tvCreateAt.setText(weibo.getCreated_at());
            e.printStackTrace();
        }
        holder.tvText.setText(weibo.getText());
        holder.tvThumbUpCount.setText(weibo.getAttitudes_count() + "");
        holder.tvCommentCount.setText(weibo.getComments_count() + "");
        holder.tvRetweetCount.setText(weibo.getReposts_count() + "");
        holder.glPics.removeAllViews();
        holder.glRetweetPics.removeAllViews();
        if (retweet != null) {
            holder.llRetweet.setVisibility(View.VISIBLE);
            holder.tvRetweetText.setText("@" + retweet.getUser().getName() + "：\n" + retweet.getText());
            holder.tvAllCount.setText(retweet.getComments_count() + "评论|" + retweet.getReposts_count() + "转发|" + retweet.getAttitudes_count() + "赞");
            ArrayList<String> pic_urls = retweet.getPic_urls();
            if (pic_urls != null && pic_urls.size() > 0) {
                for (int i = 0; i < pic_urls.size(); i++) {
                    ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_pic, holder.glRetweetPics, false);
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.width = retweetGridLayoutWidth / 3;
                    params.height = retweetGridLayoutWidth / 3;
                    view.setTag(R.id.tag0, pic_urls);
                    view.setTag(R.id.tag_1, i);
                    view.setOnClickListener(new OnPicClickListener(activity));
                    Picasso.with(activity).load(pic_urls.get(i)).resize(retweetGridLayoutWidth / 3, retweetGridLayoutWidth / 3).centerCrop().into(view);
                    holder.glRetweetPics.addView(view);
                }
            }
        } else {
            holder.llRetweet.setVisibility(View.GONE);
            ArrayList<String> pic_urls = weibo.getPic_urls();
            if (pic_urls != null && pic_urls.size() > 0) {
                for (int i = 0; i < pic_urls.size(); i++) {
                    ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_pic, holder.glPics, false);
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.width = weiboGridLayoutWidth / 3;
                    params.height = weiboGridLayoutWidth / 3;
                    view.setTag(R.id.tag0, pic_urls);
                    view.setTag(R.id.tag_1, i);
                    view.setOnClickListener(new OnPicClickListener(activity));
                    Picasso.with(activity).load(pic_urls.get(i)).resize(weiboGridLayoutWidth / 3, weiboGridLayoutWidth / 3).centerCrop().into(view);
                    holder.glPics.addView(view);
                }
            }
        }
    }
}
