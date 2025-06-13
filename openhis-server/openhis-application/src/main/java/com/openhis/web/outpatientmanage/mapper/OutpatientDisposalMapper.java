/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.mapper;

import java.util.List;

import com.openhis.web.outpatientmanage.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.pharmacymanage.dto.*;

@Repository
public interface OutpatientDisposalMapper {

    /**
     * 就诊病人列表分页查询
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 就诊病人列表
     */
    Page<OutpatientDisposalEncounterInfoPageDto> selectEncounterInfoListPage(@Param("page") Page<EncounterInfoPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<EncounterInfoSearchParam> queryWrapper);

    /**
     * 患者基本信息查询
     *
     * @param encounterId 就诊号
     * @return 患者基本信息
     */
    OutpatientDisposalPatientInfoDto selectEncounterPatientInfo(@Param("encounterId") Long encounterId);

    /**
     * 诊疗查询
     *
     * @param encounterId 就诊号
     * @param serviceTableName 服务申请表名
     * @param requestTableName 器材请求表名
     * @param device 耗材
     * @param activity 诊疗
     * @return 诊疗单列表
     */
    List<OutpatientDisposalTempInfoDto> selectEncounterActivityInfoList(@Param("encounterId") Long encounterId,
        @Param("serviceTableName") String serviceTableName, @Param("requestTableName") String requestTableName,
        @Param("DEVICE") Integer device, @Param("activity") Integer activity);

    /**
     * 耗材查询
     *
     * @param groupNo 分组编号
     * @param encounterId 就诊号
     * @param requestTableName 器材请求表名
     * @return 耗材列表
     */
    List<OutpatientDisposalDeviceInfoDto> selectEncounterDeviceInfoList(@Param("groupNo") String groupNo,
        @Param("encounterId") Long encounterId, @Param("requestTableName") String requestTableName);

    /**
     * 执行查询
     *
     * @param type 类型
     * @param busNo 编码
     * @param activityId 诊疗ID
     * @param status 状态
     * @return 耗材列表
     */
    List<OutpatientDisposalExecuteInfoDto> selectActivityExecuteInfo(@Param("type") Integer type,
        @Param("busNo") String busNo, @Param("activityId") Long activityId, @Param("status") Integer status);

    /**
     * 执行查询
     *
     * @param type 类型
     * @param busNo 编码
     * @param activityId 诊疗ID
     * @param status 状态
     * @return 耗材列表
     */
    List<OutpatientDisposalExecuteInfoDto> selectDeviceExecuteInfo(@Param("type") Integer type,
        @Param("busNo") String busNo, @Param("activityId") Long activityId, @Param("status") Integer status);

    /**
     * 诊疗查询
     *
     * @param itemId 诊疗号
     * @param serviceTableName 服务申请表名
     * @param activity 诊疗
     * @return 诊疗单列表
     */
    OutpatientDisposalActivityInfoDto selectEncounterActivityInfo(@Param("itemId") Long itemId,
        @Param("serviceTableName") String serviceTableName, @Param("activity") Integer activity);

    /**
     * 诊疗查询
     *
     * @param itemId 诊疗号
     * @param requestTableName 器材请求表名
     * @param device 耗材
     * @return 诊疗单列表
     */
    OutpatientDisposalActivityInfoDto selectEncounterActivityDeviceInfo(@Param("itemId") Long itemId,
        @Param("requestTableName") String requestTableName, @Param("DEVICE") Integer device);
}
