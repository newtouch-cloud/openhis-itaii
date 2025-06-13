package com.openhis.common.enums.ybenums;

/**
 * UsedFrquEnum
 *
 * @author Wuser
 * @date 2025/4/21
 */
import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用频次枚举
 *
 * @author YourName
 * @date 2025-04-21
 */
@Getter
@AllArgsConstructor
public enum YbUsedFrqu {

    DAILY_ONCE("11", "每天一次 (qd)"),

    DAILY_TWICE("12", "每天二次 (bid)"),

    DAILY_THRICE("13", "每天三次 (tid)"),

    DAILY_FOUR_TIMES("14", "每天四次 (qid)"),

    WEEKLY_ONCE("21", "每周一次 (qw)"),

    WEEKLY_TWICE("22", "每周二次 (biw)"),

    WEEKLY_THRICE("23", "每周三次 (tiw)"),

    EVERY_TWO_WEEKS("24", "每两周一次 (q2w)"),

    EVERY_HOUR("31", "每小时一次 (qh)"),

    EVERY_TWO_HOURS("32", "每2小时一次 (q2h)"),

    EVERY_FOUR_HOURS("33", "每4小时一次 (q4h)"),

    EVERY_FIVE_HOURS("34", "每5小时一次 (q5h)"),

    EVERY_SIX_HOURS("35", "每6小时一次 (q6h)"),

    EVERY_EIGHT_HOURS("36", "每8小时一次 (q8h)"),

    EVERY_TWELVE_HOURS("37", "每12小时一次 (q12h)"),

    EVERY_NIGHT("41", "每晚一次 (qn)"),

    EVERY_OTHER_DAY("42", "隔天一次 (qod)"),

    EVERY_FIVE_DAYS("43", "五天一次 (q5d)"),

    EVERY_TEN_DAYS("44", "十天一次 (q10d)"),

    EVERY_THREE_DAYS("45", "隔三日一次 (q3d)"),

    TWELVE_HOUR_MAINTAIN("51", "12小时维持"),

    TWENTY_FOUR_HOUR_MAINTAIN("52", "24小时维持"),

    IMMEDIATELY("61", "立即 (st)"),

    WHEN_NEEDED("62", "必要时使用 (prn)"),

    ONCE("63", "一次 (once)"),

    MONTHLY_ONCE("71", "每月一次 (qm)"),

    MONTHLY_TWICE("72", "每月两次 (bim)"),

    EVERY_THREE_MONTHS("73", "每三个月一次 (q3m)"),

    EVERY_SIX_MONTHS("74", "每六个月一次 (q6m)");

    private String value;
    private String description;

    public static YbUsedFrqu getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbUsedFrqu frqu : values()) {
            if (frqu.getValue().equals(value)) {
                return frqu;
            }
        }
        return null;
    }
}