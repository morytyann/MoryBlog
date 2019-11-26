package com.mory.moryblog.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.mory.moryblog.R;
import com.mory.moryblog.listener.OnPicClickListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
     * 此乃显示普通图片的方法（同时监听点击事件）
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
        Picasso.get().load(imgUrl).into(view);
    }

    /**
     * 此乃显示九宫格的方法（同时监听点击事件）
     *
     * @param activity   当前Activity
     * @param imgUrls    图片地址集合
     * @param gridLayout 九宫格View
     * @param width      图片宽度
     * @param height     图片高度
     */
    public static void showPhotoOnGridLayout(final Activity activity, final List<String> imgUrls, final GridLayout gridLayout, final int width, final int height) {
        Observable.from(imgUrls).observeOn(Schedulers.io()).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                try {
                    return Picasso.get().load(s).resize(width, height).centerCrop().get();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Action1<Bitmap>() {
            private int i = 0;

            @Override
            public void call(Bitmap bitmap) {
                if (bitmap == null) {
                    return;
                }
                ImageView view = (ImageView) activity.getLayoutInflater().inflate(R.layout.grid_item_pic, gridLayout, false);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = width;
                params.height = height;
                view.setTag(R.id.tag0, imgUrls);
                view.setTag(R.id.tag1, i);
                view.setOnClickListener(new OnPicClickListener(activity));
                view.setImageBitmap(bitmap);
                gridLayout.addView(view);
                i++;
            }
        });
    }
}
