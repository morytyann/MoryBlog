package com.mory.moryblog.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mory on 2016/3/28.
 * 用户实体类
 */
@Data
@NoArgsConstructor
public class User implements Serializable {
    // UID
    private long id;
    // UID（字符串）
    private String idString;
    // 注册时间
    private Date createdAt;
    // 昵称
    private String screenName;
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
    private String profileImageUrl;
    // 头像地址(大图，180x180px)
    private String avatarLarge;
    // 个性化域名
    private String domain;
    // 所在地
    private String location;
    // 所在省份ID
    private int province;
    // 所在城市ID
    private int city;
    // 粉丝数
    private int followersCount;
    // 关注数
    private int friendsCount;
    // 收藏数
    private int favouritesCount;
    // 微博数
    private int statusesCount;
    // 互粉数
    private int biFollowersCount;
    // 在线状态，0：不在线、1：在线
    private int onlineStatus;
    // 是否允许所有人给我发私信，true：是，false：否
    private boolean allowAllActMsg;
    // 是否允许所有人对我的微博进行评论，true：是，false：否
    private boolean allowAllComment;
    // 是否允许标识用户的地理位置，true：是，false：否
    private boolean geoEnabled;
    // 该用户是否关注当前登录用户，true：是，false：否
    private boolean followMe;
    // 是否是微博认证用户，即加V用户，true：是，false：否
    private boolean verified;
    // 认证原因
    private String verifiedReason;
    // 以下属性暂未支持
    private boolean following;

}
