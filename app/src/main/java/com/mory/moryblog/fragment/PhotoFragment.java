package com.mory.moryblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mory.moryblog.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Mory on 2016/3/29.
 * 用于显示图片
 */
public class PhotoFragment extends Fragment {
    private String pic_url;

    public PhotoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        PhotoView ivPhoto = (PhotoView) v.findViewById(R.id.ivPhoto);
        ivPhoto.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().finish();
            }
        });
        Picasso.with(getContext()).load(pic_url).into(ivPhoto);
        ivPhoto.setMaximumScale(8f);
        ivPhoto.setMediumScale(4f);
        return v;
    }

    @Override
    public void setArguments(Bundle args) {
        pic_url = args.getString("pic_url").replace("thumbnail", "bmiddle");
    }
}
