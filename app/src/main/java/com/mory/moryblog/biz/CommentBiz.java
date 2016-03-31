package com.mory.moryblog.biz;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.mory.moryblog.adapter.CommentViewHolder;
import com.mory.moryblog.entity.Comment;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/31.
 * 封装了与评论相关的业务类
 * 1.加载评论（包括加载更多和加载最新）
 * 2.复制评论内容
 * 3.回复评论
 * 4.删除评论
 * 5.显示评论
 */
public class CommentBiz {
    public static ArrayList<Comment> loadComment(Activity activity, Weibo weibo) {
        ArrayList<Comment> comments = new ArrayList<>();
        String id = weibo.getIdString();
        AsyncWeiboRunner runner = new AsyncWeiboRunner(activity);
        WeiboParameters params = new WeiboParameters(Constant.APP_KEY);
        params.put("access_token", SettingKeeper.readAccessToken(activity).getToken());
        params.put("id", id);
        String result = runner.request(Constant.COMMENT_SHOW, params, "GET");
        try {
            JSONObject jObject = new JSONObject(result);
            if (!jObject.has("comments")) {
                return null;
            }
            JSONArray jArray = jObject.getJSONArray("comments");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject commentObject = jArray.getJSONObject(i);
                comments.add(getComment(commentObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private static Comment getComment(JSONObject commentObject) throws JSONException {
        Comment comment = new Comment();
        if (commentObject.has("created_at")) {
            comment.setCreated_at(commentObject.getString("created_at"));
        }
        if (commentObject.has("mid")) {
            comment.setMid(commentObject.getString("mid"));
        }
        if (commentObject.has("source")) {
            comment.setSource(commentObject.getString("source"));
        }
        if (commentObject.has("user")) {
            comment.setUser(UserBiz.getUser(commentObject.getJSONObject("user")));
        }
        if (commentObject.has("text")) {
            comment.setText(commentObject.getString("text"));
        }
        return comment;
    }

    public static void showComment(AppCompatActivity activity, CommentViewHolder holder, Comment comment) {
        Picasso.with(activity).load(comment.getUser().getAvatar_large()).into(holder.civCommentUserAvatar);
        holder.tvCommentUser.setText(comment.getUser().getName());
        holder.tvCommentCreateAt.setText(comment.getCreated_at());
        holder.tvCommentText.setText(comment.getText());
    }
}
