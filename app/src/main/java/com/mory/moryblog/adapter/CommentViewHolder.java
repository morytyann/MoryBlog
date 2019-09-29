package com.mory.moryblog.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mory.moryblog.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mory on 2016/3/31.
 * ViewHolder类
 * 持有评论View的所有子View
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView civCommentUserAvatar;
    public TextView tvCommentUser;
    public TextView tvCommentCreateAt;
    public ImageButton ibCommentShowMenu;
    public TextView tvCommentText;

    public CommentViewHolder(View itemView) {
        super(itemView);
        civCommentUserAvatar = (CircleImageView) itemView.findViewById(R.id.civCommentUserAvatar);
        tvCommentUser = (TextView) itemView.findViewById(R.id.tvCommentUser);
        tvCommentCreateAt = (TextView) itemView.findViewById(R.id.tvCommentCreateAt);
        ibCommentShowMenu = (ImageButton) itemView.findViewById(R.id.ibCommentShowMenu);
        tvCommentText = (TextView) itemView.findViewById(R.id.tvCommentText);
    }
}
