package com.mory.moryblog.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mory.moryblog.R;
import com.mory.moryblog.entity.User;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mory on 2016/3/28.
 * 自定义adapter 用于显示微博列表
 */
public class WeiboAdapter extends RecyclerView.Adapter<WeiboAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Weibo> weibos;
    private AppCompatActivity activity;
    private LayoutInflater inflater;

    public WeiboAdapter(AppCompatActivity activity, ArrayList<Weibo> weibos, int resource) {
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
        Weibo weibo = weibos.get(position);
        Weibo retweet = weibo.getRetweeted_status();
        User user = weibo.getUser();
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(ImageLoaderConfiguration.createDefault(activity));
        }
        loader.displayImage(user.getAvatar_large(), holder.civUserAvatar);
        holder.tvUserName.setText(user.getName());
        holder.tvCreateAt.setText(weibo.getCreated_at());
        holder.tvText.setText(weibo.getText());
        holder.tvThumbUpCount.setText(weibo.getAttitudes_count() + "");
        holder.tvCommentCount.setText(weibo.getComments_count() + "");
        holder.tvRetweetCount.setText(weibo.getReposts_count() + "");
        if (retweet != null) {
            holder.llRetweet.setVisibility(View.VISIBLE);
            holder.gvPics.setVisibility(View.GONE);
            holder.tvRetweetText.setText("@" + retweet.getUser().getName() + "：" + retweet.getText());
            holder.tvAllCount.setText(retweet.getComments_count() + "评论|" + retweet.getReposts_count() + "转发|" + retweet.getAttitudes_count() + "赞");
            ArrayList<String> pic_urls = retweet.getPic_urls();
            if (pic_urls != null && pic_urls.size() > 0) {
                if (pic_urls.size() == 4) {
                    holder.gvRetweetPics.setNumColumns(2);
                }
                holder.gvRetweetPics.setAdapter(new PicAdapter(activity, pic_urls, R.layout.grid_item_pic));
//                for (int i = 0; i < pic_urls.size(); i++) {
//                    Log.d(Constant.TAG, "onBindViewHolder: " + "RetweetLoadingPicture");
//                    ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_pic, holder.gvRetweetPics, false);
//                    loader.displayImage(pic_urls.get(i), view);
//                    holder.gvRetweetPics.addView(view);
//                }
            } else {
                holder.gvRetweetPics.setVisibility(View.GONE);
            }
        } else {
            holder.llRetweet.setVisibility(View.GONE);
            ArrayList<String> pic_urls = weibo.getPic_urls();
            if (pic_urls != null && pic_urls.size() > 0) {
                holder.gvPics.setVisibility(View.VISIBLE);
                if (pic_urls.size() == 4) {
                    holder.gvPics.setNumColumns(2);
                }
                holder.gvPics.setAdapter(new PicAdapter(activity, pic_urls, R.layout.grid_item_pic));
//                for (int i = 0; i < pic_urls.size(); i++) {
//                    Log.d(Constant.TAG, "onBindViewHolder: " + "WeiboLoadingPicture");
//                    ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_pic, holder.gvPics, false);
//                    loader.displayImage(pic_urls.get(i), view);
//                    holder.gvPics.addView(view);
//                }
            } else {
                holder.gvPics.setVisibility(View.GONE);
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
        public MyGridView gvRetweetPics;
        public TextView tvAllCount;
        public MyGridView gvPics;
        public TextView tvThumbUpCount;
        public TextView tvCommentCount;
        public TextView tvRetweetCount;

        public ViewHolder(View itemView) {
            super(itemView);
            civUserAvatar = (CircleImageView) itemView.findViewById(R.id.civUserAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvCreateAt = (TextView) itemView.findViewById(R.id.tvCreateAt);
            ibShowMenu = (ImageButton) itemView.findViewById(R.id.ibShowMenu);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            llRetweet = (LinearLayout) itemView.findViewById(R.id.llRetweet);
            tvRetweetText = (TextView) itemView.findViewById(R.id.tvRetweetText);
            gvRetweetPics = (MyGridView) itemView.findViewById(R.id.gvRetweetPics);
            tvAllCount = (TextView) itemView.findViewById(R.id.tvAllCount);
            gvPics = (MyGridView) itemView.findViewById(R.id.gvPics);
            tvThumbUpCount = (TextView) itemView.findViewById(R.id.tvThumbUpCount);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);
        }
    }
}
