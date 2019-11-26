package com.mory.moryblog.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mory.moryblog.entity.Comment;
import com.mory.moryblog.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mory on 2016/3/31.
 * 自定义adapter用于显示评论
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private LayoutInflater inflater;
    private List<Comment> comments;
    private int resource;

    public CommentAdapter(Activity activity, List<Comment> comments, int resource) {
        this.comments = comments;
        this.resource = resource;
        this.inflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        Log.i("MoryBlog", "onBindViewHolder: ");

        Picasso.get().load(comment.getUser().getAvatarLarge()).into(holder.civCommentUserAvatar);
        holder.tvCommentUser.setText(comment.getUser().getName());
        holder.tvCommentCreateAt.setText(TextUtil.getReadableTime(comment.getCreatedAt()));
        holder.tvCommentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}
