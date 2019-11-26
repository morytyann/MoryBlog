package com.mory.moryblog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mory on 2016/3/29.
 * <p>
 * 文本工具类
 */
public class TextUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
    private static SimpleDateFormat shortFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private static Date now;

    public static void changeNow() {
        now = new Date();
    }

    public static String getReadableTime(Date date) {
        long timeLag = now.getTime() - date.getTime();
        long minute = (timeLag / 1000 / 60) + 1;
        long day = timeLag / 1000 / 60 / 60 / 24;
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
}
