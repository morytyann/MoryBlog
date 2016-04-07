package com.mory.moryblog.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Mory on 2016/4/7.
 * 图片的工具类
 * 1.显示图片
 * 2.保存图片
 */
public class ImageUtil {
    /**
     * 此乃显示Gif的神秘代码
     *
     * @param activity 当前Activity
     * @param imgUrl   图片地址
     * @param view     GifImageView
     */
    public static void showGif(final Activity activity, final String imgUrl, final GifImageView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        Observable.create(new Observable.OnSubscribe<GifDrawable>() {
            @Override
            public void call(Subscriber<? super GifDrawable> subscriber) {
                try {
                    subscriber.onNext(new GifDrawable(HttpUtil.getHttpBytes(imgUrl)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GifDrawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constant.TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(GifDrawable gifDrawable) {
                        view.setImageDrawable(gifDrawable);
                    }
                });
    }

    /**
     * 此乃显示普通图片的方法（同时监听点击图片退出）
     *
     * @param activity 当前Activity
     * @param imgUrl   图片地址
     * @param view     PhotoView
     */
    public static void showPhoto(final Activity activity, String imgUrl, PhotoView view) {
        view.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                activity.finish();
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        Picasso.with(activity).load(imgUrl).into(view);
    }
}
