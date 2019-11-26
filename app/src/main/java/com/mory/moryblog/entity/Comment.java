package com.mory.moryblog.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mory on 2016/3/28.
 * 评论实体类
 */
@Data
@NoArgsConstructor
public class Comment implements Serializable {
    // 评论ID
    private long id;
    // 评论MID
    private String mid;
    // 评论创建时间
    private Date createdAt;
    // 评论来源
    private String source;
    // 评论作者
    private User user;
    // 评论内容
    private String text;

}
