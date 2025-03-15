/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.core.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.core.common.constant.CacheConstants;
import com.core.common.core.redis.RedisCache;
import com.core.common.exception.UtilException;

/**
 * 排番组件
 *
 * @author zxy
 * @date 2024-11-29
 */
@Component
public final class AssignSeqUtil {
    private final RedisCache redisCache;

    // 缓存Key格式（包含前缀）
    private static final String FORMAT_CACHE_KEY = "assign-seq:%s";

    // 缓存Key格式（包含前缀）
    private static final String FORMAT_CACHE_KEY_BY_DAY = "assign-seq:%s:%s";

    // 默认序号长度
    private static final int DEFAULT_SUFFIX_LENGTH = 8;

    // 默认序号长度（每日重新排番）
    private static final int DEFAULT_SUFFIX_LENGTH_DAY = 4;

    // 编号格式前缀
    private static final String FORMAT_SEQ_PREFIX = "%s%0";

    // 编号格式前缀
    private static final String FORMAT_SEQ_PREFIX_DAY = "%s%s%0";

    // 编号格式后缀
    private static final String FORMAT_SEQ_SUFFIX = "d";

    // 失效时长（48小时）
    private static final int EXPIRE_SECONDS = 172800;

    private AssignSeqUtil(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    private static final Logger log = LoggerFactory.getLogger(AssignSeqUtil.class);

    /**
     * 编号排番
     *
     * @param prefix 编号前缀
     * @return 番号（示例：XXX00000001）
     */
    public String getSeq(String prefix) {
        return this.getSeq(prefix, DEFAULT_SUFFIX_LENGTH);
    }

    /**
     * 编号排番
     *
     * @param prefix 编号前缀
     * @param seqLength 序号长度（去除前缀后位数）
     * @return 番号（示例：XXX00000001）
     */
    public String getSeq(String prefix, int seqLength) {
        return String.format(FORMAT_SEQ_PREFIX + seqLength + FORMAT_SEQ_SUFFIX, prefix, this.getSeqNo(prefix));
    }

    /**
     * 编号排番（批量）
     *
     * @param prefix 编号前缀
     * @param seqLength 序号长度（去除前缀后位数）
     * @param count 排番数量
     * @return 番号（示例：XXX00000001，XXX00000002）
     */
    public List<String> getSeq(String prefix, int seqLength, int count) {

        // 获取最新编号
        int seqEnd = this.getSeqNoCore(String.format(FORMAT_CACHE_KEY, prefix), count);

        // 返回番号集合
        List<String> seqS = new ArrayList<>();
        long seq = seqEnd - count + 1;
        while (seq <= seqEnd) {
            // 拼接番号
            seqS.add(String.format(FORMAT_SEQ_PREFIX + seqLength + FORMAT_SEQ_SUFFIX, prefix, seq++));
        }
        return seqS;
    }

    /**
     * 编号排番
     *
     * @param key 编号主键
     * @return 番号（示例：XXX00000001）
     */
    public int getSeqNo(String key) {
        return this.getSeqNoCore(String.format(FORMAT_CACHE_KEY, key), 1);
    }

    /**
     * 编号排番（批量）
     *
     * @param key 编号主键
     * @param count 排番数量
     * @return 番号（示例：XXX00000001，XXX00000002）
     */
    public List<Integer> getSeqNo(String key, int count) {

        // 获取最新编号
        int seqEnd = this.getSeqNoCore(String.format(FORMAT_CACHE_KEY, key), count);

        // 返回番号集合
        List<Integer> seqS = new ArrayList<>();
        int seq = seqEnd - count + 1;
        while (seq <= seqEnd) {
            // 拼接番号
            seqS.add(seq++);
        }
        return seqS;
    }

    /**
     * 编号排番（每日重新排番）
     *
     * @param prefix 编号前缀
     * @return 番号
     */
    public String getSeqByDay(String prefix) {
        return this.getSeqByDay(prefix, DEFAULT_SUFFIX_LENGTH_DAY);
    }

    /**
     * 编号排番（每日重新排番）
     *
     * @param prefix 编号前缀
     * @param seqLength 序号长度（去除前缀后位数）
     * @return 番号
     */
    public String getSeqByDay(String prefix, int seqLength) {
        // 获取当前日期
        String today = DateUtils.today();

        // 获取最新编号
        int seq = this.getSeqNoByDay(prefix, today, 1);

        // 拼接番号并返回
        return String.format(FORMAT_SEQ_PREFIX_DAY + seqLength + FORMAT_SEQ_SUFFIX, prefix, today, seq);
    }

    /**
     * 编号排番（每日重新排番）（批量）
     *
     * @param prefix 编号前缀
     * @param seqLength 序号长度（去除前缀后位数）
     * @param count 排番数量
     * @return 番号（最小值）
     */
    public List<String> getSeqByDay(String prefix, int seqLength, int count) {

        // 获取当前日期
        String today = DateUtils.today();

        // 获取最新编号
        int seqEnd = this.getSeqNoByDay(prefix, today, count);

        // 返回番号集合
        List<String> seqS = new ArrayList<>();
        long seq = seqEnd - count + 1;
        while (seq <= seqEnd) {
            // 拼接番号
            seqS.add(String.format(FORMAT_SEQ_PREFIX_DAY + seqLength + FORMAT_SEQ_SUFFIX, prefix, today, seq++));
        }
        return seqS;
    }

    /**
     * 编号排番（每日重新排番）
     *
     * @param prefix 编号前缀
     * @return 番号
     */
    public int getSeqNoByDay(String prefix) {
        return this.getSeqNoByDay(prefix, DateUtils.today(), 1);
    }

    /**
     * 编号排番（每日重新排番）（批量）
     *
     * @param prefix 编号前缀
     * @param count 排番数量
     * @return 番号（最小值）
     */
    public List<Integer> getSeqNoByDay(String prefix, int count) {

        // 获取当前日期
        String today = DateUtils.today();

        // 获取最新编号
        int seqEnd = this.getSeqNoByDay(prefix, today, count);

        // 返回番号集合
        List<Integer> seqS = new ArrayList<>();
        int seq = seqEnd - count + 1;
        while (seq <= seqEnd) {
            // 拼接番号
            seqS.add(seq++);
        }
        return seqS;
    }

    /**
     * 编号排番（每日重新排番）
     *
     * @param prefix 编号前缀
     * @param date 序号长度（去除前缀后位数）
     * @param count 排番数量
     * @return 番号
     */
    private int getSeqNoByDay(String prefix, String date, int count) {

        // 拼接缓存Key（包含当日日期）
        String cacheKeyByDay = String.format(FORMAT_CACHE_KEY_BY_DAY, prefix, date);

        // 获取最新编号
        int seq = this.getSeqNoCore(cacheKeyByDay, count);

        // 当最小编号为1时，设置保存期限
        if (seq - count + 1 == 1) {
            redisCache.expire(cacheKeyByDay, EXPIRE_SECONDS);
        }

        // 拼接番号并返回
        return seq;
    }

    /**
     * 编号排番（批量）
     *
     * @param cacheKey 缓存Key
     * @param count 排番数量
     * @return 番号（示例：XXX00000001，XXX00000002）
     */
    private int getSeqNoCore(String cacheKey, int count) {

        // 获取最新编号
        long seq = redisCache.incr(cacheKey, count);

        // 达到最大值时重置番号
        if (seq >= Integer.MAX_VALUE) {
            log.error(CacheConstants.ASSIGN_SEQ_FAILED, DateUtils.getStrYmdHmsRead(), cacheKey);
            throw new UtilException("排番失败！");
        }

        // 番号返回
        return (int)seq;
    }

    /**
     * 格式化字符串，支持动态调整数字部分的位数。
     *
     * @param baseStr 基础字符串
     * @param num 数字部分的值
     * @param numDigits 数字部分的位数
     * @return 格式化后的字符串,基串.0001的格式,后面几位数自己动态设置
     */
    public static String formatString(String baseStr, Long num, int numDigits) {
        // 使用 String.format 动态生成格式化字符串
        String formatPattern = "%s.%0" + numDigits + "d";
        return String.format(formatPattern, baseStr, num);
    }

}
