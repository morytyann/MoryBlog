package com.mory.moryblog.entity;

/**
 * Created by Mory on 2016/3/28.
 * 评论实体类
 */
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
