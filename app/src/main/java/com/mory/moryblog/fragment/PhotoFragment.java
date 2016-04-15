package com.mory.moryblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mory.moryblog.R;
import com.mory.moryblog.util.ImageUtil;

import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Mory on 2016/3/29.
 * 用于显示图片
 */
public class PhotoFragment extends Fragment {
    private String pic_url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        PhotoView ivPhoto = (PhotoView) v.findViewById(R.id.ivPhoto);
        GifImageView givGifPhoto = (GifImageView) v.findViewById(R.id.givGifPhoto);
        // 是Gif时加载Gif，不是则按普通方式加载。
        if (pic_url.endsWith(".gif")) {
            ivPhoto.setVisibility(View.GONE);
            givGifPhoto.setVisibility(View.VISIBLE);
            ImageUtil.showGif(getActivity(), pic_url, givGifPhoto);
        } else {
            ivPhoto.setVisibility(View.VISIBLE);
            givGifPhoto.setVisibility(View.GONE);
            ImageUtil.showPhoto(getActivity(), pic_url, ivPhoto);
        }
        return v;
    }

    /**
     * 使用newInstance，而不是new来获得一个Fragment
     *
     * @param pic_url 图片地址
     * @return PhotoFragment的对象
     */
    public static PhotoFragment newInstance(String pic_url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("pic_url", pic_url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        String s = args.getString("pic_url");
        if (s != null) {
            this.pic_url = s.replace("thumbnail", "bmiddle");
        }
    }
}
