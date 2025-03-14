package com.openhis.web.doctorstation.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;

/**
 * 医生站-医嘱/处方 应用Mapper
 */
@Repository
public interface DoctorStationAdviceAppMapper {

    /**
     * 查询医嘱信息
     * 
     * @param page 分页参数
     * @param medicationTableName 药品定义表名
     * @param deviceTableName 耗材定义表名
     * @param activityTableName 诊疗定义表名
     * @param queryWrapper 查询条件
     * @return 医嘱信息
     */
    IPage<AdviceBaseDto> getAdviceBaseInfo(@Param("page") Page<AdviceBaseDto> page,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("activityTableName") String activityTableName,
        @Param(Constants.WRAPPER) QueryWrapper<AdviceBaseDto> queryWrapper);

}
