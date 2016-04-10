package com.mory.moryblog.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        civUserAvatar = (CircleImageView) itemView.findViewById(R.id.civUserAvatar);
        tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        tvCreateAt = (TextView) itemView.findViewById(R.id.tvCreateAt);
        ibShowMenu = (ImageButton) itemView.findViewById(R.id.ibShowMenu);
        tvText = (TextView) itemView.findViewById(R.id.tvText);
        llRetweet = (LinearLayout) itemView.findViewById(R.id.llRetweet);
        tvRetweetText = (TextView) itemView.findViewById(R.id.tvRetweetText);
        glRetweetPics = (GridLayout) itemView.findViewById(R.id.glRetweetPics);
        tvAllCount = (TextView) itemView.findViewById(R.id.tvAllCount);
        glPics = (GridLayout) itemView.findViewById(R.id.glPics);
        tvThumbUpCount = (TextView) itemView.findViewById(R.id.tvThumbUpCount);
        tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
        tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);
        llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
        this.itemView = itemView;
    }
}
