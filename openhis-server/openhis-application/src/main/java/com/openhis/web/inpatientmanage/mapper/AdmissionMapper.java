package com.openhis.web.inpatientmanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inpatientmanage.dto.AdmissionDto;
import com.openhis.web.inpatientmanage.dto.AdmissionUpDto;

/**
 * 住院登记
 *
 * @author liuhr
 * @since 2025/04/08
 */
@Repository
public interface AdmissionMapper {

    /**
     * 住院登记信息分页查询
     *
     * @param page 分页
     * @param classEnum 类别编码(1：住院类型)
     * @param formEnum 类别编码(1：住院类型)
     * @param queryWrapper 查询条件
     * @return 住院登记信息
     */
    IPage<AdmissionDto> getPage(@Param("page") Page<AdmissionDto> page, @Param("classEnum") Integer classEnum,
        @Param("formEnum") Integer formEnum, @Param(Constants.WRAPPER) QueryWrapper<AdmissionDto> queryWrapper);

    /**
     * 住院登记详情
     *
     * @param classEnum 类别编码(1:住院类型)
     * @param statusEnum 状态编码(4:出院状态)
     * @param bedForm 床:8
     * @param wardForm 病区:4
     * @param operational 床位状态(4:占用)
     * @param typeCode 就诊参与者身份类型 (1:接诊医生)
     * @param id 就诊ID
     * @param tenantId 租户
     * @return 住院登记详情
     */
    AdmissionUpDto getAdmissionOne(@Param("classEnum") Integer classEnum, @Param("statusEnum") Integer statusEnum,
        @Param("bedForm") Integer bedForm, @Param("wardForm") Integer wardForm, @Param("typeCode") String typeCode,
        @Param("operational") Integer operational, @Param("id") Long id, @Param("tenantId") Integer tenantId);
}
