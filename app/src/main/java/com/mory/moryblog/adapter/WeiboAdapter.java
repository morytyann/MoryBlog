package com.mory.moryblog.adapter;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.activity.WeiboDetailActivity;
import com.mory.moryblog.biz.WeiboBiz;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;

import java.util.ArrayList;

import rx.Observable;
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
    private ArrayList<Weibo> weibos;
    private int resource;

    public WeiboAdapter(MainActivity activity, LinearLayoutManager manager, SwipeRefreshLayout srl, ArrayList<Weibo> weibos, int resource) {
        this.weibos = weibos;
        this.resource = resource;
        this.manager = manager;
        this.srl = srl;
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
        if (manager.findLastVisibleItemPosition() == getItemCount() - 2 && !Constant.IS_FRESHING) {
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
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(WeiboBiz.refreshWeibo(activity, Constant.TYPE_LOAD_MORE));
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Constant.IS_FRESHING = false;
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Constant.IS_FRESHING = false;
                        srl.setRefreshing(false);
                        Toast.makeText(activity, "出错啦，错误信息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        switch (integer) {
                            case Constant.FRESH_SUCCESS: // 刷新成功
                                notifyDataSetChanged();
                                break;
                            case Constant.FRESH_NO_NEW: // 刷新成功但没有新微博
                                Toast.makeText(activity, "没有更多微博了", Toast.LENGTH_SHORT).show();
                                break;
                            case Constant.FRESH_FAILED: // 刷新失败
                                Toast.makeText(activity, "刷新失败", Toast.LENGTH_SHORT).show();
                                break;
                            case Constant.FRESHING: // 正在刷新
                                Toast.makeText(activity, "正在刷新...", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
}
