package com.mory.moryblog.entity;

import lombok.Data;

/**
 * Created by Mory on 2016/3/28.
 * 评论实体类
 */
@Data
public class Comment {
    // 评论创建时间
    private String created_at;
    // 评论ID
    private int id;
    // 评论MID
    private String mid;
    // 评论来源
    private String source;
    // 评论作者
    private User user;
    // 评论内容
    private String text;

}
