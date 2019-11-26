package com.mory.moryblog.net;

import com.mory.moryblog.entity.Comments;
import com.mory.moryblog.entity.Timeline;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeiboApi {

    /**
     * 获取首页时间线
     */
    @GET("/2/statuses/home_timeline.json")
    Observable<Timeline> homeTimeline(
            @Query("access_token") String accessToken,
            @Query("since_id") long sinceId,
            @Query("max_id") long maxId,
            @Query("feature") int feature,
            @Query("trim_user") int trimUser
    );

    /**
     * 获取用户时间线
     */
    @GET("/2/statuses/user_timeline.json")
    Observable<Timeline> userTimeline();

    /**
     * 获取转发时间线
     */
    @GET("/2/statuses/repost_timeline.json")
    Observable<Timeline> repostTimeline();

    /**
     * 获取某条微博的所有评论
     */
    @GET("/2/comments/show.json")
    Observable<Comments> commentWeibo(
            @Query("access_token") String accessToken,
            @Query("id") long id,
            @Query("since_id") long sinceId,
            @Query("max_id") long maxId
    );

    /**
     * 获取我发出的微博评论
     */
    @GET("/2/comments/by_me.json")
    Observable<Comments> commentByMe();

    /**
     * 获取我收到的微博评论
     */
    @GET("/2/comments/to_me.json")
    Observable<Comments> commentToMe();

}
