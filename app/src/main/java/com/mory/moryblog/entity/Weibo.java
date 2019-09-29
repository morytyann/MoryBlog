package com.mory.moryblog.entity;

import java.util.ArrayList;

import lombok.Data;

/**
 * Created by Mory on 2016/3/28.
 * 微博实体类
 */
@Data
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
    // 是否被删除 0：否 1：是 仅在被删除的情况下返回
    private int deleted;
    // 以下属性暂未支持
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;

}
