package com.mory.moryblog.listener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mory.moryblog.R;
import com.mory.moryblog.activity.MainActivity;
import com.mory.moryblog.activity.PhotoActivity;

import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/29.
 * 监听查看大图的点击
 */
public class OnPicClickListener implements View.OnClickListener {
    private AppCompatActivity activity;

    public OnPicClickListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> pic_urls = (ArrayList<String>) v.getTag(R.id.tag0);
        int position = (int) v.getTag(R.id.tag_1);
        activity.startActivity(new Intent(activity, PhotoActivity.class).putExtra("position", position).putStringArrayListExtra("pic_urls", pic_urls));
    }
}
