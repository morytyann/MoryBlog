package com.mory.moryblog.biz;

import com.mory.moryblog.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mory on 2016/3/28.
 * 封装了与用户相关的业务类
 * 1.加载用户
 * 2.关注/取关用户
 * 3.私信用户
 * 4.拉黑用户
 * 5.移除粉丝
 */
public class UserBiz {
    public static User getUser(JSONObject userObject) {
        try {
            User user = new User();
            if (userObject.has("create_at")) {
                user.setCreated_at(userObject.getString("create_at"));
            }
            if (userObject.has("idstr")) {
                user.setIdString(userObject.getString("idstr"));
            }
            if (userObject.has("name")) {
                user.setName(userObject.getString("name"));
            }
            if (userObject.has("remark")) {
                user.setRemark(userObject.getString("remark"));
            }
            if (userObject.has("gender")) {
                user.setGender(userObject.getString("gender"));
            }
            if (userObject.has("description")) {
                user.setDescription(userObject.getString("description"));
            }
            if (userObject.has("url")) {
                user.setUrl(userObject.getString("url"));
            }
            if (userObject.has("profile_image_url")) {
                user.setProfile_image_url("profile_image_url");
            }
            if (userObject.has("avatar_large")) {
                user.setAvatar_large(userObject.getString("avatar_large"));
            }
            if (userObject.has("domain")) {
                user.setDomain(userObject.getString("domain"));
            }
            if (userObject.has("location")) {
                user.setLocation(userObject.getString("location"));
            }
            if (userObject.has("followers_count")) {
                user.setFollowers_count(userObject.getInt("followers_count"));
            }
            if (userObject.has("friends_count")) {
                user.setFriends_count(userObject.getInt("friends_count"));
            }
            if (userObject.has("favourites_count")) {
                user.setFavourites_count(userObject.getInt("favourites_count"));
            }
            if (userObject.has("statuses_count")) {
                user.setStatuses_count(userObject.getInt("statuses_count"));
            }
            if (userObject.has("bi_followers_count")) {
                user.setBi_followers_count(userObject.getInt("bi_followers_count"));
            }
            if (userObject.has("verified")) {
                user.setVerified(userObject.getBoolean("verified"));
            }
            if (userObject.has("verified_reason")) {
                user.setVerified_reason(userObject.getString("verified_reason"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
