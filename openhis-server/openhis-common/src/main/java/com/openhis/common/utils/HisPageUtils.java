package com.openhis.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class HisPageUtils {

    /**
     * 执行分页查询并转换为目标类型
     *
     * @param mapper MyBatis Plus Mapper 接口
     * @param queryWrapper 查询条件
     * @param pageNo 当前页
     * @param pageSize 每页大小
     * @param targetClass 目标类（如 MedicationDto.class）
     * @param <T> 源对象类型（数据库实体类）
     * @param <R> 目标对象类型（DTO 类）
     * @return 分页结果（目标类型）
     */
    public static <T, R> Page<R> selectPage(BaseMapper<T> mapper, QueryWrapper<T> queryWrapper, int pageNo,
        int pageSize, Class<R> targetClass) {
        // 构建分页对象
        Page<T> page = new Page<>(pageNo, pageSize);
        // 执行分页查询
        Page<T> sourcePage = mapper.selectPage(page, queryWrapper);
        // 转换为目标类型的分页对象
        Page<R> targetPage = new Page<>();
        // 复制分页信息
        BeanUtils.copyProperties(sourcePage, targetPage);
        // 转换记录列表
        targetPage.setRecords(convertToDtoList(sourcePage.getRecords(), targetClass));
        return targetPage;
    }

    /**
     * 将源对象列表转换为目标对象列表
     *
     * @param sourceList 源对象列表
     * @param targetClass 目标类
     * @param <T> 源对象类型
     * @param <R> 目标对象类型
     * @return 目标对象列表
     */
    private static <T, R> List<R> convertToDtoList(List<T> sourceList, Class<R> targetClass) {
        return sourceList.stream().map(source -> convertToDto(source, targetClass)).collect(Collectors.toList());
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param targetClass 目标类
     * @param <T> 源对象类型
     * @param <R> 目标对象类型
     * @return 目标对象
     */
    private static <T, R> R convertToDto(T source, Class<R> targetClass) {
        try {
            // 创建目标对象实例
            R target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target); // 复制属性
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to DTO", e);
        }
    }
}
