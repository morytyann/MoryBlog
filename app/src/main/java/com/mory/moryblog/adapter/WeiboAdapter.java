package com.mory.moryblog.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mory.moryblog.R;
import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.entity.User;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.listener.OnPicClickListener;
import com.mory.moryblog.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mory on 2016/3/28.
 * 自定义adapter 用于显示微博列表
 */
public class WeiboAdapter extends RecyclerView.Adapter<WeiboAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Weibo> weibos;
    private MainActivity activity;
    private LayoutInflater inflater;
    private int weiboGridLayoutwholeWidth = 750;
    private int retweetGridLayoutWidth = 750;

    public WeiboAdapter(MainActivity activity, ArrayList<Weibo> weibos, int resource) {
        this.weibos = weibos;
        this.resource = resource;
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return weibos.size();
    }

    /**
     * 在这里初始化ViewHolder
     *
     * @param parent   父控件
     * @param viewType 不知道...目测是可以根据viewType返回不同的holder
     * @return ViewHolder对象
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 在这里设置ViewHolder里各个组件的属性
     *
     * @param holder   ViewHolder对象
     * @param position 当前位置
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            Point p = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(p);
            retweetGridLayoutWidth = p.x - (holder.llContent.getPaddingStart() + holder.llContent.getPaddingEnd() + holder.llRetweet.getPaddingStart() + holder.llRetweet.getPaddingEnd());
            weiboGridLayoutwholeWidth = p.x - (holder.llContent.getPaddingStart() + holder.llContent.getPaddingEnd());
        }
        Weibo weibo = weibos.get(position);
        Weibo retweet = weibo.getRetweeted_status();
        User user = weibo.getUser();
        Picasso.with(activity).load(user.getAvatar_large()).into(holder.civUserAvatar);
        holder.tvUserName.setText(user.getName());
        try {
            holder.tvCreateAt.setText(StringUtil.getReadableTime(weibo.getCreated_at()));
        } catch (ParseException e) {
            holder.tvCreateAt.setText(weibo.getCreated_at());
            e.printStackTrace();
        }
        holder.tvText.setText(weibo.getText());
        holder.tvThumbUpCount.setText(weibo.getAttitudes_count() + "");
        holder.tvCommentCount.setText(weibo.getComments_count() + "");
        holder.tvRetweetCount.setText(weibo.getReposts_count() + "");
        holder.glPics.removeAllViews();
        holder.glRetweetPics.removeAllViews();
        if (retweet != null) {
            holder.llRetweet.setVisibility(View.VISIBLE);
            holder.tvRetweetText.setText("@" + retweet.getUser().getName() + "：\n" + retweet.getText());
            holder.tvAllCount.setText(retweet.getComments_count() + "评论|" + retweet.getReposts_count() + "转发|" + retweet.getAttitudes_count() + "赞");
            ArrayList<String> pic_urls = retweet.getPic_urls();
            if (pic_urls != null && pic_urls.size() > 0) {
                for (int i = 0; i < pic_urls.size(); i++) {
                    ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_pic, holder.glRetweetPics, false);
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.width = retweetGridLayoutWidth / 3;
                    params.height = retweetGridLayoutWidth / 3;
                    view.setTag(R.id.tag_first, pic_urls);
                    view.setTag(R.id.tag_second, i);
                    view.setOnClickListener(new OnPicClickListener(activity));
                    Picasso.with(activity).load(pic_urls.get(i)).resize(retweetGridLayoutWidth / 3, retweetGridLayoutWidth / 3).centerCrop().into(view);
                    holder.glRetweetPics.addView(view);
                }
            }
        } else {
            holder.llRetweet.setVisibility(View.GONE);
            ArrayList<String> pic_urls = weibo.getPic_urls();
            if (pic_urls != null && pic_urls.size() > 0) {
                for (int i = 0; i < pic_urls.size(); i++) {
                    ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_pic, holder.glPics, false);
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.width = weiboGridLayoutwholeWidth / 3;
                    params.height = weiboGridLayoutwholeWidth / 3;
                    view.setTag(R.id.tag_first, pic_urls);
                    view.setTag(R.id.tag_second, i);
                    view.setOnClickListener(new OnPicClickListener(activity));
                    Picasso.with(activity).load(pic_urls.get(i)).resize(weiboGridLayoutwholeWidth / 3, weiboGridLayoutwholeWidth / 3).centerCrop().into(view);
                    holder.glPics.addView(view);
                }
            }
        }
    }

    /**
     * 内部类 用于复用子View
     */
    class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
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
        }
    }
}
