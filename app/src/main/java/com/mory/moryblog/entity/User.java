package com.mory.moryblog.entity;

/**
 * Created by Mory on 2016/3/28.
 * 用户实体类
 */
public class User {
    // 注册时间
    private String created_at;
    // UID
    private int id;
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
    // 微博数
    private int statuses_count;
    // 收藏数
    private int favourites_count;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getScreen_name() {
        return this.screen_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getProvince() {
        return this.province;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getCity() {
        return this.city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url() {
        return this.profile_image_url;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollowers_count() {
        return this.followers_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getFriends_count() {
        return this.friends_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getStatuses_count() {
        return this.statuses_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public int getFavourites_count() {
        return this.favourites_count;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean getFollowing() {
        return this.following;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean getAllow_all_act_msg() {
        return this.allow_all_act_msg;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean getGeo_enabled() {
        return this.geo_enabled;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean getVerified() {
        return this.verified;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public boolean getAllow_all_comment() {
        return this.allow_all_comment;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getAvatar_large() {
        return this.avatar_large;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public String getVerified_reason() {
        return this.verified_reason;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public boolean getFollow_me() {
        return this.follow_me;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getOnline_status() {
        return this.online_status;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    public int getBi_followers_count() {
        return this.bi_followers_count;
    }
}
