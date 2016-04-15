package com.mory.moryblog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.mory.moryblog.R;
import com.mory.moryblog.adapter.CommentAdapter;
import com.mory.moryblog.adapter.WeiboViewHolder;
import com.mory.moryblog.biz.CommentBiz;
import com.mory.moryblog.biz.WeiboBiz;
import com.mory.moryblog.entity.Comment;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeiboDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvComments;
    private ArrayList<Comment> comments;
    private Weibo weibo;
    private int position;
    private CommentAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_detail);
        getData();
        setViews();
    }

    private void getData() {
        position = getIntent().getIntExtra("position", 0);
        weibo = Constant.weibos.get(position);
        comments = new ArrayList<>();
        Observable.create(new Observable.OnSubscribe<ArrayList<Comment>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Comment>> subscriber) {
                subscriber.onNext(CommentBiz.loadComment(WeiboDetailActivity.this, weibo));
                subscriber.onCompleted();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArrayList<Comment>>() {
                    @Override
                    public void onCompleted() {
                        rvAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constant.TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Comment> comments) {
                        WeiboDetailActivity.this.comments.clear();
                        WeiboDetailActivity.this.comments.addAll(comments);
                    }
                });
    }

    private void setViews() {
        // findViewById
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWeiboDetail);
        // 设置标题
        if (toolbar != null) {
            toolbar.setOnClickListener(this);
            setSupportActionBar(toolbar);
        }
        // 显示评论
        rvComments.setItemAnimator(new DefaultItemAnimator());
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new CommentAdapter(this, comments, R.layout.list_item_comment);
        rvComments.setAdapter(rvAdapter);
        // 显示微博
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(this, R.layout.list_item_weibo);
        WeiboBiz.showWeibo(this, weibo, new WeiboViewHolder(header), position);
        header.attachTo(rvComments);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbarWeiboDetail:
                rvComments.getLayoutManager().smoothScrollToPosition(rvComments, null, 0);
                break;
        }
    }
}
