package com.mory.moryblog.adapter;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mory.moryblog.App;
import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.activity.WeiboDetailActivity;
import com.mory.moryblog.biz.WeiboBiz;
import com.mory.moryblog.entity.Timeline;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.SettingKeeper;
import com.mory.moryblog.util.TextUtil;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mory on 2016/3/28.
 * 自定义adapter 用于显示微博列表
 */
public class WeiboAdapter extends RecyclerView.Adapter<WeiboViewHolder> {

    private MainActivity activity;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout srl;
    private LayoutInflater inflater;
    private List<Weibo> weiboList;
    private int resource;

    public WeiboAdapter(MainActivity activity, LinearLayoutManager manager, SwipeRefreshLayout srl, List<Weibo> weiboList, int resource) {
        this.weiboList = weiboList;
        this.resource = resource;
        this.manager = manager;
        this.srl = srl;
        this.activity = activity;
        this.inflater = this.activity.getLayoutInflater();
    }

    @Override
    public int getItemCount() {
        return weiboList.size();
    }

    /**
     * 在这里初始化ViewHolder
     *
     * @param parent   父控件
     * @param viewType 不知道...目测是可以根据viewType返回不同的holder
     * @return ViewHolder对象
     */
    @NonNull
    @Override
    public WeiboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                activity.startActivity(new Intent(activity, WeiboDetailActivity.class).putExtra("weibo", weiboList.get(position)));
            }
        });
        WeiboBiz.showWeibo(activity, weiboList.get(position), holder, position);
        if (manager.findLastVisibleItemPosition() == getItemCount() - 2 && !srl.isRefreshing()) {
            srl.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doRefreshLoadMore();
                }
            }, 1000);
        }
    }

    /**
     * 完成加载更多微博的业务
     */
    private void doRefreshLoadMore() {
        String accessToken = SettingKeeper.readAccessToken().getToken();
        Weibo weibo = weiboList.get(weiboList.size() - 1);
        App.weiboApi.homeTimeline(accessToken, 0L, weibo.getId(), 0, 0).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Timeline>() {
            @Override
            public void onCompleted() {
                TextUtil.changeNow();
                srl.setRefreshing(false);
                notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                srl.setRefreshing(false);
                Toast.makeText(activity, "出错啦，错误信息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Timeline timeline) {
                List<Weibo> statuses = timeline.getStatuses();
                if (statuses.isEmpty()) {
                    Toast.makeText(activity, "没有更多微博了", Toast.LENGTH_SHORT).show();
                } else {
                    weiboList.addAll(statuses);
                }
            }
        });
    }

}
