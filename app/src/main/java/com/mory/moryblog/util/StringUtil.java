package com.mory.moryblog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mory on 2016/3/29.
 * 字符串工具类
 * 格式化时间
 * 提供可读的文本
 */
public class StringUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
    private static SimpleDateFormat parse = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
    private static SimpleDateFormat shortFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private static Date date0;

    public static String getReadableTime(String time) throws ParseException {
        Date date = parse.parse(time);
        long timeLag = date0.getTime() - date.getTime();
        long minute = (timeLag / 60000) + 1;
        long day = timeLag / 60000 / 60 / 24;
        if (minute < 60) {
            return minute + "分钟前";
        } else {
            switch ((int) day) {
                case 0:
                    return "今天" + shortFormat.format(date);
                case 1:
                    return "昨天" + shortFormat.format(date);
                case 2:
                    return "前天" + shortFormat.format(date);
                default:
                    return format.format(date);
            }
        }
    }

    public static void init() {
        date0 = new Date();
    }
}
