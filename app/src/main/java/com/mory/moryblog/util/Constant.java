package com.mory.moryblog.util;

/**
 * Created by Mory on 2016/3/28.
 * 常量类
 */
public class Constant {
    /**
     * APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY。
     */
    public static final String APP_KEY = "3127499667";

    /**
     * 回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write," + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

    /**
     * 全局Log标签
     */
    public static final String TAG = "MoryBlog";

    /**
     * 加载微博的URL
     */
    public static final String HOME_TIMELINE = "https://api.weibo.com/2/statuses/home_timeline.json";

    /**
     * 加载更多
     */
    public static final String LOAD_MORE = "MORE";

    /**
     * 加载最新
     */
    public static final String LOAD_NEW = "NEW";

    /**
     * 成功
     */
    public static final int SUCCESS = 1827;

    /**
     * 失败
     */
    public static final int FAILED = 4;
}
