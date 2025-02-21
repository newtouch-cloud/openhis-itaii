package com.core.common.biz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;

public interface HisBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据ID逻辑删除单条记录
     */
    default int logicalDelById(Long id) {
        String tableName = getTableName();
        return logicalDelById(tableName, id);
    }

    /**
     * 根据ID列表批量逻辑删除记录
     */
    default int logicalDelByIds(List<Long> ids) {
        String tableName = getTableName();
        return logicalDelByIds(tableName, ids);
    }

    /**
     * 根据 UpdateWrapper 逻辑删除记录
     */
    default int logicalDelByWrapper(LambdaUpdateWrapper<T> updateWrapper) {
        String tableName = getTableName();
        return logicalDelByWrapper(updateWrapper, tableName);
    }

    @Update("UPDATE ${tableName} SET delete_flag = 1 WHERE id = #{id}")
    int logicalDelById(@Param("tableName") String tableName, @Param("id") Long id);

    @Update({
            "<script>",
            "UPDATE ${tableName} SET delete_flag = 1 WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int logicalDelByIds(@Param("tableName") String tableName, @Param("ids") List<Long> ids);

    @Update("UPDATE ${tableName} SET delete_flag = 1 ${ew.customSqlSegment}")
    int logicalDelByWrapper(@Param(Constants.WRAPPER) LambdaUpdateWrapper<T> updateWrapper, @Param("tableName") String tableName);

    /**
     * 获取表名
     */
    default String getTableName() {
        TableName tableNameAnnotation = AnnotationUtils.findAnnotation(this.getClass(), TableName.class);
        if (tableNameAnnotation == null) {
            throw new RuntimeException("Table name annotation not found on Mapper interface");
        }
        return tableNameAnnotation.value();
    }
}
