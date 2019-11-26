package com.mory.moryblog.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.mory.moryblog.App;
import com.mory.moryblog.R;
import com.mory.moryblog.adapter.CommentAdapter;
import com.mory.moryblog.adapter.WeiboViewHolder;
import com.mory.moryblog.biz.WeiboBiz;
import com.mory.moryblog.entity.Comment;
import com.mory.moryblog.entity.Comments;
import com.mory.moryblog.entity.Weibo;
import com.mory.moryblog.util.Constant;
import com.mory.moryblog.util.SettingKeeper;
import com.mory.moryblog.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeiboDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvComments;
    private List<Comment> comments;
    private Weibo weibo;
    private CommentAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_detail);

        weibo = (Weibo) getIntent().getSerializableExtra("weibo");
        if (weibo == null) {
            return;
        }

        setViews();
        getData();
    }

    private void getData() {
        String token = SettingKeeper.readAccessToken().getToken();
        long id = weibo.getId();
        comments = new ArrayList<>();

        App.weiboApi.commentWeibo(token, id, 0, 0).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Comments>() {

            @Override
            public void onCompleted() {
                TextUtil.changeNow();
                if (rvAdapter != null) {
                    rvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Constant.TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            public void onNext(Comments comments) {
                WeiboDetailActivity.this.comments.addAll(comments.getComments());
                WeiboDetailActivity.this.showComments();
            }

        });
    }

    private void setViews() {
        // rv
        rvComments = findViewById(R.id.rvComments);
        rvComments.setItemAnimator(new DefaultItemAnimator());
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        // 设置标题
        Toolbar toolbar = findViewById(R.id.toolbarWeiboDetail);
        if (toolbar != null) {
            toolbar.setOnClickListener(this);
            setSupportActionBar(toolbar);
        }
        // 显示微博
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(this, R.layout.list_item_weibo);
        WeiboBiz.showWeibo(this, weibo, new WeiboViewHolder(header), -1);
        header.attachTo(rvComments);
    }

    private void showComments() {
        // 显示评论
        rvAdapter = new CommentAdapter(this, comments, R.layout.list_item_comment);
        rvComments.setAdapter(rvAdapter);
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
