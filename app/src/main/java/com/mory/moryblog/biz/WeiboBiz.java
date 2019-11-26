package com.mory.moryblog.biz;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;

import com.mory.moryblog.adapter.WeiboViewHolder;
import com.mory.moryblog.entity.Pic;
import com.mory.moryblog.entity.User;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.ImageUtil;
import com.mory.moryblog.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 显示微博
     *
     * @param activity activity
     * @param weibo    微博对象
     * @param holder   持有所有子View的对象
     * @param position 位置
     */
    public static void showWeibo(final Activity activity, Weibo weibo, final WeiboViewHolder holder, int position) {
        if (position == 0) {
            Point p = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(p);
            retweetGridLayoutWidth = p.x - (holder.llContent.getPaddingStart() + holder.llContent.getPaddingEnd() + holder.llRetweet.getPaddingStart() + holder.llRetweet.getPaddingEnd());
            weiboGridLayoutWidth = p.x - (holder.llContent.getPaddingStart() + holder.llContent.getPaddingEnd() + holder.glPics.getPaddingStart() + holder.glPics.getPaddingEnd());
        }
        Weibo retweet = weibo.getRetweetedStatus();
        User user = weibo.getUser();
        Picasso.get().load(user.getAvatarLarge()).into(holder.civUserAvatar);
        holder.tvUserName.setText(user.getName());
        holder.tvCreateAt.setText(TextUtil.getReadableTime(weibo.getCreatedAt()));
        holder.tvText.setText(weibo.getText());
        holder.tvThumbUpCount.setText(String.valueOf(weibo.getAttitudesCount()));
        holder.tvCommentCount.setText(String.valueOf(weibo.getCommentsCount()));
        holder.tvRetweetCount.setText(String.valueOf(weibo.getRepostsCount()));
        holder.glPics.removeAllViews();
        holder.glRetweetPics.removeAllViews();
        if (retweet != null) {
            holder.llRetweet.setVisibility(View.VISIBLE);
            if (retweet.getDeleted() != 1) {
                String retweetText = "@" + retweet.getUser().getName() + "：" + retweet.getText();
                holder.tvRetweetText.setText(retweetText);
            } else {
                holder.tvRetweetText.setText(retweet.getText());
            }
            String allCount = retweet.getCommentsCount() + "评论|" + retweet.getRepostsCount() + "转发|" + retweet.getAttitudesCount() + "赞";
            holder.tvAllCount.setText(allCount);
            List<Pic> pics = retweet.getPicUrls();
            final List<String> picUrls = new ArrayList<>();
            for (Pic pic : pics) {
                picUrls.add(pic.getThumbnailPic());
            }
            int size;
            if ((size = picUrls.size()) > 0) {
                final int width = retweetGridLayoutWidth / 4;
                final int height = retweetGridLayoutWidth / 4;
                int count = 3;
                if (size == 4) {
                    count = 2;
                }
                holder.glRetweetPics.setColumnCount(count);
                holder.glRetweetPics.setRowCount(count);
                ImageUtil.showPhotoOnGridLayout(activity, picUrls, holder.glRetweetPics, width, height);
            }
        } else {
            holder.llRetweet.setVisibility(View.GONE);
            List<Pic> pics = weibo.getPicUrls();
            final List<String> picUrls = new ArrayList<>();
            for (Pic pic : pics) {
                picUrls.add(pic.getThumbnailPic());
            }
            int size;
            if ((size = picUrls.size()) > 0) {
                int width = weiboGridLayoutWidth / 4;
                int height = weiboGridLayoutWidth / 4;
                int count = 3;
                if (size == 4) {
                    count = 2;
                }
                holder.glPics.setColumnCount(count);
                holder.glPics.setRowCount(count);
                ImageUtil.showPhotoOnGridLayout(activity, picUrls, holder.glPics, width, height);
            }
        }
    }

}
