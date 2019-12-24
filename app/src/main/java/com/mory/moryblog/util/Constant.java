package com.mory.moryblog.util;

/**
 * Created by Mory on 2016/3/28.
 * 常量类
 */
public interface Constant {
    /**
     * APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY。
     */
    String APP_KEY = "3053762398";

    /**
     * 回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    String SCOPE = "email,direct_messages_read,direct_messages_write," +
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
            "follow_app_official_microblog," + "invitation_write";

    /**
     * 全局Log标签
     */
    String TAG = "MoryBlog";

}
