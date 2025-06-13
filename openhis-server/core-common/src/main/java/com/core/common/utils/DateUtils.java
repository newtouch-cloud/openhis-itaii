package com.core.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 * @author system
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String YMDHMS_FOR_READ = "yyyy年MM月dd日 HH时mm分ss秒";

    private static String[] parsePatterns =
        {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 获取当日日期
     *
     * @return 当日日期
     */
    public static final String today() {
        return dateTimeNow(YYYYMMDD);
    }

    /**
     * 获取当日日期（可读）
     * 
     * @return
     */
    public static final String getStrYmdHmsRead() {
        return dateTimeNow(YMDHMS_FOR_READ);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int)((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 计算 m 分钟后的时间
     *
     * @param dateTime 开始时间
     * @param m 计算分钟数
     * @return m分钟后的时间
     */
    public static Date addDateMinute(Date dateTime, Integer m) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.add(Calendar.MINUTE, m);
        return calendar.getTime();

    }

    /**
     * 获取传入日期的开始时间/结束日期
     *
     * @param strDate yyyy-MM-dd格式的字符串日期
     * @param flag  true：今天的开始时间，false：返回今天的结束时间
     * @return 今天的日期 00:00:00，或者今天的日期 23:59:59
     */
    public static LocalDateTime startDayOrEndDay(String strDate,boolean flag) {

        String startTimeStr = "00:00:00";
        String endTimeStr = "23:59:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        try {
            // 解析日期字符串
            LocalDateTime date = LocalDateTime.parse(strDate, formatter);
            // 解析开始时间字符串
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            // 解析结束时间字符串
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
            if(flag){
                return startTime;
            }else{
                return endTime;
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断日期是否为未来时间
     *
     * @param dateString 字符串日期
     * @return 是/否
     */
    public static boolean isFuture(String dateString) {
        // 创建 DateTimeFormatter 对象，并设置所需的日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        try {
            // 解析字符串为 LocalDate 对象
            LocalDate dateToCheck = LocalDate.parse(dateString, formatter);
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 检查日期是否是未来的时间
            return dateToCheck.isAfter(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析失败或其他异常，返回 false 或根据需要处理异常
            return false;
        }
    }

    /**
     * 判断日期是否为未来时间
     *
     * @param date Date 类型的日期
     * @return 是/否
     */
    public static boolean isFuture(Date date) {
        // 获取当前时间
        Date currentDate = Calendar.getInstance().getTime();
        // 检查传入的日期是否在当前时间之后
        return date.after(currentDate);
    }

    /**
     * 从身份证号码中提取生日
     *
     * @param idCard 身份证号
     * @return 出生日
     */
    public static Date extractBirthday(String idCard) {
        if (idCard == null) {
            // 身份证号码为空
            return null;
        }
        // 提取第7到14位作为生日字符串
        String birthdayStr = idCard.substring(6, 14);
        // 将生日字符串转换为 LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(birthdayStr, formatter);

        // 将 LocalDate 转换为 java.util.Date
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


}
