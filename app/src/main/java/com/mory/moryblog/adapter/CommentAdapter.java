package com.mory.moryblog.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mory.moryblog.biz.CommentBiz;
import com.mory.moryblog.entity.Comment;

import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/31.
 * 自定义adapter用于显示评论
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Comment> comments;
    private Activity activity;
    private int resource;

    public CommentAdapter(Activity activity, ArrayList<Comment> comments, int resource) {
        this.comments = comments;
        this.resource = resource;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentBiz.showComment(activity, holder, comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
