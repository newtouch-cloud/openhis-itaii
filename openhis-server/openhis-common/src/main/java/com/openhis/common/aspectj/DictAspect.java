package com.openhis.common.aspectj;

import java.lang.reflect.Field;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.annotation.Dict;

@Aspect
@Component
public class DictAspect {

    @Autowired
    private JdbcTemplate jdbcTemplate; // 使用 JdbcTemplate 执行 SQL

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || "
        + "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(); // 执行原方法

        if (result instanceof R) {
            // 如果返回值是 R<?>，提取其中的数据
            R<?> response = (R<?>)result;
            Object data = response.getData(); // 获取 R<?> 中的实际数据

            if (data instanceof Page) {
                // 如果数据是 Page 类型，处理分页数据
                Page<?> page = (Page<?>)data;
                List<?> records = page.getRecords();
                if (!records.isEmpty()) {
                    for (Object obj : records) {
                        processDict(obj); // 处理每个 DTO 对象
                    }
                }
            } else if (data instanceof List) {
                // 如果数据是 List 类型，处理列表数据
                List<?> list = (List<?>)data;
                if (!list.isEmpty()) {
                    for (Object obj : list) {
                        processDict(obj); // 处理每个 DTO 对象
                    }
                }
            } else {
                // 如果数据是单个 DTO 对象，直接处理
                processDict(data);
            }
        }

        return result;
    }

    private <T> void processDict(T dto) throws IllegalAccessException {
        if (dto == null) {
            return;
        }

        // 获取 DTO 类的所有字段
        for (Field field : dto.getClass().getDeclaredFields()) {
            // 检查字段是否带有 @Dict 注解
            if (field.isAnnotationPresent(Dict.class)) {
                Dict dictAnnotation = field.getAnnotation(Dict.class);
                field.setAccessible(true); // 设置字段可访问
                Object fieldValue = field.get(dto); // 获取字段值

                if (fieldValue != null) {
                    String dictCode = dictAnnotation.dictCode();
                    String dictText = dictAnnotation.dictText();
                    String dictTable = dictAnnotation.dictTable();

                    // 查询字典值
                    String dictLabel = queryDictLabel(dictTable, dictCode, dictText, fieldValue.toString());
                    if (dictLabel != null) {
                        try {
                            // 动态生成 _dictText 字段名
                            String textFieldName = field.getName() + "_dictText";
                            Field textField = dto.getClass().getDeclaredField(textFieldName);
                            textField.setAccessible(true);
                            textField.set(dto, dictLabel); // 设置 _dictText 字段的值
                        } catch (NoSuchFieldException e) {
                            // 如果 _dictText 字段不存在，忽略错误
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private String queryDictLabel(String dictTable, String dictCode, String dictText, String dictValue) {
        String sql;
        if (StringUtils.isEmpty(dictTable)) {
            // 场景 1：默认查询 sys_dict_data 表
            sql = "SELECT dict_label FROM sys_dict_data WHERE dict_type = ? AND dict_value::varchar = ? LIMIT 1";
            try {
                return jdbcTemplate.queryForObject(sql, String.class, dictCode, dictValue);
            } catch (DataAccessException e) {
                // 如果查询结果为空，返回 空字符串
                return "";
            }
        } else {
            // 场景 2：查询指定表
            sql = String.format("SELECT %s FROM %s WHERE %s::varchar = ? LIMIT 1", dictText, dictTable, dictCode);
            try {
                return jdbcTemplate.queryForObject(sql, String.class, dictValue);
            } catch (DataAccessException e) {
                // 如果查询结果为空，返回 空字符串
                return "";
            }
        }
    }
}