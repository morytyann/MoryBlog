package com.mory.moryblog.entity;

import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/28.
 * 微博实体类
 */
public class Weibo {
    // 微博创建时间
    private String created_at;
    // 微博ID
    private int id;
    // 微博MID
    private int mid;
    // 微博ID(字符串)
    private String idString;
    // 微博来源
    private String source;
    // 是否已收藏
    private boolean favorited;
    // 是否被截断
    private boolean truncated;
    // 微博信息内容
    private String text;
    // 转发数
    private int reposts_count;
    // 评论数
    private int comments_count;
    // 点赞数
    private int attitudes_count;
    // 图片地址（官方坑爹系列，其它的地址字段只返回第一张，如果要看其他图的大图，把地址中的thumbnail换成其他的再下载就行了）
    private ArrayList<String> pic_urls;
    // 缩略图片地址，没有时不返回此字段
    private ArrayList<String> thumbnail_pic;
    // 中等尺寸图片地址，没有时不返回此字段
    private ArrayList<String> bmiddle_pic;
    // 原始图片地址，没有时不返回此字段
    private ArrayList<String> original_pic;
    // 被转发的原微博信息字段，当该微博为转发微博时返回
    private Weibo retweeted_status;
    // 微博作者的用户信息字段
    private User user;
    // 以下属性暂未支持
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id() {
        return this.in_reply_to_status_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_user_id() {
        return this.in_reply_to_user_id;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getIn_reply_to_screen_name() {
        return this.in_reply_to_screen_name;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getMid() {
        return this.mid;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getReposts_count() {
        return this.reposts_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getComments_count() {
        return this.comments_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setThumbnail_pic(ArrayList<String> thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public ArrayList<String> getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setBmiddle_pic(ArrayList<String> bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public ArrayList<String> getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setOriginal_pic(ArrayList<String> original_pic) {
        this.original_pic = original_pic;
    }

    public ArrayList<String> getOriginal_pic() {
        return original_pic;
    }

    public void setRetweeted_status(Weibo retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public Weibo getRetweeted_status() {
        return retweeted_status;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getIdString() {
        return idString;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setPic_urls(ArrayList<String> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public ArrayList<String> getPic_urls() {
        return pic_urls;
    }
}
