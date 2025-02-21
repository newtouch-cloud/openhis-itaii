package com.core.generator.util;

import java.util.Arrays;

import org.apache.commons.lang3.RegExUtils;

import com.core.common.constant.GenConstants;
import com.core.common.utils.StringUtils;
import com.core.generator.config.GenConfig;
import com.core.generator.domain.GenTable;
import com.core.generator.domain.GenTableColumn;

/**
 * 代码生成器 工具类
 * 
 * @author system
 */
public class GenUtils {
    /**
     * 初始化表信息
     */
    public static void initTable(GenTable genTable, String operName) {
        String tablePrefix = genTable.getTableName().split("_")[0];
        switch (tablePrefix) {
            case "cod":
                genTable.setPackageName(GenConfig.getPackageName() + ".code");
                break;
            case "adm":
                genTable.setPackageName(GenConfig.getPackageName() + ".administration");
                break;
            case "cli":
                genTable.setPackageName(GenConfig.getPackageName() + ".clinical");
                break;
            case "dia":
                genTable.setPackageName(GenConfig.getPackageName() + ".diagnostic");
                break;
            case "med":
                genTable.setPackageName(GenConfig.getPackageName() + ".medication");
                break;
            case "wor":
                genTable.setPackageName(GenConfig.getPackageName() + ".workflow");
                break;
            case "fin":
                genTable.setPackageName(GenConfig.getPackageName() + ".financial");
                break;
            case "def":
                genTable.setPackageName(GenConfig.getPackageName() + ".definition");
                break;
            case "doc":
                genTable.setPackageName(GenConfig.getPackageName() + ".document");
                break;
            default:
                genTable.setPackageName(GenConfig.getPackageName() + ".errortable");
        }
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setModuleName(getModuleName(GenConfig.getPackageName()));
        genTable.setBusinessName(getBusinessName(genTable.getTableName()));
        genTable.setFunctionName(replaceText(genTable.getTableComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        genTable.setCreateBy(operName);
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumn column, GenTable table) {
        String dataType = column.getColumnType();// getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getTableId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(StringUtils.toCamelCase(columnName));
        // 设置默认类型
        column.setJavaType(GenConstants.TYPE_STRING);
        column.setQueryType(GenConstants.QUERY_EQ);

        if ("int8".equals(dataType)) {
            column.setJavaType(GenConstants.TYPE_LONG);
        } else if ("int4".equals(dataType)) {
            column.setJavaType(GenConstants.TYPE_INTEGER);
        } else if ("timestamptz".equals(dataType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
        } else if ("numeric".equals(dataType)) {
            column.setJavaType(GenConstants.TYPE_BIGDECIMAL);
        } else if ("timestamp".equals(dataType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
        }
        // 插入字段（默认所有字段都需要插入）
        column.setIsInsert(GenConstants.REQUIRE);
    }

    /**
     * 校验数组是否包含指定值
     * 
     * @param arr 数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取模块名
     * 
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    /**
     * 获取业务名
     * 
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        return StringUtils.substring(tableName, lastIndex + 1, nameLength);
    }

    /**
     * 表名转换成Java类名
     * 
     * @param tableName 表名称
     * @return 类名
     */
    public static String convertClassName(String tableName) {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix)) {
            String[] searchList = StringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase(tableName);
    }

    /**
     * 批量替换前缀
     * 
     * @param replacementm 替换值
     * @param searchList 替换列表
     * @return
     */
    public static String replaceFirst(String replacementm, String[] searchList) {
        String text = replacementm;
        for (String searchString : searchList) {
            if (replacementm.startsWith(searchString)) {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    /**
     * 关键字替换
     * 
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表|源)", "");
    }

    /**
     * 获取数据库类型字段
     * 
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     * 
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }
}
