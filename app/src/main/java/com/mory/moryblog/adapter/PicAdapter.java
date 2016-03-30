package com.mory.moryblog.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mory.moryblog.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mory on 2016/3/28.
 * 自定义adapter用于显示图片
 */
public class PicAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    private ArrayList<String> pic_urls;
    private LayoutInflater inflater;
    private int resource;

    public PicAdapter(AppCompatActivity activity, ArrayList<String> pic_urls, int resource) {
        this.activity = activity;
        this.pic_urls = pic_urls;
        this.resource = resource;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pic_urls.size();
    }

    @Override
    public Object getItem(int position) {
        return pic_urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivPic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(activity).load(pic_urls.get(position)).into(holder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
