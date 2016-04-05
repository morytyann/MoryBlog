package com.mory.moryblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mory.moryblog.R;
import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.activity.WeiboDetailActivity;
import com.mory.moryblog.biz.WeiboBiz;
import com.mory.moryblog.entity.User;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.listener.OnPicClickListener;
import com.mory.moryblog.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/28.
 * 自定义adapter 用于显示微博列表
 */
public class WeiboAdapter extends RecyclerView.Adapter<WeiboViewHolder> {
    private MainActivity activity;
    private LayoutInflater inflater;
    private ArrayList<Weibo> weibos;
    private int resource;
    private int position;

    public WeiboAdapter(MainActivity activity, ArrayList<Weibo> weibos, int resource) {
        this.weibos = weibos;
        this.resource = resource;
        this.activity = activity;
        this.inflater = this.activity.getLayoutInflater();
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
    public WeiboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);
        return new WeiboViewHolder(view);
    }

    /**
     * 在这里设置ViewHolder里各个组件的属性
     *
     * @param holder   ViewHolder对象
     * @param position 当前位置
     */
    @Override
    public void onBindViewHolder(WeiboViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, WeiboDetailActivity.class).putExtra("position", position));
            }
        });
        WeiboBiz.showWeibo(activity, weibos.get(position), holder, position);
    }
}
