package com.mory.moryblog.entity;

import lombok.Data;

/**
 * Created by Mory on 2016/3/28.
 * 用户实体类
 */
@Data
public class User {
    // 注册时间
    private String created_at;
    // UID
    private int id;
    // UID（字符串）
    private String idString;
    // 昵称
    private String screen_name;
    // 友好名称
    private String name;
    // 用户备注信息，只有在查询用户关系时才返回此字段
    private String remark;
    // 性别，m：男，f：女，n：未知
    private String gender;
    // 个人描述
    private String description;
    // 博客地址
    private String url;
    // 头像地址(中图，50x50px)
    private String profile_image_url;
    // 头像地址(大图，180x180px)
    private String avatar_large;
    // 个性化域名
    private String domain;
    // 所在地
    private String location;
    // 所在省份ID
    private int province;
    // 所在城市ID
    private int city;
    // 粉丝数
    private int followers_count;
    // 关注数
    private int friends_count;
    // 收藏数
    private int favourites_count;
    // 微博数
    private int statuses_count;
    // 互粉数
    private int bi_followers_count;
    // 在线状态，0：不在线、1：在线
    private int online_status;
    // 是否允许所有人给我发私信，true：是，false：否
    private boolean allow_all_act_msg;
    // 是否允许所有人对我的微博进行评论，true：是，false：否
    private boolean allow_all_comment;
    // 是否允许标识用户的地理位置，true：是，false：否
    private boolean geo_enabled;
    // 该用户是否关注当前登录用户，true：是，false：否
    private boolean follow_me;
    // 是否是微博认证用户，即加V用户，true：是，false：否
    private boolean verified;
    // 认证原因
    private String verified_reason;

    // 以下属性暂未支持
    private boolean following;

}
