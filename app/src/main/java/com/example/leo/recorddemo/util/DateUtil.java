package com.example.leo.recorddemo.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间格式化工具类
 * Created by 梦影 on 2015/7/30.
 */
public class DateUtil {
    public static final String DATA_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_YYYY_MM_DD_HH_MM_SS2 = "yyyyMMddHHmmss";
    public static final String DATA_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATA_YYYY_MM_DD_HH_MM2 = "yyyy年MM月dd日 HH:mm";
    public static final String DATA_YYYY_MM_DD_HH_MM3 = "yyyy/MM/dd HH:mm";
    public static final String DATA_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATA_YYYY_MM_DD2 = "yyyy年MM月dd日";
    public static final String DATA_YYYY_MM_DD3 = "yyyy.MM.dd";
    public static final String DATA_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String DATA_MM_DD = "MM-dd";
    public static final String DATA_MM_DD1 = "MM/dd";
    public static final String DATA_MM_DD2 = "MM月dd日";
    public static final String DATA_MM_DD3 = "MM月-dd";
    public static final String DATA_HH_MM = "HH:mm";
    public static final String DATA_HH_MM_SS = "HH:mm:ss";

    public static String format(String dataSecond, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dataSecond + "000")));
    }

    public static String stringdate(Long dateString) {
        String format = "MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String stringdate1(Long dateString) {
        String format = "MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String stringdate5(Long dateString) {
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String stringdate3(Long dateString) {
        String format = "yyyy年MM月dd日";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String stringdate4(Long dateString) {
        String format = "MM月dd日";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String stringdate7(Long dateString) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String date_yyyy_mm_dd(String dateString) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String yyyy_m_dHM(String dateString) {
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String dateMmDdHhMm(String dateString) {
        String format = "MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    public static String dateMM_dd_hh_mm(String dateString) {
        String format = "MM月dd日 HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(dateString + "000")));
    }

    /**
     * 格式化时间
     *
     * @param time yyyy-MM-dd HH:mm
     * @return
     */
    public static String formatDateTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (TextUtils.isEmpty(time)) {
                return "";
            }
            Date date = null;
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar current = Calendar.getInstance();
            Calendar today = Calendar.getInstance();//今天
            today.set(Calendar.YEAR, current.get(Calendar.YEAR));
            today.set(Calendar.MONTH, current.get(Calendar.MONTH));
            today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
            //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);

            Calendar yesterday = Calendar.getInstance();//昨天
            yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
            yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
            yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
            yesterday.set(Calendar.HOUR_OF_DAY, 0);
            yesterday.set(Calendar.MINUTE, 0);
            yesterday.set(Calendar.SECOND, 0);
            current.setTime(date);
            if (current.after(today)) {
                return "今天 " + time.split(" ")[1];
            } else if (current.before(today) && current.after(yesterday)) {
                return "昨天 " + time.split(" ")[1];
            } else {
                int index = time.indexOf("-") + 1;
                return time.substring(index, time.length());
            }
        } catch (Exception e) {
            return "";
        }
    }

    /*
    * 评论时间格式化
    * */
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    public static String formatPostTime(String time) {
        try {
            String preFormat = format(time, DATA_YYYY_MM_DD_HH_MM_SS);
            SimpleDateFormat format = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS, Locale.CHINA);
            Date date = format.parse(preFormat);
            long delta = new Date().getTime() - date.getTime();
            if (delta < ONE_MINUTE) {
                long seconds = toSeconds(delta);
                return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
            } else if (delta < 45L * ONE_MINUTE) {
                long minutes = toMinutes(delta);
                return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
            } else if (delta < 24L * ONE_HOUR) {
                long hours = toHours(delta);
                return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
            } else {
                return preFormat.split("\\s+")[0];
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
    * 计算时间差
    * */
    public static String analyzeTimeDifference(long currentTimes, String endTime) {
        long endTimes = Long.parseLong(endTime + "000");
        if (endTimes > currentTimes) {
            long diff = endTimes - currentTimes;
            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            return day + "天" + hour + "小时";
        } else {
            return "已结束";
        }
    }

    /*
    * 判断两个时间戳是否同一天-毫秒级
    * */
    public static boolean isSameDay(long ms1, long ms2) {
        if (ms1 == 0L || ms2 == 0L)
            return false;
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
    }

    /**
     * 天数计算
     *
     * @return
     */
    public static String days(String begin_time, String end_time) {
        int timediff = Integer.parseInt(end_time) - Integer.parseInt(begin_time);
        int days = timediff / 86400;
        return String.valueOf(days);
    }

    private static final int SECONDS_IN_DAY = 60 * 60 * 24;
    private static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    /*
    * 计算时间差(毫秒级)
    * */
    public static String analyzeTimeDiff(long duration, String format) {
        // 毫秒
        long ssec = duration % 1000;
        // 秒
        long sec = (duration / 1000) % 60;
        // 分钟
        long min = (duration / 1000 / 60) % 60;
        // 小时
        long hour = (duration / 1000 / 60 / 60) % 24;
        // 天
        long day = duration / 1000 / 60 / 60 / 24;

        return format.replace("D", String.format(Locale.CHINA, "%02d", day))
                .replace("H", String.format(Locale.CHINA, "%02d", hour))
                .replace("M", String.format(Locale.CHINA, "%02d", min))
                .replace("S", String.format(Locale.CHINA, "%02d", sec));
    }

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }


    public static String analyzeTimeDiff(long duration) {
        // 毫秒
        long ssec = duration % 1000;
        // 秒
        long sec = (duration / 1000) % 60;
        // 分钟
        long min = (duration / 1000 / 60) % 60;
        // 小时
        long hour = (duration / 1000 / 60 / 60) % 24;
        // 天
        long day = duration / 1000 / 60 / 60 / 24;
        if (day > 0) {
            return day + "天" + hour + "小时";
        } else if (hour > 0) {
            return hour + "小时" + min + "分钟";
        } else if (min > 0) {
            return min + "分钟";
        } else {
            return sec + "秒";
        }
    }
}
