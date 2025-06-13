package com.openhis.common.utils;

import java.util.Arrays;

import com.openhis.common.enums.HisEnumInterface;

public class EnumUtils {

    /**
     * 根据 value 获取枚举的 info
     *
     * @param enumClass 枚举类
     * @param value 枚举的 value
     * @param <E> 枚举类型
     * @return 对应的 info，如果未找到则返回 null
     */
    public static <E extends Enum<E> & HisEnumInterface> String getInfoByValue(Class<E> enumClass, Integer value) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getValue().equals(value)).findFirst()
            .map(HisEnumInterface::getInfo).orElse(null);
    }

    /**
     * 根据 code 获取枚举的 info
     *
     * @param enumClass 枚举类
     * @param code 枚举的 code
     * @param <E> 枚举类型
     * @return 对应的 info，如果未找到则返回 null
     */
    public static <E extends Enum<E> & HisEnumInterface> String getInfoByCode(Class<E> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            .map(HisEnumInterface:: getInfo)
            .orElse(null);
    }

}
