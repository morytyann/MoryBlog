package com.mory.moryblog.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mory on 2016/4/7.
 * Http工具类
 */
public class HttpUtil {
    public static byte[] getHttpBytes(String imgUrl) throws IOException {
        URL url = new URL(imgUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5 * 1000);
        InputStream is = connection.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        byte[] data = bos.toByteArray();
        bos.close();
        is.close();
        return data;
    }
}
