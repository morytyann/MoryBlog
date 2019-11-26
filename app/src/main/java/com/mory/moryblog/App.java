package com.mory.moryblog;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mory.moryblog.net.WeiboApi;
import com.mory.moryblog.util.SettingKeeper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    public static Gson gson;
    public static WeiboApi weiboApi;

    @Override
    public void onCreate() {
        super.onCreate();

        initNetwork();
        SettingKeeper.init(this);
    }

    private void initNetwork() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .setDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
                .create();

        weiboApi = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.weibo.com")
                .client(okHttpClient)
                .build().create(WeiboApi.class);
    }
}
