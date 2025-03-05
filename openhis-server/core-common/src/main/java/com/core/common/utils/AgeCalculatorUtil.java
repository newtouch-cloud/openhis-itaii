package com.core.common.utils;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 根据出生日期算年龄
 *
 * @author liuhr
 * @date 2025/2/24
 */
public final class AgeCalculatorUtil {

    /**
     * 当前年龄取得（床位列表表示年龄用）
     */
    public static String getAge(Date date) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear() - dateTime.getYear();
        if (years > 2) {
            return String.format("%d岁", years);
        }

        Period period = Period.between(dateTime.toLocalDate(), now.toLocalDate());
        int months = period.getMonths();
        int days = period.getDays();
        long hours = ChronoUnit.HOURS.between(dateTime, now) - (days * 24);

        if (hours < 0) {
            hours += 24;
            days--;
        }
        if (days < 0) {
            months--;
            days = getLastDayOfMonth(dateTime) - dateTime.getDayOfMonth() + now.getDayOfMonth();
        }
        if (months < 0) {
            months += 12;
            years--;
        }
        if (years < 0) {
            return "1小时";
        }

        if (years > 0 && months > 0) {
            return String.format("%d岁%d月", years, months);
        }
        if (years > 0) {
            return String.format("%d岁", years);
        }
        if (months > 0 && days > 0) {
            return String.format("%d月%d天", months, days);
        }
        if (months > 0) {
            return String.format("%d月", months);
        }
        if (days > 0 && hours > 0) {
            return String.format("%d天%d小时", days, hours);
        }
        if (days > 0) {
            return String.format("%d天", days);
        }
        if (hours > 0) {
            return String.format("%d小时", hours);
        }
        return "1小时";
    }

    private static int getLastDayOfMonth(LocalDateTime dateTime) {
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(dateTime.getYear()) && dateTime.getMonthValue() == 2) {
            return 29;
        }
        return daysInMonth[dateTime.getMonthValue()];
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}