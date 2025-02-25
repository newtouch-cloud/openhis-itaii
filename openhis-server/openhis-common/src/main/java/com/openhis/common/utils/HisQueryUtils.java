package com.openhis.common.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.CommonConstants;

/**
 * His查询工具类
 */
public class HisQueryUtils {

    /**
     * 条件查询构造器
     *
     * @param entity 传参实体
     * @param searchKey 模糊查询关键字
     * @param searchFields 支持模糊查询的字段集合 ; 不需要模糊查询传 null 即可
     * @param request 请求
     * @return 构造条件
     */
    public static <T> QueryWrapper<T> buildQueryWrapper(T entity, String searchKey, HashSet<String> searchFields,
        HttpServletRequest request) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 添加租户id查询条件
        queryWrapper.eq(CommonConstants.Common.TENANT_ID, getCurrentTenantId());
        if (entity == null) {
            return queryWrapper;
        }
        // 反射获取实体类的字段
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null && !value.toString().equals("")) {
                    // String fieldName = field.getName();
                    // 将驼峰命名的字段名转换为下划线命名的数据库字段名
                    String fieldName = camelToUnderline(field.getName());
                    // 处理等于条件
                    queryWrapper.eq(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 处理模糊查询关键字
        if (searchKey != null && !searchKey.isEmpty() && searchFields != null && !searchFields.isEmpty()) {
            queryWrapper.and(wrapper -> {
                for (String field : searchFields) {
                    wrapper.or().like(field, searchKey);
                }
            });
        }
        // 处理时间段查询
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD);
        // DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            // 检查参数名是否以 "STime" 或 "ETime" 结尾
            if (paramName.endsWith(CommonConstants.Common.S_TIME)
                || paramName.endsWith(CommonConstants.Common.E_TIME)) {
                // 提取字段名（去掉 "STime" 或 "ETime" 后缀）
                String fieldName = paramName.substring(0, paramName.length() - 5);
                // 驼峰转下划线
                String dbFieldName = camelToUnderline(fieldName);
                // 获取对应的 STime 和 ETime 值
                String startValue = getParameterValue(request, fieldName + CommonConstants.Common.S_TIME);
                String endValue = getParameterValue(request, fieldName + CommonConstants.Common.E_TIME);
                // 如果 Start 和 End 都有值，则添加时间段查询条件
                if (startValue != null && endValue != null) {
                    try {
                        SimpleDateFormat dateFormat;
                        if (isValidFormat(formatter, startValue)) {
                            dateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD);
                        } else {
                            dateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS);
                        }
                        Date startDate = dateFormat.parse(startValue);
                        Date endDate = dateFormat.parse(endValue);
                        queryWrapper.ge(dbFieldName, startDate); // 大于等于 STime
                        queryWrapper.le(dbFieldName, endDate); // 小于等于 ETime
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 检查时间字符串是否符合指定格式
     *
     * @param formatter 时间格式
     * @param dateStr 时间字符串
     * @return 是否匹配
     */
    private static boolean isValidFormat(DateTimeFormatter formatter, String dateStr) {
        try {
            formatter.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 从 request 中获取参数值
     */
    private static String getParameterValue(HttpServletRequest request, String paramName) {
        String[] values = request.getParameterValues(paramName);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    /**
     * 将驼峰命名的字段名转换为下划线命名的数据库字段名
     */
    private static String camelToUnderline(String camel) {
        StringBuilder result = new StringBuilder();
        for (char c : camel.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 获取当前租户 ID
     */
    private static Integer getCurrentTenantId() {
        // 获取当前登录用户的租户 ID
        if (SecurityUtils.getAuthentication() != null) {
            return SecurityUtils.getLoginUser().getTenantId();
        }
        return 0;
    }

}
