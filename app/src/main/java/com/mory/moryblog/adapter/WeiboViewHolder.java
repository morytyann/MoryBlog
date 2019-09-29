package com.mory.moryblog.adapter;

import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mory.moryblog.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mory on 2016/3/31.
 * ViewHolder类
 * 持有微博View的所有子View
 */
public class WeiboViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView civUserAvatar;
    public TextView tvUserName;
    public TextView tvCreateAt;
    public ImageButton ibShowMenu;
    public TextView tvText;
    public LinearLayout llRetweet;
    public TextView tvRetweetText;
    public GridLayout glRetweetPics;
    public TextView tvAllCount;
    public GridLayout glPics;
    public TextView tvThumbUpCount;
    public TextView tvCommentCount;
    public TextView tvRetweetCount;
    public LinearLayout llContent;
    public View itemView;

    public WeiboViewHolder(View itemView) {
        super(itemView);
        civUserAvatar = itemView.findViewById(R.id.civUserAvatar);
        tvUserName = itemView.findViewById(R.id.tvUserName);
        tvCreateAt = itemView.findViewById(R.id.tvCreateAt);
        ibShowMenu = itemView.findViewById(R.id.ibShowMenu);
        tvText = itemView.findViewById(R.id.tvText);
        llRetweet = itemView.findViewById(R.id.llRetweet);
        tvRetweetText = itemView.findViewById(R.id.tvRetweetText);
        glRetweetPics = itemView.findViewById(R.id.glRetweetPics);
        tvAllCount = itemView.findViewById(R.id.tvAllCount);
        glPics = itemView.findViewById(R.id.glPics);
        tvThumbUpCount = itemView.findViewById(R.id.tvThumbUpCount);
        tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
        llContent = itemView.findViewById(R.id.llContent);
        this.itemView = itemView;
    }
}
